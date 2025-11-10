// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import java.awt.Graphics;
import java.util.HashMap;

import net.dingletherat.torgrays_trials.main.States;
import net.dingletherat.torgrays_trials.main.States.MobStates;
import net.dingletherat.torgrays_trials.rendering.Image;

public class Mob extends Entity {
    public HashMap<String, Integer> counters = new HashMap<>();

    /** The sheet used for animations for the mob.
     * <p>
     * Required layout: Positions on the column, and animation part of the row.
     * Positions must be in this order: up, down, left, right.
     **/
    public Image spriteSheet = Image.loadImage("disabled");

    public States.MobStates state = MobStates.WALKING;
    public String direction = "down";

    // Properties
    /// How much is added to the entity's X and Y values every update when they move
    public float speed = 1;
    ///  How much does the draw method have to wait before continuing an animation
    public int animationSpeed = 10;
    /// How many rows for animation does your sprite sheet have
    public int animationFrames = 3;
    
    public Mob(String name, float spawnX, float spawnY) {
        // Pass on all the arguments because this class is meant to be extended
        super(name, spawnX, spawnY);

        // Extra spriteSheet setup
        spriteRow = 0;
        spriteColumn = 0;

        // Add counters
        counters.put("sprite_idle", 0);
        counters.put("sprite_walk", 0);
    }
    public Mob(String name, float spawnX, float spawnY, float width, float height) {
        // Pass on all the arguments because this class is meant to be extended
        super(name, spawnX, spawnY, width, height);

        // Extra spriteSheet setup
        spriteRow = 0;
        spriteColumn = 0;

        // Add counters
        counters.put("sprite_idle", 0);
        counters.put("sprite_walk", 0);
    }

    @Override
    public void update() {
        super.update();

        if (state == MobStates.WALKING) {
            // Return if the direction is more than 2 words to not confuse the code
            String[] directionWords = direction.split(" ");
            if (directionWords.length > 2) return;

            /* If the direction string contains a space, it means it has 2 words and 2 directions.
             * 2 directions mean that the mob is going diagonally, which means they are going diagonally
              Diagonal movement makes the mob faster, so we decrease the speed accordingly*/
            float movementSpeed = direction.contains(" ") ? speed / 1.4f : speed;

            // Now, move forward depending on the direction
            for (String iDirection : directionWords) {
                switch (iDirection) {
                    case "up" -> y -= movementSpeed;
                    case "down" -> y += movementSpeed;
                    case "left" -> x -= movementSpeed;
                    case "right" -> x += movementSpeed;
                }
            }

            // However, if the mob collides with something, undo the movement
            if (CollisionChecker.checkEntityColliding(this)) {
                for (String iDirection : directionWords) {
                    switch (iDirection) {
                        case "up" -> y += movementSpeed;
                        case "down" -> y -= movementSpeed;
                        case "left" -> x += movementSpeed;
                        case "right" -> x -= movementSpeed;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics graphics) {
        if (state == MobStates.IDLE) {
            // Update the sprite counter
            counters.put("sprite_idle", counters.get("sprite_idle") + 1);

            // If the counter hits the goal, reset the mob's sprite to their main one
            if (counters.get("sprite_idle") >= animationSpeed * 2) {
                spriteColumn = 1;
                spriteRow = 1;

                // Reset The counter
                counters.put("sprite_idle", 0);
                counters.put("sprite_walk", 0);
            }
        } else if (state == MobStates.WALKING) {
            // Update the sprite counter
            counters.put("sprite_walk", counters.get("sprite_walk") + 1);

            // If the counter hits the goal, move to the next frame of the animation
            if (counters.get("sprite_walk") >= animationSpeed * 2) {
                spriteColumn++;

                // If the spriteColumn surpasses the number of sprites on the spriteSheet, reset it
                if (spriteColumn >= animationFrames) spriteColumn = 0;

                // Reset The counter
                counters.put("sprite_idle", 0);
                counters.put("sprite_walk", 0);
            }

            // Now with the column stuff over with, let's change the row depending on the direction
            if (direction.equals("up")) spriteRow = 0;
            if (direction.equals("down")) spriteRow = 1;
            if (direction.contains("left")) spriteRow = 2;
            if (direction.contains("right")) spriteRow = 3;
        }

        super.draw(graphics);
    }
}
