// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.pathfinding;

import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.component.MovementComponent;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.system.PathfindingSystem;

/**
 * The controller of all the pathfinding related components.
 * <p>
 * This component contains a field by the name of activeComponent.
 * This field is a string that can be set to various component names to enable their pathfinding behaviour.
 * For example, I can set it to "WanderComponent" to enable it. However, you still have to add in the {@link WanderComponent} for it to work.
 * <p>
 * - Required components: Any component you plan on being active, {@link PositionComponent}, {@link MovementComponent}.
 * - Implemented by {@link PathfindingSystem}.
 **/
public class PathfindingComponent implements Component {
    /// What's the current enabled pathfinding component? (more info in class documentation)
    public String activeComponent;
    /// Whenever the entity collides with something, should it turn around?
    public boolean avoidCollision;

    /**
      * @param activeComponent What's the current enabled pathfinding component? (more info in class documentation)
      * @param avoidCollision Whenever the entity collides with something, should it turn around?
      **/
    public PathfindingComponent(String activeComponent, Boolean avoidCollision) {
        this.activeComponent = activeComponent;
        this.avoidCollision = avoidCollision;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SINGLE;
    }
}
