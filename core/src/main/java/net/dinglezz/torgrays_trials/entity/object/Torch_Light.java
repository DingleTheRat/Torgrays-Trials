package net.dinglezz.torgrays_trials.entity.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Torch_Light extends Entity {
    public Torch_Light(TilePoint tilePoint) {
        super("Torch Light", tilePoint);
        image = registerEntitySprite("entity/object/torch_light/on_wall");
        currentImage = image;
    }
}
