// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.rendering.DataImage;

public class SpriteComponent implements Component {
	public DataImage sprite;

    /**
     * @param spriteName The name of the sprite in the files.
     * @param sizeX How much do you want the image to be scaled on the X?
     * @param sizeY How much do you want the image to be scaled on the Y?
     **/
    public SpriteComponent(String spriteName, Integer sizeX, Integer sizeY) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(sizeX * Main.tileSize, sizeY * Main.tileSize);
    }
    /**
     * @param spriteName The name of the sprite in the files.
     * @param sizeX How much do you want the image to be scaled on the X?
     * @param sizeY How much do you want the image to be scaled on the Y?
     **/
    public SpriteComponent(String spriteName, BigDecimal sizeX, BigDecimal sizeY) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(sizeX.intValue(), sizeY.intValue());
    }
    /**
     * @param spriteName The name of the sprite in the files.
     **/
    public SpriteComponent(String spriteName) {
        sprite = DataImage.loadImage(spriteName);
        sprite.scaleImage(Main.tileSize, Main.tileSize);
    }
}
