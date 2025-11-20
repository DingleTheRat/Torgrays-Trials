package net.dinglezz.torgrays_trials.entity.item.weapon;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Stick extends Item {
    public Stick(TilePoint tilePoint) {
        super("Stick", tilePoint);
        tags.add(ItemTags.TAG_WEAPON);
        icon = registerEntitySprite("entity/item/weapon/stick");
        currentImage = icon;
        knockBackPower = 5;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "Knockback > Damage \nAttack: " + attackValue + "\nKnockback: " + knockBackPower;
        price = 3;
    }
}
