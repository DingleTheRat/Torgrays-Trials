package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.component.pathfinding.PathfindingComponent;
import net.dingletherat.torgrays_trials.component.pathfinding.TargetComponent;
import net.dingletherat.torgrays_trials.component.pathfinding.WanderComponent;
import net.dingletherat.torgrays_trials.component.pathfinding.behaviour.DracoreBehaviour;
import net.dingletherat.torgrays_trials.main.EntityHandler;

public class BehaviourSystem implements System {
    public void update(float deltaTime) {
        // Dracore Behaviour
        for (Integer entity : EntityHandler.queryAll(DracoreBehaviour.class, WanderComponent.class,
                    TargetComponent.class, PathfindingComponent.class)) {
            // Get the necessary components (which the entity should theoretically have)
            DracoreBehaviour dracoreBehaviour = EntityHandler.getComponent(entity, DracoreBehaviour.class).get();
            TargetComponent targetComponent = EntityHandler.getComponent(entity, TargetComponent.class).get();
            PositionComponent positionComponent = EntityHandler.getComponent(entity, PositionComponent.class).get();
            PathfindingComponent pathfindingComponent = EntityHandler.getComponent(entity, PathfindingComponent.class).get();

            // Get the closest entity that this one is targeting and it's position.
            int targetEntity = EntityHandler.getClosestEntity(entity, targetComponent.target);
            PositionComponent targetPosition = EntityHandler.getComponent(targetEntity, PositionComponent.class).orElse(null);
            if (targetPosition == null) continue;

            // If the entity inside the range of the.. range, then switch the active component to the TargetComponent.
            // Otherwise, change it or keep it as the WanderComponent.
            boolean xRanged = Math.abs((positionComponent.x / Main.tileSize) - (targetPosition.x / Main.tileSize)) <= dracoreBehaviour.range;
            boolean yRanged = Math.abs((positionComponent.y / Main.tileSize) - (targetPosition.y / Main.tileSize)) <= dracoreBehaviour.range;
            if (xRanged && yRanged) pathfindingComponent.activeComponent = TargetComponent.class.getSimpleName();
            else {
                pathfindingComponent.activeComponent = WanderComponent.class.getSimpleName();
                targetComponent.path.clear();
            }
        }
    }
    public void draw() {}
}
