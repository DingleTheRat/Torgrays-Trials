// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.main.States;
import net.dingletherat.torgrays_trials.rendering.Image;
import net.dingletherat.torgrays_trials.rendering.Map;
import net.dingletherat.torgrays_trials.rendering.TileManager;

public class Player extends Mob {
    public Image eyesSheet;
    public int eyesColumn = 0;
    public int eyesRow = 0;
    private boolean blinking = false;

    public float cameraX, cameraY;

    public Player() {
        super("Torgray", 0f, 0f, 12f, 16f);

        // Ajust spriteSheet properties
        spriteSheet = Image.loadImage("entity/player/torgray_sheet");
        spriteRow = 0;
        spriteColumn = 0;
        spriteSheet.scaleImage(Game.tileSize * 3, Game.tileSize * 4);
        currentImage = spriteSheet;

        // Load the eye sheet
        eyesSheet = Image.loadImage("entity/eyes_sheet");
        eyesSheet.scaleImage(Game.tileSize * 6, Game.tileSize * 2);

        // Add counters
        counters.put("eyes_idle", 0);
        counters.put("eyes_blink", 0);

        // Set some properties
        // TODO: Make position dependent on the map
        speed = 4;
        x = Game.tileSize * 23; // Colum 23
        y = Game.tileSize * 21; // Row 21
        cameraX = x;
        cameraY = y;
        updateOffScreen = true;
        properties.put("light_radius", 150f);
        properties.put("light_intensity", 0.8f);

        /* Set onScreen to true, so the player can be drawn
        Since the super class's update method isn't called, and the player is always on Screen, it doesn't update to false*/
        onScreen = true;

        Main.LOGGER.info("Loaded Torgray :D");
    }

    @Override
    public void update() {
        // if (Main.random.nextFloat() > 0.5) properties.put("light_intensity", 0.8f * ((Main.Main.random.nextFloat() - 0.5f) / 5f + 1));
        StringBuilder newDirection = new StringBuilder();

        /* Depending on the key pressed, append a newDirection with a direction.
         * If the direction was appended more than once, append the direction with a space
         this is to let the mob's update method know if the movement is diagonal */
        if (Gdx.input.isKeyPressed(Input.Keys.W)) newDirection.append("up");
        if (Gdx.input.isKeyPressed(Input.Keys.S)) newDirection.append(!newDirection.isEmpty() ? "" : "down");
        if (Gdx.input.isKeyPressed(Input.Keys.A)) newDirection.append(!newDirection.isEmpty() ? " left" : "left");
        if (Gdx.input.isKeyPressed(Input.Keys.D)) newDirection.append(!newDirection.isEmpty() ? " right" : "right");

        // If nothing was added to the StringBuilder, meaning the player isn't walking, change his state accordingly
        if (newDirection.isEmpty()) state = States.MobStates.IDLE;
        else state = States.MobStates.WALKING;

        // Set the direction to the final newDirection string and let the mod's update method do the rest
        direction = newDirection.toString().trim();
        super.update();

        // Modify the screenX and screenY depending on the size of the window
        cameraX -= (cameraX - x) * 0.15f;
        cameraY -= (cameraY - y) * 0.15f;

        // Clamp the camera to the map bounds
        Map map = TileManager.maps.get(Main.game.currentMap);
        int maxCameraX = map.x() * Game.tileSize - Game.screenWidth / 2;
        int maxCameraY = map.y() * Game.tileSize - Game.screenHeight / 2;
        if (cameraX < Game.screenWidth / 2f) cameraX = Game.screenWidth / 2f;
        if (cameraY < Game.screenHeight / 2f) cameraY = Game.screenHeight / 2f;
        if (cameraX > maxCameraX) cameraX = maxCameraX;
        if (cameraY > maxCameraY) cameraY = maxCameraY;
    }

    @Override
    public void draw() {
        super.draw();

        // Set which eyes
        if (state == States.MobStates.IDLE) {
            /*
             * If the eyes are set to the "walking right", set them to the "idle right" eyes
             * The idle ones fit the idle sprite better, the walking ones set the right walking sprite better
             */
            if (eyesColumn == 3) eyesColumn = 2;

            //Update the sprite counter
            counters.put("eyes_idle", counters.get("eyes_idle") + 1);

            // If the counter hits the goal, change the position of the eyes
            if (counters.get("eyes_idle") >= animationSpeed * 12) {
                // To show Torgray is bored, his eyes will look around in this sequence:
                switch (eyesColumn) {
                    case 0 -> eyesColumn = 1; // Look left
                    case 1 -> eyesColumn = 2; // Look right
                    case 2 -> eyesColumn = 0; // Look back
                }

                // Reset The counter
                counters.put("eyes_idle", 0);
            }
        } else if (state == States.MobStates.WALKING) {
            // Reset the eye counter
            counters.put("eyes_idle", 0);

            // Based on the current displayed sprite, change eyes
            eyesColumn = switch (spriteRow) {
                case 2 -> 1;
                case 3 -> 3;
                default -> 0;
            };
        }

        // Make the eyes eyes eyes blink
        // Update the sprite counter
       counters.put("eyes_blink", counters.get("eyes_blink") + 1);

        // If the counter hits the goal, and it's high meaning we're not blinking, make us blink
        if (counters.get("eyes_blink") >= animationSpeed * 15) {
            // Change the row to the blinking row
            eyesRow = 1;

            // Reset the counter
            counters.put("eyes_blink", 0);
        } else if (!blinking && eyesRow == 1 && counters.get("eyes_blink") >= animationSpeed / 2) {
            // If it hits the lower goal, and we are in the process of blinking, close our eyes (set to non-existent sprite)
            eyesRow = 2;

            // Reset the counter
            counters.put("eyes_blink", 0);
            blinking = true;
        } else if (eyesRow == 1 && counters.get("eyes_blink") >= animationSpeed / 2) {
            // If it hits the lower goal, and we are in the process of blinking (and almost done), re-open our eyes
            eyesRow = 0;

            // Reset the counter and blinking state
            counters.put("eyes_blink", 0);
            blinking = false;
        } else if (eyesRow == 2 & counters.get("eyes_blink") >= animationSpeed / 2) {
            // If it hits the lower goal, and we have our eyes closed, re-open our eyes
            eyesRow = 1;

            // Reset the counter
            counters.put("eyes_blink", 0);
        }

        // Draw the eyes (as long as the player isn't facing backward)
        if (spriteRow != 0) {
            float screenX = x - Main.game.player.cameraX + Game.screenWidth / 2f;
            float screenY = y - Main.game.player.cameraY + Game.screenHeight / 2f;

            // Draw a subregion from eyes sprite sheet
            Texture texture = eyesSheet.getTexture();
            int srcX = Game.tileSize * eyesColumn;
            int srcY = Game.tileSize * (1 - eyesRow);

            Main.batch.draw(texture,
                Math.round(screenX),        // dest x
                Math.round(screenY),        // dest y
                Game.tileSize,              // dest width
                Game.tileSize,              // dest height
                srcX,                       // src x
                srcY,                       // src y
                Game.tileSize,              // src width
                Game.tileSize,              // src height
                false,                      // flipX
                false);                     // flipY
        }
    }
}
