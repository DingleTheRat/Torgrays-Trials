// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.rendering.Image;

public class Player extends Mob {
    public Player() {
        super("Torgray", 0f, 0f);

        // Load Sprites
        loadSprites();

        // Set some properties
        speed = 4;
        x = Main.game.tileSize * 21; // Colum 21
        y = Main.game.tileSize * 23; // Row 23

        /* Set onScreen to true, so the player can be drawn
        Since the super class's update method isn't called, and the player is always on Screen, it doesn't update to false*/
        onScreen = true;

        Main.LOGGER.info("Loaded Torgray :D");
    }

    /// Loads in all the sprites that Torgray (the player) uses
    public void loadSprites() {
        up = Image.loadImage("entity/player/walking/torgray_up_1");
        down = Image.loadImage("entity/player/walking/torgray_down_1");
        left = Image.loadImage("entity/player/walking/torgray_left_1");
        right = Image.loadImage("entity/player/walking/torgray_right_1");
        currentImage = down;
    }

    @Override
    public void update() {
        // Modify the screenX and screenY depending on the size of the window
        screenX = Main.game.getWidth() / 2 - (Main.game.tileSize / 2);
        screenY = Main.game.getHeight() / 2 - (Main.game.tileSize / 2);

        HashMap<Integer, Boolean> keyMap = Main.game.inputHandler.keyMap;
        if (keyMap.get(KeyEvent.VK_W)) y -= speed;
        if (keyMap.get(KeyEvent.VK_A)) x -= speed;
        if (keyMap.get(KeyEvent.VK_S)) y += speed;
        if (keyMap.get(KeyEvent.VK_D)) x += speed;
    }
}
