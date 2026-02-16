
// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.area;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.Component;

public class AreaComponent implements Component {
    public int height;
    public int width;
    public float offsetX;
    public float offsetY;

    public AreaComponent(Integer height, Integer width, BigDecimal offsetX, BigDecimal offsetY) {
        this.height = height * Main.tileSize;
        this.width = width * Main.tileSize;
        this.offsetX = offsetX.floatValue();
        this.offsetY = offsetY.floatValue();
    }
    public AreaComponent(BigDecimal height, BigDecimal width, BigDecimal offsetX, BigDecimal offsetY) {
        this.height = height.intValue();
        this.width = width.intValue();
        this.offsetX = offsetX.floatValue();
        this.offsetY = offsetY.floatValue();
    }
    public AreaComponent(Integer height, Integer width) {
        this(height, width, new BigDecimal(0), new BigDecimal(0));
    }
    public AreaComponent(BigDecimal height, BigDecimal width) {
        this(height, width, new BigDecimal(0), new BigDecimal(0));
    }
}
