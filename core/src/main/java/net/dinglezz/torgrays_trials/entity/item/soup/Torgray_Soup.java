package net.dinglezz.torgrays_trials.entity.item.soup;

import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.item.ItemTags;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.*;
import java.util.Random;

public class Torgray_Soup extends Item {
    public Torgray_Soup(TilePoint tilePoint) {
        super("Torgray's Soup", tilePoint);

        tags.add(ItemTags.TAG_CONSUMABLE);
        icon = registerEntitySprite("entity/item/soup/torgray's_soup");
        currentImage = icon;
        description = "Torgray's wisest soup. \nIt's warm and a bit hearty. \nHealing: +4";
        maxStack = 12;
        price = 2;
    }

    @Override
    public boolean use(Mob mob) {
        Main.game.ui.uiState = States.UIStates.DIALOGUE;
        int random = new Random().nextInt(9) + 1;

        Main.game.ui.setCurrentDialogue(switch (random) {
            case 1 -> "Erm the last two keys have to be bought. \n+4 health";
            case 2 -> "Erm by pressing F3 you can enter debug \nmode. \n+4 health";
            case 3 -> "Erm when it's gloom, Dracores respawn, \nhave their health quadrupled, and do half a \nheart more damage. \n+4 health";
            case 4 -> "Erm it appears you are left handed. \nBut hey, that's just a theory! A GAME THEORY! \n+4 health";
            case 5 -> "Erm after passing all 4 gates you get \nsome cool goodies. \n+4 health";
            case 6 -> "Erm in dark gloom or light gloom, your light \nrange increases or decreases by 50px. \n+4 health";
            case 7 -> "Erm a heart is actually 2 health points, \nhalf a heart is 1. \n+4 health";
            case 8 -> "Erm when you go down from your spawn \npoint, in the area where the Dracores are, \nthere is a path to a hut. \n+4 health";
            case 9 -> "Erm leveling up increases your defence and \nattack values, but so do swords and shields. \n+4 health";
            default -> "Erm something went wrong. \n+4 health";
        });

        Main.game.player.heal(4);
        generateParticles(Main.game.player, Main.game.player);
        Sound.playSFX("Power Up");
        return true;
    }

    // Particles
    public Color getParticleColor() {return new Color(209, 25, 25);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
