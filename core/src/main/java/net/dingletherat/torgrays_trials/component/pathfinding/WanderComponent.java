// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.pathfinding;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.system.PathfindingSystem;

/**
 * Activated by the {@link PathfindingComponent}, the entity will Wander around randomly.
 * <p>
 * The entity will pick a direction to wander in every few ticks (chosen by {@code decisionSpeed}).
 * The entity has a chance to go diagonally, go straight, or change its state to idle.
 * Sometimes, the entity might also not change states at all, even when it's updated again.
 * <p>
 * - Required components: {@link PathfindingComponent}
 * - Recomended components:
 * - Implemented by {@link PathfindingSystem}
 **/
public class WanderComponent implements Component {
    public float decisionSpeed;
    public float counter = 0;

    public WanderComponent(BigDecimal decisionSpeed) {
        this.decisionSpeed = decisionSpeed.floatValue();
    }
}
