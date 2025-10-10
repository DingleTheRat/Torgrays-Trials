package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.main.DataManager;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.event.KeyEvent;

import org.json.JSONObject;

public class Healing_Pond extends Event {
    public Healing_Pond(TilePoint tilePoint, JSONObject parameters) {
        super(tilePoint, parameters);
    }

    @Override
    public void onHit() {}

    @Override
    public void whileHit() {
        Main.game.ui.uiState = States.UIStates.INTERACT;

        if (Main.game.inputHandler.keyStates.get(States.GameStates.PLAY).get(KeyEvent.VK_E)) {
            Main.game.ui.uiState = States.UIStates.DIALOGUE;
            Main.game.player.cancelAttack();
            if (Main.game.saveSlot != 0) {
                Main.game.ui.setCurrentDialogue("*Drinks water* \nI feel.. safe, almost like the world took a \nsnapshot of me");
                Main.game.ui.uiState = States.UIStates.DIALOGUE;
                DataManager.saveData(Main.game.saveSlot);
            } else {
                Main.game.ui.uiState = States.UIStates.SAVE;
                Main.game.ui.commandNumber = 0;
            }
        }
    }

    @Override
    public void onLeave() {
        Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
    }
}
