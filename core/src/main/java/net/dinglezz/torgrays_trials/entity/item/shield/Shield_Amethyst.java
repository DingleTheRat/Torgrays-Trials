package net.dinglezz.torgrays_trials.entity.item.shield;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Shield_Amethyst extends Item {
    public Shield_Amethyst(TilePoint tilePoint) {
        super("Amethyst Shield", tilePoint);
        tags.add(ItemTags.TAG_SHIELD);
        icon = registerEntitySprite("entity/item/shield/amethyst_shield");
        currentImage = icon;
        defenceValue = 2;
        description = "A majestic purple shield \nDefence: " + defenceValue;
        price = 6;
    }
}
