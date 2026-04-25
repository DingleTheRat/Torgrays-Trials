// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.sprite;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.rendering.DataImage;
import net.dingletherat.torgrays_trials.system.SpriteSystem;

/**
 * Draws a sprite image from a image of your choice at the entity's location.
 * <p>
 * This component just simply draws the sprite from its spriteName/Path in the files.
 * The draw order compared to other entities is usually depending on its Y. However you can change that with the {@link ZComponent}.
 * <p>
 * - Recommended components: {@link PositionComponent}
 * - Implemented by {@link SpriteSystem}
 **/
public class SpriteComponent implements Component {
	public DataImage sprite;
    /// The sprite's draw priority over other sprites in this entity
    public int z;

    /**
     * @param spriteName The name of the sprite in the files.
     * @param sizeX How much do you want the image to be scaled on the X in tiles?
     * @param sizeY How much do you want the image to be scaled on the Y in tiles?
     * @param z The sprite's draw priority over other sprites in this entity
     **/
    public SpriteComponent(String spriteName, Integer sizeX, Integer sizeY, Integer z) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(sizeX * Main.tileSize, sizeY * Main.tileSize);
        this.z = z;
    }
    /**
     * @param spriteName The name of the sprite in the files.
     * @param sizeX How much do you want the image to be scaled on the X in world units?
     * @param sizeY How much do you want the image to be scaled on the Y in world units?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteComponent(String spriteName, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(sizeX.intValue(), sizeY.intValue());
        this.z = z;
    }
    /**
     * An alternate constructor without {@code sizeX} and {@code sizeY} (automatically set to tileSize)
     * @param spriteName The name of the sprite in the files.
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteComponent(String spriteName, Integer z) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(Main.tileSize, Main.tileSize);
        this.z = z;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI;
    }
}
