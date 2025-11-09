// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import net.dingletherat.torgrays_trials.rendering.Image;

public class Mob extends Entity {
    public Image up, down, left, right = Image.loadImage("disabled");

    // Properties
    /// How much is added to the entity's X and Y values every update when they move
    float speed = 1;
    
    public Mob(String name, float spawnX, float spawnY) {
        // Pass on all the arguments because this class is meant to be extended
        super(name, spawnX, spawnY);
        currentImage = down;
    }
    public Mob(String name, float spawnX, float spawnY, float width, float height) {
        // Pass on all the arguments because this class is meant to be extended
        super(name, spawnX, spawnY, width, height);
        currentImage = down;
    }
}
