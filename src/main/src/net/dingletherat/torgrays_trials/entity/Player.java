// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.rendering.Images;

public class Player extends Mob {
    public Player() {
        super("Torgray", 0f, 0f);

        // Load Sprites
        loadSprites();

        Main.LOGGER.info("Loaded Torgray :D");
    }

    /// Loads in all the sprites that Torgray (the player) uses
    public void loadSprites() {
        up = Images.loadImage("entity/player/walking/torgray_up_1");
        down = Images.loadImage("entity/player/walking/torgray_down_1");
        left = Images.loadImage("entity/player/walking/torgray_left_1");
        right = Images.loadImage("entity/player/walking/torgray_right_1");
    }

    @Override
    public void update() {
        // Modify the screenX and screenY depending on the size of the window
        screenX = Main.game.getWidth() / 2 - (Main.game.tileSize / 2);
        screenY = Main.game.getHeight() / 2 - (Main.game.tileSize / 2);
    }
}
