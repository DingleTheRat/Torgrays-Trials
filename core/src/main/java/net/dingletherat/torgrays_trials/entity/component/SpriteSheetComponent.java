// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component;

import net.dingletherat.torgrays_trials.rendering.DataImage;

public class SpriteSheetComponent implements Component {
    /** The sheet used for animations for the entity.
     * <p>
     * Required layout: Positions on the column and animation part on the row.
     * Positions must be in this order: up, down, left, right.
     **/
	public DataImage spriteSheet;

	/// This is the colum where the sprite would be pulled from. Set to -1 to disable it.
	public int column;
	/// This is the row where the sprite would be pulled from. Set to -1 to disable it.
	public int row;

    /// What's the size of the sprite sheet on the X?
    public int sizeX;
    /// What's the size of the sprite sheet on the Y?
    public int sizeY;

    /**
     * @param spriteSheet The sheet used for animations for the entity.
     * <p>
      Required layout: Positions on the column and animation part on the row.
      Positions must be in this order: up, down, left, right.
      @param column This is the colum where the sprite would be pulled from. Set to -1 to disable it.
      @param row This is the row where the sprite would be pulled from. Set to -1 to disable it.
      @param sizeX What's the size of the sprite sheet on the X?
      @param sizeY What's the size of the sprite sheet on the Y?
     **/
    public SpriteSheetComponent(String spriteSheetName, Integer column, Integer row, Integer sizeX, Integer sizeY) {
        spriteSheet = DataImage.loadImage(spriteSheetName);
        this.column = column;
        this.row = row;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}
