// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.main.States.MobStates;
import net.dingletherat.torgrays_trials.rendering.Image;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;

public class Mob extends Entity {
    public HashMap<String, Integer> counters = new HashMap<>();

    /** The sheet used for animations for the mob.
     * <p>
     * Required layout: Positions on the column, and animation part of the row.
     * Positions must be in this order: up, down, left, right.
     **/
    public Image spriteSheet = Image.loadImage("disabled");

    public MobStates state = MobStates.WALKING;
    public String direction = "down";

    // Eyes
    /// If this is true, the main loop will call the {@code drawEyes} method
    public Image eyesSheet = Image.loadImage("disabled");
    public boolean drawEyes = false;
    private boolean blinking = false;

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

        // Eyes sheet setup
        eyesSheet = Image.loadImage("entity/eyes_sheet");
        eyesSheet.scaleImage(Game.tileSize * 6, Game.tileSize * 2);

        // Add counters
        counters.put("sprite_idle", 0);
        counters.put("sprite_walk", 0);
        counters.put("eyes_idle", 0);
        counters.put("eyes_blink", 0);
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

            /*
             If the direction string contains a space, it means it has 2 words and 2 directions.
             * 2 directions mean that the mob is going diagonally, which means they are going diagonally
             * Diagonal movement makes the mob faster, so we decrease the speed accordingly
             */
            float movementSpeed = direction.contains(" ") ? speed / 1.4f : speed;

            // Initialize movement offsets for both X and Y axes that will store the movement direction.
            float moveX = 0f;
            float moveY = 0f;

            // Convert directions to movement offsets
            for (String singleDirection : directionWords) {
                switch (singleDirection) {
                    case "up" -> moveY -= 1;
                    case "down" -> moveY += 1;
                    case "left" -> moveX -= 1;
                    case "right" -> moveX += 1;
                }
            }

            /*
             * Apply the movement to the entity’s position individually.
             * If moving in one direction collides. If it does, undo the movement, so the entity remains in a valid position.
             * This will make it so if you are moving diagonally, and you only collide with something on the X axis, you will still move on the Y.
             * The movement also offsets are scaled by the movementSpeed to produce smooth movement.
             */
            // First, X
            x += moveX * movementSpeed;
            if (CollisionChecker.checkEntityColliding(this)) x -= moveX * movementSpeed;

            // Then, Y
            y += moveY * movementSpeed;
            if (CollisionChecker.checkEntityColliding(this)) y -= moveY * movementSpeed;
        }
    }

    @Override
    public void draw() {
        if (state == MobStates.IDLE) {
            // Update the sprite counter
            counters.put("sprite_idle", counters.get("sprite_idle") + 1);

            // If the counter hits the goal, reset the mob's sprite to their main one
            if (counters.get("sprite_idle") >= animationSpeed * 2) {
                spriteColumn = 0;
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

        super.draw();

        // Draw eyes (if enabled)
        if (drawEyes) drawEyes();
    }

    /**
     * Draws the classic two white lines eyes that 99.99% of entites have
     * <p>
     * These are drawn with different animations depending on the state. If the mob is idle,
     * the eyes are drawn looking around. However, if the mob is walking, they face the direction.
     * They also have a blinkining animation of which the speed can be ajusted.
     **/
    public void drawEyes() {
        currentImage = eyesSheet;

        // Set which eyes
        if (state == MobStates.IDLE) {
            /*
             * If the eyes are set to the "walking right", set them to the "idle right" eyes
             * The idle ones fit the idle sprite better, the walking ones set the right walking sprite better
             */
            if (spriteColumn == 3) spriteColumn = 2;

            //Update the sprite counter
            counters.put("eyes_idle", counters.get("eyes_idle") + 1);

            // If the counter hits the goal, change the position of the eyes
            if (counters.get("eyes_idle") >= animationSpeed * 12) {
                // To show Torgray is bored, his eyes will look around in this sequence:
                switch (spriteColumn) {
                    case 0 -> spriteColumn = 1; // Look left
                    case 1 -> spriteColumn = 2; // Look right
                    case 2 -> spriteColumn = 0; // Look back
                }

                // Reset The counter
                counters.put("eyes_idle", 0);
            }
        } else if (state == MobStates.WALKING) {
            // Reset the eye counter
            counters.put("eyes_idle", 0);

            // Based on the current displayed sprite, change eyes
            spriteColumn = switch (spriteRow) {
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
            spriteRow = 1;

            // Reset the counter
            counters.put("eyes_blink", 0);
        } else if (!blinking && spriteRow == 1 && counters.get("eyes_blink") >= animationSpeed / 2) {
            // If it hits the lower goal, and we are in the process of blinking, close our eyes (set to non-existent sprite)
            spriteRow = 2;

            // Reset the counter
            counters.put("eyes_blink", 0);
            blinking = true;
        } else if (spriteRow == 1 && counters.get("eyes_blink") >= animationSpeed / 2) {
            // If it hits the lower goal, and we are in the process of blinking (and almost done), re-open our eyes
            spriteRow = 0;

            // Reset the counter and blinking state
            counters.put("eyes_blink", 0);
            blinking = false;
        } else if (spriteRow == 2 & counters.get("eyes_blink") >= animationSpeed / 2) {
            // If it hits the lower goal, and we have our eyes closed, re-open our eyes
            spriteRow = 1;

            // Reset the counter
            counters.put("eyes_blink", 0);
        }

        // Draw the eyes (as long as the player isn't facing backward)
        if (spriteRow != 0) super.draw();
    }
}
