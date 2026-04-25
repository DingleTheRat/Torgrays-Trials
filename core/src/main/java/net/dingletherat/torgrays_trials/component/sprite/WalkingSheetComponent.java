// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.sprite;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.system.SpriteSystem;

/**
 * An extention of the {@link SpriteSheetComponent} class that animates the SpriteSheet depending on the entity's movement.
 * <p>
 * For instance, if an entity is walking up, the sprite will switch to the walking up portion of the sheet.
 * <p>
 * For now, there's a required layout for every WalkingSheet:
 * - Required layout: Positions on the column and animation part on the row.
 * - Positions must be in this order: up, down, left, right.
 * <p>
 * - Recommended components: {@link PositionComponent}
 * - Implemented by {@link SpriteSystem}
 **/
public class WalkingSheetComponent extends SpriteSheetComponent {
    /// How fast will the entity preform animations such as walking?
    public float animationSpeed;
    /// How many animation frames are the in the spriteSheet? (Set automatically)
    public int animationFrames;

    // Counters
    public float idleCounter = 0;
    public float walkingCounter = 0;

    /**
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Positions on the column and animation part on the row.
     * Positions must be in this order: up, down, left, right.
     * @param animationSpeed How fast will the entity preform animations such as walking?
     * @param sizeX What's the size of the sprite sheet on the X in world units?
     * @param sizeY What's the size of the sprite sheet on the Y in world units?
     * @param z The sprite's draw priority over other sprites
     **/
    public WalkingSheetComponent(String spriteSheetName, BigDecimal animationSpeed, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
        animationFrames = sizeX / Main.tileSize;
        this.animationSpeed = animationSpeed.floatValue();
    }
    /**
     * An alternate constructor without {@code animationSpeed}, which will be set to 0.4f instead.
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Positions on the column and animation part on the row.
     * Positions must be in this order: up, down, left, right.
     * @param sizeX What's the size of the sprite sheet on the X in tiles?
     * @param sizeY What's the size of the sprite sheet on the Y in tiles?
     * @param z The sprite's draw priority over other sprites
     **/
    public WalkingSheetComponent(String spriteSheetName, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
        animationFrames = sizeX;
        animationSpeed = 0.4f;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI;
    }
}
