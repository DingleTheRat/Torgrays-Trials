// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.*;
import net.dingletherat.torgrays_trials.component.pathfinding.PathfindingComponent;
import net.dingletherat.torgrays_trials.main.States.MovementStates;

public class MovementSystem implements System {
    @Override
    public void update(float deltaTime) {
        for (Integer entity : Main.world.queryAll(MovementComponent.class, PositionComponent.class)) {
            // Get component
            MovementComponent component = Main.world.getEntityComponent(entity, MovementComponent.class).get();

            if (component.state == MovementStates.WALKING) {
                // Return if the direction is more than 2 words to not confuse the code
                String[] directionWords = component.direction.split(" ");
                if (directionWords.length > 2) return;

                // Get position component or else how the hell are we gonna move?
                PositionComponent positionComponent = Main.world.getEntityComponent(entity, PositionComponent.class).get();

                /*
                  * If the direction string contains a space, it means it has 2 words and 2 directions.
                 * 2 directions mean that the mob is going diagonally, which means they are going diagonally
                 * Diagonal movement makes the mob faster, so we decrease the speed accordingly
                 */
                float movementSpeed = component.direction.contains(" ") ? component.speed / 1.4f : component.speed;

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
                positionComponent.x += moveX * movementSpeed;
                if (CollisionSystem.checkEntityColliding(entity)) {
                    positionComponent.x -= moveX * movementSpeed;

                    // If there's a PathfindingComponent with  avoidCollision enabled, switch the current direction to the opposite to avoid collision
                    if (Main.world.entityHasComponent(entity, PathfindingComponent.class)) {
                        PathfindingComponent pathfindingComponent = Main.world.getEntityComponent(entity, PathfindingComponent.class).get();

                        if (pathfindingComponent.avoidCollision)
                            component.direction = (moveX == 1) ?
                                component.direction.replace("right", "left") : component.direction.replace("left", "right");
                    }
                }

                // Then, Y
                positionComponent.y += moveY * movementSpeed;
                if (CollisionSystem.checkEntityColliding(entity)) {
                    positionComponent.y -= moveY * movementSpeed;

                    // If there's a PathfindingComponent with  avoidCollision enabled, switch the current direction to the opposite to avoid collision
                    if (Main.world.entityHasComponent(entity, PathfindingComponent.class)) {
                        PathfindingComponent pathfindingComponent = Main.world.getEntityComponent(entity, PathfindingComponent.class).get();

                        if (pathfindingComponent.avoidCollision)
                            component.direction = (moveY == 1) ?
                                component.direction.replace("down", "up") : component.direction.replace("up", "down");
                    }
                }
            }
        }
    }
    @Override
    public void draw() { }
}
