// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;

public class PositionComponent implements Component {
    public float x;
    public float y;

    public PositionComponent(Integer col, Integer row) {
        this.x = col * Main.tileSize;
        this.y = row * Main.tileSize;
    }

    public PositionComponent(BigDecimal x, BigDecimal y) {
        this.x = x.floatValue();
        this.y = y.floatValue();
    }
}
