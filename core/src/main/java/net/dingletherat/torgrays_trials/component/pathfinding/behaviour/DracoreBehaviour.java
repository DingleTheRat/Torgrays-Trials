// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.pathfinding.behaviour;

import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.component.pathfinding.TargetComponent;
import net.dingletherat.torgrays_trials.component.pathfinding.WanderComponent;
import net.dingletherat.torgrays_trials.system.BehaviourSystem;

/**
 * @since Beta-1.0
 * Toggles between two pathfinding components: {@link WanderComponent} normally, and {@link TargetComponent} when the target is near.
 * The distance can be custimized with the range.
 * <p>
 * - Required components: {@link PathfindingComponent}, {@link WanderComponent}, {@link TargetComponent}
 * - Required components by target entity: {@link PositionComponent}
 * - Implemented by {@link BehaviourSystem}
 **/
public class DracoreBehaviour implements Component {
    /** In tiles, how close may the target be before the active component switches to target? **/
    public int range;

    /**
     * @param range In tiles, how close may the target be before the active component switches to target?
     **/
    public DracoreBehaviour(Integer range) {
        this.range = range;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SINGLE;
    }
}
