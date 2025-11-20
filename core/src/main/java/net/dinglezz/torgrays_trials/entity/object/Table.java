package net.dinglezz.torgrays_trials.entity.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Table extends Entity {
    public Table(TilePoint tilePoint) {
        super("Table", tilePoint);
        image = registerEntitySprite("entity/object/table");
        currentImage = image;

        collision = true;
        resizeSolidArea(0, 16, 48, 32 , 2);
    }
}
