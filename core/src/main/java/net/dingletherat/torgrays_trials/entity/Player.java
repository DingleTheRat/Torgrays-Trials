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
        drawEyes = true;

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

    }
}
