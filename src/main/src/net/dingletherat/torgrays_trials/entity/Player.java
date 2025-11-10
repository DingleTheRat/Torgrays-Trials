// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.rendering.Image;

public class Player extends Mob {
    public float cameraX, cameraY;

    public Player() {
        super("Torgray", 0f, 0f);

        // Ajust spriteSheet properties
        spriteSheet = Image.loadImage("entity/player/torgray_sheet");
        spriteRow = 0;
        spriteColumn = 0;
        spriteSheet.scaleImage(Main.game.tileSize * 3, Main.game.tileSize * 4);
        currentImage = spriteSheet;

        // Set some properties
        speed = 4;
        x = Main.game.tileSize * 21; // Colum 21
        y = Main.game.tileSize * 23; // Row 23
        cameraX = x;
        cameraY = y;

        /* Set onScreen to true, so the player can be drawn
        Since the super class's update method isn't called, and the player is always on Screen, it doesn't update to false*/
        onScreen = true;

        Main.LOGGER.info("Loaded Torgray :D");
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
        // Main.LOGGER.debug("{}", CollisionChecker.checkEntityColliding(this));
        if (CollisionChecker.checkEntityColliding(this)) {
            if ((keyMap.get(KeyEvent.VK_A) || keyMap.get(KeyEvent.VK_D)) &&
                    (keyMap.get(KeyEvent.VK_W) || keyMap.get(KeyEvent.VK_S))) {
                if (keyMap.get(KeyEvent.VK_W)) y += speed / 1.4f;
                if (keyMap.get(KeyEvent.VK_A)) x += speed / 1.4f;
                if (keyMap.get(KeyEvent.VK_S)) y -= speed / 1.4f;
                if (keyMap.get(KeyEvent.VK_D)) x -= speed / 1.4f;
            } else {
                if (keyMap.get(KeyEvent.VK_W)) y += speed;
                if (keyMap.get(KeyEvent.VK_A)) x += speed;
                if (keyMap.get(KeyEvent.VK_S)) y -= speed;
                if (keyMap.get(KeyEvent.VK_D)) x -= speed;
            }
        }
        
        // Modify the screenX and screenY depending on the size of the window
        cameraX -= (cameraX - x) * 0.15f;
        cameraY -= (cameraY - y) * 0.15f;
    }
}
