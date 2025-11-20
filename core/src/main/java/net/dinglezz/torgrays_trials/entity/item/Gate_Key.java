package net.dinglezz.torgrays_trials.entity.item;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.main.CollisionChecker;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.tile.TilePoint;


public class Gate_Key extends Item {
    public Gate_Key(TilePoint tilePoint) {
        super("Gate Key", tilePoint);
        name = "Gate Key";
        icon = registerEntitySprite("entity/item/key");
        currentImage = icon;
        tags.add(ItemTags.TAG_CONSUMABLE);
        description = "Matches the keyhole of a gate.";
        maxStack = 4;
        price = 6;
    }

    @Override
    public boolean use(Mob mob) {
        Entity detected = CollisionChecker.getDetected(mob, Main.game.objects.get(Main.game.currentMap), "Gate");
        if (detected != null) {
            Main.game.ui.addMiniNotification("-1 " + name);
            Sound.playSFX("Unlock");
            Main.game.objects.get(Main.game.currentMap).remove(detected);
            return true;
        } else {
            return false;
        }
    }
}
