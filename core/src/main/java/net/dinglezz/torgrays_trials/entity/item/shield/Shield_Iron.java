package net.dinglezz.torgrays_trials.entity.item.shield;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Shield_Iron extends Item {
    public Shield_Iron(TilePoint tilePoint) {
        super("Iron Shield", tilePoint);
        tags.add(ItemTags.TAG_SHIELD);
        icon = registerEntitySprite("entity/item/shield/iron_shield");
        currentImage = icon;
        defenceValue = 1;
        description = "A shiny iron shield \nDefence: " + defenceValue;
        price = 4;
    }
}
