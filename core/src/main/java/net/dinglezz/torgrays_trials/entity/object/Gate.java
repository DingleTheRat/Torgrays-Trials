package net.dinglezz.torgrays_trials.entity.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Gate extends Entity {
    public Gate(TilePoint tilePoint) {
        super("Gate", tilePoint);
        image = registerEntitySprite("entity/object/gate");
        currentImage = image;

        collision = true;
        resizeSolidArea(0, 16, 48, 32, 2);
    }
}
