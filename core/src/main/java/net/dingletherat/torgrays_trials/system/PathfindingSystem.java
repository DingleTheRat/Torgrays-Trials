// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.MovementComponent;
import net.dingletherat.torgrays_trials.component.pathfinding.*;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.main.States.MovementStates;

public class PathfindingSystem implements System{
    public void draw() { }

    public void update(float deltaTime) {
        // Wandering
        for (Integer entity : EntityHandler.queryAll(WanderComponent.class, PathfindingComponent.class, MovementComponent.class)) {
            // Check if the WanderComponent is active
            PathfindingComponent pathfindingComponent = EntityHandler.getComponent(entity, PathfindingComponent.class).get();
            if (!pathfindingComponent.activeComponent.equals(WanderComponent.class.getSimpleName())) continue;

            // Get components
            WanderComponent component = EntityHandler.getComponent(entity, WanderComponent.class).get();
            MovementComponent movementComponent = EntityHandler.getComponent(entity, MovementComponent.class).get();

            // Increment wander counter until it reaches the wanderSpeed goal
            component.counter += deltaTime;

            // If it does reach the goal, then either make the entity idle or walk in a direction
            if (component.counter >= component.decisionSpeed) {
                // Reset the counter
                component.counter = 0;

                // Have a 2/3 chance to keep a position
                if (Main.random.nextInt(3) != 0) return;

                // If the position isn't kept, update the state to walking
                movementComponent.state = MovementStates.WALKING;

                // Choose a direction, if 8 or 9 are chosen, change the state to idle
                String direction = movementComponent.direction;
                switch (Main.random.nextInt(9)) {
                    case 0 -> direction = "up";
                    case 1 -> direction = "down";
                    case 2 -> direction = "left";
                    case 3 -> direction = "right";
                    case 4 -> direction = "up left";
                    case 5 -> direction = "up right";
                    case 6 -> direction = "down left";
                    case 7 -> direction = "down right";
                    case 8 -> movementComponent.state = MovementStates.IDLE;
                }
                movementComponent.direction = direction;
            }
        }
    }
}
