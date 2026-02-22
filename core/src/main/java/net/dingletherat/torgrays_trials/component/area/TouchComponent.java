// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.area;

import java.math.BigDecimal;

import org.json.JSONArray;

import net.dingletherat.torgrays_trials.system.TouchSystem;

/**
 * Removes or adds components to the entites upon intersection with any other {@link AreaComponent}
 * <p>
 * To modify an entity's components, you have to provide a {@link JSONArray} of arguments, similar to the way you declare components in a template.
 * However, each component also has to have an {@code action} boolean that determines if a component is removed or added (true = add, false = remove).
 * <p>
 * - Recommended components: {@link PositionComponent}.
 * - More info in the implementation of this component in {@link TouchSystem}.
 **/
public class TouchComponent extends AreaComponent {
    /// A {@link JSONArray} of how you wanna modify the current entity's components upon touch (more info in class documentation)
    public JSONArray entity1Action;
    /// A {@link JSONArray} of how you wanna modify the other entity's (the entity thecurrent one toched) components upon touch (more info in class documentation)
    public JSONArray entity2Action;

    /**
     * @param height What will be the height of the area in tiles?
     * @param width What will be the width of the area in tiles?
     * @param offsetX Horizontal offset of the area relative to the entity's current position in world units.
     * @param offsetY Vertical offset of the area relative to the entity's current position in world units.
     * @param entity1Action A {@link JSONArray} of how you wanna modify the current entity's components upon touch (more info in class documentation)
     * @param entity2Action A {@link JSONArray} of how you wanna modify the other entity's (the entity thecurrent one toched) components upon touch (more info in class documentation)
     **/
    public TouchComponent(Integer height, Integer width, BigDecimal offsetX, BigDecimal offsetY, JSONArray entity1Action, JSONArray entity2Action) {
        super(height, width, offsetX, offsetY);
        this.entity1Action = entity1Action;
        this.entity2Action = entity2Action;
    }
    /**
     * @param height What will be the height of the area in world units?
     * @param width What will be the width of the area in world units?
     * @param offsetX Horizontal offset of the area relative to the entity's current position in world units.
     * @param offsetY Vertical offset of the area relative to the entity's current position in world units.
     * @param entity1Action A {@link JSONArray} of how you wanna modify the current entity's components upon touch (more info in class documentation)
     * @param entity2Action A {@link JSONArray} of how you wanna modify the other entity's (the entity thecurrent one toched) components upon touch (more info in class documentation)
     **/
    public TouchComponent(BigDecimal height, BigDecimal width, BigDecimal offsetX, BigDecimal offsetY, JSONArray entity1Action, JSONArray entity2Action) {
        super(height, width, offsetX, offsetY);
        this.entity1Action = entity1Action;
        this.entity2Action = entity2Action;
    }
    /**
     * Alternate constructor with no offsets (automatically set to 0)
     * @param height What will be the height of the area in tiles?
     * @param width What will be the width of the area in tiles?
     * @param entity1Action A {@link JSONArray} of how you wanna modify the current entity's components upon touch (more info in class documentation)
     * @param entity2Action A {@link JSONArray} of how you wanna modify the other entity's (the entity thecurrent one toched) components upon touch (more info in class documentation)
     **/
    public TouchComponent(Integer height, Integer width, JSONArray entity1Action, JSONArray entity2Action) {
        super(height, width);
        this.entity1Action = entity1Action;
        this.entity2Action = entity2Action;
    }
    /**
     * Alternate constructor with no offsets (automatically set to 0)
     * @param height What will be the height of the area in world units?
     * @param width What will be the width of the area in world units?
     * @param entity1Action A {@link JSONArray} of how you wanna modify the current entity's components upon touch (more info in class documentation)
     * @param entity2Action A {@link JSONArray} of how you wanna modify the other entity's (the entity thecurrent one toched) components upon touch (more info in class documentation)
     **/
    public TouchComponent(BigDecimal height, BigDecimal width, JSONArray entity1Action, JSONArray entity2Action) {
        super(height, width);
        this.entity1Action = entity1Action;
        this.entity2Action = entity2Action;
    }
}
