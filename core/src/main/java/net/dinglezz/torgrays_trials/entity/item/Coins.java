package net.dinglezz.torgrays_trials.entity.item;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Player;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Coins extends Item {
    public Coins(TilePoint tilePoint, int amount) {
        super("Coins", tilePoint);
        this.amount = amount;
        setDefaultValues();
    }
    public Coins(TilePoint tilePoint) {
        super("Coins", tilePoint);
        setDefaultValues();
    }
    private void setDefaultValues() {
        maxStack = 2;
        tags.add(ItemTags.TAG_PICKUP_ONLY);
        tags.add(ItemTags.TAG_NON_SELLABLE);
        icon = registerEntitySprite("entity/item/coin");
        currentImage = icon;
        description = "Precious pieces of gold";
        price = 1;

        // Increase the amount of coins if the "Generous Coins" effect is active
        if (Main.game.player != null && Main.game.player.hasEffect("Generous Coins")) amount = amount * 2;
    }

    @Override
    public <T extends Entity> void onHit(T entity) {
        if (entity instanceof Player player) {
            setDefaultValues();

            player.coins += amount;
            Sound.playSFX("Coin");

            // Grammar check
            if (amount == 1) Main.game.ui.addMiniNotification("+" + amount + " Coin");
            else Main.game.ui.addMiniNotification("+" + amount + " Coins");

            // Remove it
            Main.game.items.get(Main.game.currentMap).remove(this);
        }
    }
}
