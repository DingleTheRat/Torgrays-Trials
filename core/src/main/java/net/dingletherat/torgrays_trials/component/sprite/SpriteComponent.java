// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.sprite;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.rendering.DataImage;

public class SpriteComponent implements Component {
	public DataImage sprite;
    /// The sprite's draw priority over other sprites
    public int z;

    /**
     * @param spriteName The name of the sprite in the files.
     * @param sizeX How much do you want the image to be scaled on the X?
     * @param sizeY How much do you want the image to be scaled on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteComponent(String spriteName, Integer sizeX, Integer sizeY, Integer z) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(sizeX * Main.tileSize, sizeY * Main.tileSize);
        this.z = z;
    }
    /**
     * @param spriteName The name of the sprite in the files.
     * @param sizeX How much do you want the image to be scaled on the X?
     * @param sizeY How much do you want the image to be scaled on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteComponent(String spriteName, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(sizeX.intValue(), sizeY.intValue());
        this.z = z;
    }
    /**
     * @param spriteName The name of the sprite in the files.
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteComponent(String spriteName, Integer z) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(Main.tileSize, Main.tileSize);
        this.z = z;
    }
}
