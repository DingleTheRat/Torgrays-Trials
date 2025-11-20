package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

public class Pit extends Event {
    public Pit(TilePoint tilePoint, JSONObject parameters) {
        super(tilePoint, parameters);
    }

    @Override
    public void onHit() {
        // Torgray being annoyed dialogue
        Main.game.ui.uiState = States.UIStates.DIALOGUE;
        Main.game.ui.setCurrentDialogue("Dang it, I feel into a pit!");

        // Take some health >:)
        Main.game.player.damage(1);
    }

    @Override
    public void whileHit() {}

    @Override
    public void onLeave() {}
}
