
// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.area;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.Component;

/**
 * Creates a custimizable box in the entity.
 * <p>
 * This class is mostly meant to be extended, not used.
 * It's useful for colllision with the {@link CollisionComponent}, or touch detection with the {@link TouchComponent}.
 * The area is always anchored to the center of the entity.
 * <p>
 * - Recommended components: {@link PositionComponent}.
 * - The class is mostly used in {@code AreaChecker}.
 **/
public class AreaComponent implements Component {
    public int height;
    public int width;
    public float offsetX;
    public float offsetY;

    /**
     * @param height What will be the height of the area in tiles?
     * @param width What will be the width of the area in tiles?
     * @param offsetX Horizontal offset of the area relative to the entity's current position in world units.
     * @param offsetY Vertical offset of the area relative to the entity's current position in world units.
     **/
    public AreaComponent(Integer height, Integer width, BigDecimal offsetX, BigDecimal offsetY) {
        this.height = height * Main.tileSize;
        this.width = width * Main.tileSize;
        this.offsetX = offsetX.floatValue();
        this.offsetY = offsetY.floatValue();
    }
    /**
     * @param height What will be the height of the area in world units?
     * @param width What will be the width of the area in world units?
     * @param offsetX Horizontal offset of the area relative to the entity's current position in world units.
     * @param offsetY Vertical offset of the area relative to the entity's current position in world units.
     **/
    public AreaComponent(BigDecimal height, BigDecimal width, BigDecimal offsetX, BigDecimal offsetY) {
        this.height = height.intValue();
        this.width = width.intValue();
        this.offsetX = offsetX.floatValue();
        this.offsetY = offsetY.floatValue();
    }
    /**
     * Alternate constructor with no offsets (automatically set to 0)
     * @param height What will be the height of the area in tiles?
     * @param width What will be the width of the area in tiles?
     **/
    public AreaComponent(Integer height, Integer width) {
        this(height, width, BigDecimal.valueOf(0), BigDecimal.valueOf(0));
    }
    /**
     * Alternate constructor with no offsets (automatically set to 0)
     * @param height What will be the height of the area in world units?
     * @param width What will be the width of the area in world units?
     **/
    public AreaComponent(BigDecimal height, BigDecimal width) {
        this(height, width, BigDecimal.valueOf(0), BigDecimal.valueOf(0));
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI;
    }
}
