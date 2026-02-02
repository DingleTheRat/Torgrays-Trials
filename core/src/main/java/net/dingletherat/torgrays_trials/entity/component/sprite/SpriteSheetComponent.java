// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component.sprite;

import java.math.BigDecimal;

public class SpriteSheetComponent extends SpriteComponent {
	/// This is the colum where the sprite would be pulled from. Set to -1 to disable it.
	public int column;
	/// This is the row where the sprite would be pulled from. Set to -1 to disable it.
	public int row;

    /**
     * @param spriteSheetName The name or path of the sheet in the files.
     * @param column This is the colum where the sprite would be pulled from. Set to -1 to disable it.
     * @param row This is the row where the sprite would be pulled from. Set to -1 to disable it.
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteSheetComponent(String spriteSheetName, BigDecimal column, BigDecimal row, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);

        this.column = column.intValue();
        this.row = row.intValue();
    }
    /**
     * @param spriteSheetName The name or path of the sheet in the files.
     * @param column This is the colum where the sprite would be pulled from. Set to -1 to disable it.
     * @param row This is the row where the sprite would be pulled from. Set to -1 to disable it.
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteSheetComponent(String spriteSheetName, Integer column, Integer row, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);

        this.column = column.intValue();
        this.row = row.intValue();
    }

    /**
     * @param spriteSheetName The name or path of the sheet in the files.
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteSheetComponent(String spriteSheetName, Integer sizeX, Integer sizeY, Integer z) {
        this(spriteSheetName, 0, 0, sizeX, sizeY, z);
    }

    /**
     * @param spriteSheetName The name or path of the sheet in the files.
     * @param sizeX What's the size of the sprite sheet on the X?
     * @param sizeY What's the size of the sprite sheet on the Y?
     **/
    public SpriteSheetComponent(String spriteSheetName, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        this(spriteSheetName, new BigDecimal(0), new BigDecimal(0), sizeX, sizeY, z);
    }
}
