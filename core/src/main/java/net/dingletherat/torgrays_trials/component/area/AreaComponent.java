
// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.area;

import java.math.BigDecimal;

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
        this(height, width, new BigDecimal(width).divide(new BigDecimal(-2)).negate(), new BigDecimal(height).divide(new BigDecimal(-2)).negate());
    }
    public AreaComponent(BigDecimal height, BigDecimal width) {
        this(height, width, width.divide(new BigDecimal(-2)).negate(), height.divide(new BigDecimal(-2)).negate());
    }
}
