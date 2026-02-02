// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component.sprite;

import java.math.BigDecimal;

/**
 * An extention of the {@link SpriteSheetComponent} class that animates the SpriteSheet depending on the entity's movement.
 * Required layout: Two rows: Normal on the top, blinking at the bottom. Expresions are on the column
 * Expresions must be in this order: Neutral, Happy, Sad, Mad
 **/
public class EyesSheetComponent extends SpriteSheetComponent {
    public float animationSpeed = 1.25f;
    public float blinkSpeed = 0.1f;
    public boolean blinking;

    // Counters
    public float blinkCounter = 0;
    public float idleCounter = 0;
    public float walkingCounter = 0;

    /**
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Two rows: Normal on the top, blinking at the bottom. Expresions are on the column
     * Expresions must be in this order: Neutral, Happy, Sad, Mad
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public EyesSheetComponent(String spriteSheetName, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
    }
    /**
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Two rows: Normal on the top, blinking at the bottom. Expresions are on the column
     * Expresions must be in this order: Neutral, Happy, Sad, Mad
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public EyesSheetComponent(String spriteSheetName, BigDecimal animationSpeed, BigDecimal blinkSpeed, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
        this.animationSpeed = animationSpeed.floatValue();
        this.blinkSpeed = blinkSpeed.floatValue();
    }
    /**
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Two rows: Normal on the top, blinking at the bottom. Expresions are on the column
     * Expresions must be in this order: Neutral, Happy, Sad, Mad
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public EyesSheetComponent(String spriteSheetName, BigDecimal animationSpeed, BigDecimal blinkSpeed, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
        this.animationSpeed = animationSpeed.floatValue();
        this.blinkSpeed = blinkSpeed.floatValue();
    }
    /**
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Two rows: Normal on the top, blinking at the bottom. Expresions are on the column
     * Expresions must be in this order: Neutral, Happy, Sad, Mad
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public EyesSheetComponent(String spriteSheetName, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
    }
}
