// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component.sprite;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;

/**
 * An extention of the {@link SpriteSheetComponent} class that animates the SpriteSheet depending on the entity's movement.
 * Required layout: Positions on the column and animation part on the row.
 * Positions must be in this order: up, down, left, right.
 **/
public class WalkingSheetComponent extends SpriteSheetComponent {
    public float animationSpeed;
    public int animationFrames;

    // Counters
    public float idleCounter = 0;
    public float walkingCounter = 0;

    /**
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Positions on the column and animation part on the row.
     * Positions must be in this order: up, down, left, right.
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public WalkingSheetComponent(String spriteSheetName, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
        animationFrames = sizeX;
        animationSpeed = 0.4f;
    }
    /**
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Positions on the column and animation part on the row.
     * Positions must be in this order: up, down, left, right.
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public WalkingSheetComponent(String spriteSheetName, BigDecimal animationSpeed, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
        animationFrames = sizeX / Main.tileSize;
        this.animationSpeed = animationSpeed.floatValue();
    }
}
