// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.sprite;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.system.SpriteSystem;

/**
 * An extention of the {@link SpriteSheetComponent} class that add in eyes to the entity.
 * <p>
 * The eyes will blink, look around as an idle animation, and even look in the direction that the entity is walking in.
 * It's super cool :D
 * <p>
 * Unfortunatly, right now the sprite sheet must be arranged in this specific order
 * - Required layout: Two rows: Normal on the top, blinking at the bottom. Expresions are on the column
 * - Expresions must be in this order: Neutral, Happy, Sad, Mad
 * <p>
 * - Recommended components: {@link PositionComponent}
 * - Implemented by {@link SpriteSystem}
 **/
public class EyesSheetComponent extends SpriteSheetComponent {
    /// How fast do the eyes preform misc animations such as look around?
    public float animationSpeed = 1.25f;
    /// How fast will the entity blink?
    public float blinkSpeed = 0.1f;
    /// Is the entity in the state of blinking?
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
     * @param animationSpeed How fast do the eyes preform misc animations such as look around?
     * @param blinkSpeed How fast will the entity blink?
     * @param sizeX What's the size of the sprite sheet on the X in tiles?
     * @param sizeY What's the size of the sprite sheet on the Y in tiles?
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
     * @param animationSpeed How fast do the eyes preform misc animations such as look around?
     * @param sizeX What's the size of the sprite sheet on the X in world units?
     * @param sizeY What's the size of the sprite sheet on the Y in world units?
     * @param z The sprite's draw priority over other sprites
     **/
    public EyesSheetComponent(String spriteSheetName, BigDecimal animationSpeed, BigDecimal blinkSpeed, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
        this.animationSpeed = animationSpeed.floatValue();
        this.blinkSpeed = blinkSpeed.floatValue();
    }

    /**
     * An alternate constructor without {@code animationSpeed} and {@code blinkSpeed}, which are set to 1.25f and 0.1f.
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Two rows: Normal on the top, blinking at the bottom. Expresions are on the column
     * Expresions must be in this order: Neutral, Happy, Sad, Mad
     * @param sizeX What's the size of the sprite sheet on the X in tiles?
     * @param sizeY What's the size of the sprite sheet on the Y in tiles?
     * @param z The sprite's draw priority over other sprites
     **/
    public EyesSheetComponent(String spriteSheetName, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
    }
    /**
     * An alternate constructor without {@code animationSpeed} and {@code blinkSpeed}, which are set to 1.25f and 0.1f.
     * @param spriteSheetName The name of the sheet in the files used for animations for the entity.
     * <p>
     * Required layout: Two rows: Normal on the top, blinking at the bottom. Expresions are on the column
     * Expresions must be in this order: Neutral, Happy, Sad, Mad
     * @param sizeX What's the size of the sprite sheet on the X in world units?
     * @param sizeY What's the size of the sprite sheet on the Y in world units?
     * @param z The sprite's draw priority over other sprites
     **/
    public EyesSheetComponent(String spriteSheetName, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);
    }
}
