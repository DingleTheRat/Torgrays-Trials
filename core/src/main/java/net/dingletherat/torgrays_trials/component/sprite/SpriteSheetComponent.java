// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.sprite;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.system.SpriteSystem;

/**
 * Draws a tile-sized portion of a sprite.
 * <p>
 * For now at least, the portion will be only the size of a tile, meaning each frame of the sheet must be the size of a tile (16x16).
 * You can choose which part of the sprite-sheet the final sprite is drawn from by setting the row and column variables.
 * <p>
 * - Recommended components: {@link PositionComponent}
 * - Implemented by {@link SpriteSystem}
 **/
public class SpriteSheetComponent extends SpriteComponent {
	/// This is the colum where the sprite would be pulled from. Set to -1 to disable it.
	public int column;
	/// This is the row where the sprite would be pulled from. Set to -1 to disable it.
	public int row;

    /**
     * @param spriteSheetName The name or path of the sheet in the files.
     * @param column This is the colum where the sprite would be pulled from.
     * @param row This is the row where the sprite would be pulled from.
     * @param sizeX What's the size of the sprite sheet on the X in world units?
     * @param sizeY What's the size of the sprite sheet on the Y in world units?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteSheetComponent(String spriteSheetName, Integer column, Integer row, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);

        this.column = column;
        this.row = row;
    }
    /**
     * @param spriteSheetName The name or path of the sheet in the files.
     * @param column This is the colum where the sprite would be pulled from.
     * @param row This is the row where the sprite would be pulled from.
     * @param sizeX What's the size of the sprite sheet on the X in tiles?
     * @param sizeY What's the size of the sprite sheet on the Y in tiles?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteSheetComponent(String spriteSheetName, Integer column, Integer row, Integer sizeX, Integer sizeY, Integer z) {
        super(spriteSheetName, sizeX, sizeY, z);

        this.column = column.intValue();
        this.row = row.intValue();
    }

    /**
     * An alternate constructor without {@code column} and {@code row}, which are set automatically to 0.
     * @param spriteSheetName The name or path of the sheet in the files.
     * @param sizeX What's the size of the sprite sheet on the X in tiles?
     * @param sizeY What's the size of the sprite sheet on the Y in tiles?
     * @param z The sprite's draw priority over other sprites
     **/
    public SpriteSheetComponent(String spriteSheetName, Integer sizeX, Integer sizeY, Integer z) {
        this(spriteSheetName, 0, 0, sizeX, sizeY, z);
    }

    /**
     * An alternate constructor without {@code column} and {@code row}, which are set automatically to 0.
     * @param spriteSheetName The name or path of the sheet in the files.
     * @param sizeX What's the size of the sprite sheet on the X in world units?
     * @param sizeY What's the size of the sprite sheet on the Y in world units?
     **/
    public SpriteSheetComponent(String spriteSheetName, BigDecimal sizeX, BigDecimal sizeY, Integer z) {
        this(spriteSheetName, 0, 0, sizeX, sizeY, z);
    }
}
