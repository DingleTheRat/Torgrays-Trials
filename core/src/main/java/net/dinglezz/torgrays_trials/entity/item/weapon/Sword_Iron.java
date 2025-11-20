package net.dinglezz.torgrays_trials.entity.item.weapon;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Sword_Iron extends Item {
    public Sword_Iron(TilePoint tilePoint) {
        super("Iron Sword", tilePoint);

        tags.add(ItemTags.TAG_WEAPON);
        icon = registerEntitySprite("entity/item/weapon/iron_sword");
        currentImage = icon;
        attackValue = 1;
        knockBackPower = 1;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "A shiny iron sword \nAttack: " + attackValue + "\nKnockback: " + knockBackPower;
        price = 4;
    }
}
