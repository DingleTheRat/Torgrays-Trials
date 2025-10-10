package net.dinglezz.torgrays_trials.entity.item;

import java.awt.event.KeyEvent;

import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class World_Map extends Item {
    public World_Map(TilePoint tilePoint) {
        super("World Map", tilePoint);
        icon = registerEntitySprite("entity/item/map");
        currentImage = icon;
        tags.add(ItemTags.TAG_CONSUMABLE);
        description = "A handy map of the world, \nwithout that annoying \ndarkness.";
        price = 8;
    }
    @Override
    public boolean use(Mob mob) {
        Main.game.ui.uiState = States.UIStates.MAP;
        Main.game.inputHandler.keyStates.get(States.GameStates.PLAY).put(KeyEvent.VK_W, false);
        Main.game.inputHandler.keyStates.get(States.GameStates.PLAY).put(KeyEvent.VK_A, false);
        Main.game.inputHandler.keyStates.get(States.GameStates.PLAY).put(KeyEvent.VK_S, false);
        Main.game.inputHandler.keyStates.get(States.GameStates.PLAY).put(KeyEvent.VK_D, false);
        return false;
    }
}
