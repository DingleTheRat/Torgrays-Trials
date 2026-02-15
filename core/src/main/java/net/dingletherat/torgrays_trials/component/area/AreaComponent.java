
// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.area;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.Component;

public class AreaComponent implements Component {
    public int height;
    public int width;

    public AreaComponent(Integer height, Integer width) {
        this.height = height * Main.tileSize;
        this.width = width * Main.tileSize;
    }
    public AreaComponent(BigDecimal height, BigDecimal width) {
        this.height = height.intValue();
        this.width = width.intValue();
    }
}
