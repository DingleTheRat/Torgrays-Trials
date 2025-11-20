package net.dinglezz.torgrays_trials.entity.item.weapon;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Pointy_Stick extends Item {
    public Pointy_Stick(TilePoint tilePoint) {
        super("Pointy Stick", tilePoint);

        name = "Stick";
        tags.add(ItemTags.TAG_WEAPON);
        icon = registerEntitySprite("entity/item/weapon/pointy_stick");
        currentImage = icon;
        attackValue = 1;
        knockBackPower = 3;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "Knockback > Damage \nAttack: " + attackValue + "\nKnockback: " + knockBackPower;
        price = 5;
    }
}
