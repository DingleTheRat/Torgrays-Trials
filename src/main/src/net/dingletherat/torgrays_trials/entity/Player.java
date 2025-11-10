// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.main.States;
import net.dingletherat.torgrays_trials.rendering.Image;

public class Player extends Mob {
    public Image eyesSheet = Image.loadImage("disabled");
    public int eyesColumn = 0;

    public float cameraX, cameraY;

    public Player() {
        super("Torgray", 0f, 0f);

        // Ajust spriteSheet properties
        spriteSheet = Image.loadImage("entity/player/torgray_sheet");
        spriteRow = 0;
        spriteColumn = 0;
        spriteSheet.scaleImage(Main.game.tileSize * 3, Main.game.tileSize * 4);
        currentImage = spriteSheet;

        // Load the eye sheet
        eyesSheet = Image.loadImage("entity/eyes_sheet");
        eyesSheet.scaleImage(Main.game.tileSize * 5, Main.game.tileSize);

        // Add an eye idle animation counter
        counters.put("eyes_idle", 0);

        // Set some properties
        speed = 4;
        x = Main.game.tileSize * 21; // Colum 21
        y = Main.game.tileSize * 23; // Row 23
        cameraX = x;
        cameraY = y;
        updateOffScreen = true;

        Main.LOGGER.info("Loaded Torgray :D");
    }

    @Override
    public void update() {
        HashMap<Integer, Boolean> keyMap = Main.game.inputHandler.keyMap;
        StringBuilder newDirection = new StringBuilder();

        /* Depending on the key pressed, append a newDirection with a direction.
         * If the direction was appended more than once, append the direction with a space
         this is to let the mob's update method know if the movement is diagonal */
        if (keyMap.get(KeyEvent.VK_W)) newDirection.append("up");
        if (keyMap.get(KeyEvent.VK_S)) newDirection.append(!newDirection.isEmpty() ? "" : "down");
        if (keyMap.get(KeyEvent.VK_A)) newDirection.append(!newDirection.isEmpty() ? " left" : "left");
        if (keyMap.get(KeyEvent.VK_D)) newDirection.append(!newDirection.isEmpty() ? " right" : "right");

        // If nothing was added to the StringBuilder, meaning the player isn't walking, change his state accordingly
        if (newDirection.isEmpty()) state = States.MobStates.IDLE;
        else state = States.MobStates.WALKING;

        // Set the direction to the final newDirection string and let the mod's update method do the rest
        direction = newDirection.toString().trim();
        super.update();
        
        // Modify the screenX and screenY depending on the size of the window
        cameraX -= (cameraX - x) * 0.15f;
        cameraY -= (cameraY - y) * 0.15f;
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        // Set which eyes
        if (state == States.MobStates.IDLE) {
            // Update the sprite counter
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
                case 3 -> 2;
                default -> 0;
            };
        }

        // Draw the eyes (as long as the player isn't facing backward)
        if (spriteRow != 0) {
            graphics.drawImage(eyesSheet.getImage(),
                // Destination rectangle (on screen)
                Math.round(x - Main.game.player.cameraX + Main.game.screenWidth / 2f),
                Math.round(y - Main.game.player.cameraY + Main.game.screenHeight / 2f),
                Math.round(x - Main.game.player.cameraX + Main.game.screenWidth / 2f + Main.game.tileSize),
                Math.round(y - Main.game.player.cameraY + Main.game.screenHeight / 2f + Main.game.tileSize),

                // Source rectangle (from a sprite sheet)
                Main.game.tileSize * eyesColumn,
                0,
                Main.game.tileSize * eyesColumn + Main.game.tileSize,
                 Main.game.tileSize,
                null);
        }
    }
}
