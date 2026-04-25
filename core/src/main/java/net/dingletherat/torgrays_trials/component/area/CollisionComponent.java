// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.area;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.component.MovementComponent;
import net.dingletherat.torgrays_trials.main.AreaChecker;
import net.dingletherat.torgrays_trials.system.MovementSystem;

/**
 * An extention of {@code AreaComponent}, this class makes it so you can't pass through collidable tiles or entites.
 * <p>
 * If the area of this component is intersecting with another CollisionComponent's or is with a tile's hitbox, the entity won't be able to move with the {@link MovementComponent}.
 * <p>
 * - Recommended components: {@link PositionComponent}.
 * - Intersections are checked with the {@link AreaChecker} and the non-moving stuff in {@link MovementSystem}.
 **/
public class CollisionComponent extends AreaComponent {
    /**
     * @param height What will be the height of the area in tiles?
     * @param width What will be the width of the area in tiles?
     * @param offsetX Horizontal offset of the area relative to the entity's current position in world units.
     * @param offsetY Vertical offset of the area relative to the entity's current position in world units.
     **/
    public CollisionComponent(Integer height, Integer width, BigDecimal offsetX, BigDecimal offsetY) {
        super(height, width, offsetX, offsetY);
    }
    /**
     * @param height What will be the height of the area in world units?
     * @param width What will be the width of the area in world units?
     * @param offsetX Horizontal offset of the area relative to the entity's current position in world units.
     * @param offsetY Vertical offset of the area relative to the entity's current position in world units.
     **/
    public CollisionComponent(BigDecimal height, BigDecimal width, BigDecimal offsetX, BigDecimal offsetY) {
        super(height, width, offsetX, offsetY);
    }
    /**
     * Alternate constructor with no offsets (automatically set to 0)
     * @param height What will be the height of the area in tiles?
     * @param width What will be the width of the area in tiles?
     **/
    public CollisionComponent(Integer height, Integer width) {
        super(height, width);
    }
    /**
     * Alternate constructor with no offsets (automatically set to 0)
     * @param height What will be the height of the area in world units?
     * @param width What will be the width of the area in world units?
     **/
    public CollisionComponent(BigDecimal height, BigDecimal width) {
        super(height, width);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI;
    }
}
