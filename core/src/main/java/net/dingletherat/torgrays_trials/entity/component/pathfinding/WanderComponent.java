// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component.pathfinding;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.entity.component.Component;

public class WanderComponent implements Component {
    public float decisionSpeed;
    public float counter = 0;

    public WanderComponent(BigDecimal decisionSpeed) {
        this.decisionSpeed = decisionSpeed.floatValue();
    }
}
