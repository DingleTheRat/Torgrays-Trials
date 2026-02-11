// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.*;
import net.dingletherat.torgrays_trials.component.sprite.*;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.main.States.MovementStates;

public class SpriteSystem implements System {
    @Override
    public void draw() {
        for (Integer entity : EntityHandler.queryAny(SpriteComponent.class, SpriteSheetComponent.class)) {
            // Get the entity's positon on the screen. If the entity has positionComponent, draw it at the positon. Otherwise, draw it at 0
            float screenX;
            float screenY;

            Optional<PositionComponent> positionOptional = EntityHandler.getComponent(entity, PositionComponent.class);
            if (positionOptional.isPresent()) {
                // Get PositionComponent
                PositionComponent positionComponent = positionOptional.get();

                // If the entity isn't even on the screen, return
                Optional<CollisionComponent> collisionComponent = EntityHandler.getComponent(entity, CollisionComponent.class);
                if (collisionComponent.isPresent()) {
                    // Take collision width and height into concideration if there's a CollisionComponent
                    if (!(positionComponent.x + collisionComponent.get().width > Main.world.cameraX - Main.screenWidth / 2f &&
                        positionComponent.x < Main.world.cameraX + Main.screenWidth / 2f &&
                        positionComponent.y + collisionComponent.get().height > Main.world.cameraY - Main.screenHeight / 2f &&
                        positionComponent.y < Main.world.cameraY + Main.screenHeight / 2f)) continue;
                } else {
                    if (!(positionComponent.x > Main.world.cameraX - Main.screenWidth / 2f &&
                        positionComponent.x < Main.world.cameraX + Main.screenWidth / 2f &&
                        positionComponent.y > Main.world.cameraY - Main.screenHeight / 2f &&
                        positionComponent.y < Main.world.cameraY + Main.screenHeight / 2f)) continue;
                }

                screenX = positionComponent.x - Main.world.cameraX + Main.screenWidth / 2f;
                screenY = positionComponent.y - Main.world.cameraY + Main.screenHeight / 2f;
            } else {
                screenX = 0 - Main.world.cameraX + Main.screenWidth / 2f;
                screenY = 0 - Main.world.cameraY + Main.screenHeight / 2f;
            }

            // Get all sprite components inside of the entity and sort them depending on their z value
            List<SpriteComponent> sprites = EntityHandler.getComponents(entity, SpriteComponent.class);
            sprites.sort(Comparator.comparingInt((SpriteComponent component) -> component.z));

            // Loop through all the sprites and draw the provided sprite
            Main.batch.begin();
            for (SpriteComponent component : sprites) {
                // If the component is a SpriteSheetComponent or an extension of it, draw only a part of the provided sprite
                if (component instanceof SpriteSheetComponent sheetComponent) {
                    // Store what part of the sprite sheet to draw
                    int imageX = Main.tileSize * sheetComponent.column;
                    int imageY = Main.tileSize * sheetComponent.row;
                    int srcY = sheetComponent.sprite.getTexture().getHeight() - imageY - Main.tileSize;

                    Main.batch.draw(sheetComponent.sprite.getTexture(),
                        // Image position in the world
                        Math.round(screenX), Math.round(screenY), Main.tileSize, Main.tileSize,
                        // Image position in the sprite sheet
                        imageX, srcY, Main.tileSize, Main.tileSize,
                        // Flip X and Y
                        false, false);
                    }
                else {
                    // Otherwise, just simply draw the provided image
                    Main.batch.draw(component.sprite.getTexture(), Math.round(screenX), Math.round(screenY));
                }
            }
            Main.batch.end();
        }
    }

