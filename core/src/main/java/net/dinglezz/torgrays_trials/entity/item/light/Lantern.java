package net.dinglezz.torgrays_trials.entity.item.light;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Lantern extends Item {
    public Lantern(TilePoint tilePoint) {
        super("Lantern", tilePoint);
        tags.add(ItemTags.TAG_LIGHT);
        tags.add(ItemTags.TAG_NON_SELLABLE);
        lightRadius = 150;
        icon = registerEntitySprite("entity/item/light/lantern");
        currentImage = icon;
        description = "SHINE BRIGHTTTTTT TONIGHTTTTTT /lyr \nLight Radius: " + lightRadius;
    }
}
