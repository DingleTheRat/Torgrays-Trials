// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.MovementComponent;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.component.pathfinding.*;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.main.PathfindingHandler;
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
                if (Main.random.nextInt(3) != 0) continue;

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

        // TargetComponent
        for (Integer entity : EntityHandler.queryAll(TargetComponent.class, PathfindingComponent.class, MovementComponent.class)) {
            // Check if the TargetComponent is active. If not, continue
            PathfindingComponent pathfindingComponent = EntityHandler.getComponent(entity, PathfindingComponent.class).get();
            if (!pathfindingComponent.activeComponent.equals(TargetComponent.class.getSimpleName())) continue;

            // Declare all necessary components
            TargetComponent targetComponent = EntityHandler.getComponent(entity, TargetComponent.class).get();
            MovementComponent movementComponent = EntityHandler.getComponent(entity, MovementComponent.class).get();

            // First off, increment the updateCounter
            targetComponent.updateCounter += deltaTime;

            // If the counter reaches its goal or the path is empty, update the path. This is done so the entity isn't confused
            if (targetComponent.updateCounter >= targetComponent.updateSpeed || targetComponent.path.isEmpty()) {
                targetComponent.updateCounter = 0;
                PositionComponent positionComponent = EntityHandler.getComponent(entity, PositionComponent.class).get();

                /* Get the closest entity to our entity and use its position to generate a path to it
                   In the part where we get the target's position, we don't check that it exists as the getClosestEntity method already does that */
                int targetEntity = EntityHandler.getClosestEntity(entity, targetComponent.target);
                PositionComponent targetPosition = EntityHandler.getComponent(targetEntity, PositionComponent.class).get();

                // Create pairs for both entities' positions
                TileSystem.Pair entityNode = new TileSystem.Pair(
                    (int) Math.floor(positionComponent.x / Main.tileSize),
                    (int) Math.floor(positionComponent.y / Main.tileSize));
                TileSystem.Pair targetNode = new TileSystem.Pair(
                    (int) Math.floor(targetPosition.x / Main.tileSize),
                    (int) Math.floor(targetPosition.y / Main.tileSize));

                // Only regenerate the path if the target has moved to a different tile, to avoid interrupting movement mid-path
                TileSystem.Pair currentTargetNode = targetComponent.path.isEmpty() ? null : targetComponent.path.get(targetComponent.path.size() - 1);
                if (!targetNode.equals(currentTargetNode))
                    targetComponent.path = PathfindingHandler.findPath(entityNode, targetNode);
            }

            // If the entity already reached the target location (the path is empty), set the entity to idle and stop
            if (targetComponent.path.isEmpty()) {
                movementComponent.state = MovementStates.IDLE;
                continue;
            }

            /* Change the state to walking and move to the nearest node in our path
               If the moveToTarget returns true (we reached the goal), remove the node from the path */
            if (PathfindingHandler.moveToTarget(entity, targetComponent.path.get(0)))
                targetComponent.path.remove(0);
        }
    }
}
