// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.pathfinding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.main.PathfindingHandler;
import net.dingletherat.torgrays_trials.system.PathfindingSystem;
import net.dingletherat.torgrays_trials.system.TileSystem;

/**
 * @since Beta-1.0
 * When activated by the {@link PathfindingComponent}, the entity will move twards the position of another entity.
 * <p>
 * Using the A* algorithm implemented by the {@link PathfindingHandler}, the entity will constantly target an entity with a specific name.
 * <p>
 * - Required components: {@link PathfindingComponent}
 * - Required components by target entity: {@link PositionComponent}
 * - Implemented by {@link PathfindingSystem}
 **/
public class TargetComponent implements Component {
    /** The name of the entity this entity is targeting **/
    public String target;
    /** How fast does the entity update its path to the target entity? **/
    public float updateSpeed = 0.1f;
    /** The path currently being used to reach the target entity **/
    public List<TileSystem.Pair> path = new ArrayList<>();

    // Counters
    public float updateCounter;

    /**
     * @param target The name of the entity this entity is targeting
     * @param updateSpeed How fast does the entity update its path to the target entity?
     **/
    public TargetComponent(String target, BigDecimal updateSpeed) {
        this.target = target;
        this.updateSpeed = updateSpeed.floatValue();
    }
    /**
     * An alternate constructor without {@code updateSpeed}, which is set to the default 0.1f.
     * @param target The name of the entity this entity is targeting
     **/
    public TargetComponent(String target) {
        this.target = target;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SINGLE;
    }
}
