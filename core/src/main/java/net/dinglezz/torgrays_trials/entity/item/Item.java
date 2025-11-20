package net.dinglezz.torgrays_trials.entity.item;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Image;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.entity.Player;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Item extends Entity implements Serializable {
    // Icon
    public Image icon;

    // Collision
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);

    // Attributes
    public String description = "Nothing Here :(";
    public int maxStack = 12;
    public int amount = 1;
    public int price = 0;

    // Tags Properties
    public ArrayList<ItemTags> tags = new ArrayList<>();
    public int attackValue;
    public int knockBackPower = 0;
    public int defenceValue;
    public int lightRadius;

    public Item(String name, TilePoint tilePoint) {
        super(name, tilePoint);

        // Disable collision by default and set default image
        collision = false;
    }

    @Override
    public <T extends Entity> void onHit(T entity) {
        if (entity instanceof Player player) {
            // Play a sound effect
            Sound.playSFX("Coin");

            // Add it to the player's inventory and remove it from the map
            player.giveItem(this);
            Main.game.items.get(Main.game.currentMap).remove(this);

            // Add a mini notification
            Main.game.ui.addMiniNotification("+" + amount + " " + name);
        }
    }

    public boolean use(Mob mob) {return false;}
}
