package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.event.KeyEvent;

import org.json.JSONObject;

public class Speak extends Event {
    public Speak(TilePoint tilePoint, JSONObject parameters) {
        super(tilePoint, parameters);
    }

    @Override
    public void onHit() {}

    @Override
    public void whileHit() {
        Main.game.ui.uiState = States.UIStates.INTERACT;

        if (Main.game.inputHandler.uiKeyStates.get(States.UIStates.INTERACT).get(KeyEvent.VK_E)) {
            // Prepare for dialogue
            Main.game.ui.uiState = States.UIStates.DIALOGUE;

            // Set the entity depending on the provided type
            Mob mob = switch (getParameter("type", String.class)) {
                case "npc" -> Main.game.npcs.get(tilePoint.map()).get(getParameter("index", Integer.class));
                case "monster" -> Main.game.monsters.get(tilePoint.map()).get(getParameter("index", Integer.class));
                default -> throw new IllegalStateException("Unexpected value: " + getParameter("type", String.class));
            };

            // Force the entity to speak against its will >:)
            mob.speak(false);
        }
    }

    @Override
    public void onLeave() {
        Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
    }
}
