// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component;

import java.math.BigDecimal;

public class PositionComponent implements Component {
    public float x;
    public float y;

    public PositionComponent(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public PositionComponent(BigDecimal x, BigDecimal y) {
        this.x = x.floatValue();
        this.y = y.floatValue();
    }
}
