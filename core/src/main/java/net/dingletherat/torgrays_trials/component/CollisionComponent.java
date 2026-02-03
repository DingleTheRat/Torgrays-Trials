// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;

public class CollisionComponent implements Component {
    public int height;
    public int width;

    public CollisionComponent(Integer height, Integer width) {
        this.height = height * Main.tileSize;
        this.width = width * Main.tileSize;
    }
    public CollisionComponent(BigDecimal height, BigDecimal width) {
        this.height = height.intValue();
        this.width = width.intValue();
    }
}
