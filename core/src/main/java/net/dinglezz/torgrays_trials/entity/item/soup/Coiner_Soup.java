package net.dinglezz.torgrays_trials.entity.item.soup;

import net.dinglezz.torgrays_trials.effect.Cuteness;
import net.dinglezz.torgrays_trials.effect.GenerousCoins;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.*;
import java.util.Random;

public class Coiner_Soup extends Item {
    public Coiner_Soup(TilePoint tilePoint) {
        super("Coiner's Soup", tilePoint);

        tags.add(ItemTags.TAG_CONSUMABLE);
        icon = registerEntitySprite("entity/item/soup/coiner's_soup");
        currentImage = icon;
        description = "The cutest soup in the world, even \nmonsters become softer near it. \nDuration: 30s";
        maxStack = 12;
        price = 2;
    }

    @Override
    public boolean use(Mob mob) {
        // Effects
        Main.game.player.addEffect(new Cuteness(30, Main.game.player));
        // (60% chance to apply GenerousCoins effect cuz it's too OP)
        int random = new Random().nextInt(100) + 1;
        if (random <= 60) Main.game.player.addEffect(new GenerousCoins(15, Main.game.player));

        generateParticles(this, Main.game.player);
        Sound.playSFX("Power Up");
        return true;
    }

    // Particles
    public Color getParticleColor() {return new Color(209, 169, 25);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