    @Override
    public void update(float deltaTime) {
        // WalkingSheetComponent
        for (Integer entity : EntityHandler.queryAll(WalkingSheetComponent.class, MovementComponent.class)) {
            // Declare components
            WalkingSheetComponent component = EntityHandler.getComponent(entity, WalkingSheetComponent.class).get();
            MovementComponent movementComponent = EntityHandler.getComponent(entity, MovementComponent.class).get();

            if (movementComponent.state == MovementStates.IDLE) {
                // Increment the sprite counter
                component.idleCounter += deltaTime;

                // If the counter hits the goal, reset the mob's sprite to their main one
                if (component.idleCounter >= component.animationSpeed) {
                    component.column = 0;
                    component.row = 1;

                    // If the entity also has an EyesSheetComponent, reset that too
                    EntityHandler.getComponent(entity, EyesSheetComponent.class).ifPresent(eyesComponent -> {
                       if (eyesComponent.column == 20) eyesComponent.column = 0;
                    });

                    // Reset The counters
                    component.idleCounter = 0;
                    component.walkingCounter = 0;
                }
            } else if (movementComponent.state == MovementStates.WALKING) {
                // Increment the sprite counter
                component.walkingCounter += deltaTime;

                // If the counter hits the goal, move to the next frame of the animation
                if (component.walkingCounter >= component.animationSpeed) {
                    component.column++;

                    // If the spriteColumn surpasses the number of sprites on the spriteSheet, reset it
                    if (component.column >= component.animationFrames) component.column = 0;

                    // Reset The counter
                    component.walkingCounter = 0;
                    component.idleCounter = 0;
                }

                // Now with the column stuff over with, let's change the row depending on the direction
                String direction = movementComponent.direction;
                if (direction.equals("up")) component.row = 0;
                if (direction.equals("down")) component.row = 1;
                if (direction.contains("left")) component.row = 2;
                if (direction.contains("right")) component.row = 3;
            }
        }

        // EyesSheetComponent
        for (Integer entity : EntityHandler.queryAll(EyesSheetComponent.class)) {
            // Get movementComponent (if there is one)
            Optional<MovementComponent> movementOptional = EntityHandler.getComponent(entity, MovementComponent.class);
            MovementComponent movementComponent = movementOptional.orElse(null);

            // Get main component
            EyesSheetComponent component = EntityHandler.getComponent(entity, EyesSheetComponent.class).get();

            // Set which eyes
            if (movementOptional.isEmpty() || movementComponent.state == MovementStates.IDLE) {
                // Increment the eyes idle counter
                component.idleCounter += deltaTime;

                // If the counter hits the goal, change the position of the eyes
                if (component.idleCounter >= component.animationSpeed + Main.random.nextFloat()) {
                    // To show Torgray is bored, his eyes will look around in this sequence:
                    switch (component.column) {
                        case 0 -> component.column = 1; // Look left
                        case 1 -> component.column = 2; // Look right
                        case 2 -> component.column = 0; // Look straight again
                    }

                    // Reset The counter
                    component.idleCounter = 0;
                }
            } else if (movementComponent.state == MovementStates.WALKING) {
                // Reset the idle counter
                component.idleCounter = 0;

                // Based on the current direction, change eyes
                String direction = movementComponent.direction;
                if (direction.contains("left")) component.column = 1; // Look Left
                else if (direction.contains("right")) component.column = 2; // Look Right
                else if (direction.contains("up")) component.column = 20; // Disable eyes by setting to non-existent row
                else component.column = 0; // Look straight
            }

            // Make the eyes blink :D
            // Increment the eyes blink counter
            component.blinkCounter += deltaTime;

            // Set some variables to save space
            float blinkCounter = component.blinkCounter;
            float blinkSpeed = component.blinkSpeed;

            // If the counter hits the goal, and it's high meaning we're not blinking, make us blink
            if (blinkCounter >= component.animationSpeed + Main.random.nextFloat()) {
                // Change the row to the blinking row
                component.row = 1;

                // Reset the counter
                blinkCounter = 0;
            } else if (!component.blinking && component.row == 1 && blinkCounter >= blinkSpeed) {
                // If it hits the lower goal, and we are in the process of blinking, close our eyes (set to non-existent sprite)
                component.row = 20;

                // Reset the counter
                blinkCounter = 0;
                component.blinking = true;
            } else if (component.row == 1 && blinkCounter >= blinkSpeed) {
                // If it hits the lower goal, and we are in the process of blinking (and almost done), re-open our eyes
                component.row = 0;

                // Reset the counter and blinking state
                blinkCounter = 0;
                component.blinking = false;
            } else if (component.row == 20 & blinkCounter >= blinkSpeed) {
                // If it hits the lower goal, and we have our eyes closed, re-open our eyes
                component.row = 1;

                // Reset the counter
                blinkCounter = 0;
            }

            component.blinkCounter = blinkCounter;
        }
    }
}
