package net.dinglezz.torgrays_trials.entity.item.weapon;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Sword_Amethyst extends Item {
    public Sword_Amethyst(TilePoint tilePoint) {
        super("Amethyst Sword", tilePoint);

        name = "Amethyst Sword";
        tags.add(ItemTags.TAG_WEAPON);
        icon = registerEntitySprite("entity/item/weapon/amethyst_sword");
        currentImage = icon;
        attackValue = 2;
        knockBackPower = 2;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "A majestic purple sword \nAttack: " + attackValue + "\nKnockback: " + knockBackPower;
        price = 6;
    }
}
