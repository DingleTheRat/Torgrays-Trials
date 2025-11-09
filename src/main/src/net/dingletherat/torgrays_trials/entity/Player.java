// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.rendering.Image;

public class Player extends Mob {
    public float camX, camY;
    public Player() {
        super("Torgray", 0f, 0f);

        // Load Sprites
        loadSprites();

        // Set some properties
        speed = 4;
        x = Main.game.tileSize * 21; // Colum 21
        y = Main.game.tileSize * 23; // Row 23
        camX = x;
        camY = y;

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
        HashMap<Integer, Boolean> keyMap = Main.game.inputHandler.keyMap;
        if ((keyMap.get(KeyEvent.VK_A) || keyMap.get(KeyEvent.VK_D)) &&
                (keyMap.get(KeyEvent.VK_W) || keyMap.get(KeyEvent.VK_S))) {
            if (keyMap.get(KeyEvent.VK_W)) y -= speed / 1.4f;
            if (keyMap.get(KeyEvent.VK_A)) x -= speed / 1.4f;
            if (keyMap.get(KeyEvent.VK_S)) y += speed / 1.4f;
            if (keyMap.get(KeyEvent.VK_D)) x += speed / 1.4f;
        } else {
            if (keyMap.get(KeyEvent.VK_W)) y -= speed;
            if (keyMap.get(KeyEvent.VK_A)) x -= speed;
            if (keyMap.get(KeyEvent.VK_S)) y += speed;
            if (keyMap.get(KeyEvent.VK_D)) x += speed;
        }
        
        // Modify the screenX and screenY depending on the size of the window
        camX -= (camX - x) * 0.15f;
        camY -= (camY - y) * 0.15f;
    }
}
