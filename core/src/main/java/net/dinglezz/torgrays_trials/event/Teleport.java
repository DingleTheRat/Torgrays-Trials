package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.main.AssetSetter;
import net.dinglezz.torgrays_trials.main.DataManager;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

import java.awt.*;
import java.util.Objects;

public class Teleport extends Event {
    public static String nextMap;
    public static int nextCol;
    public static int nextRow;
    public static String nextDirection;

    public Teleport(TilePoint tilePoint, JSONObject parameters) {
        super(tilePoint, parameters);
    }

    @Override
    public void onHit() {
        if (Objects.equals(getParameter("required direction", String.class), Main.game.player.direction)) {
            // Play the sound (important)
            Sound.playSFX("Teleport");

            // Set all the necessary values
            nextMap = getParameter("map", String.class);
            nextDirection = getParameter("direction", String.class);
            if (hasParameter("col") && hasParameter("row")) {
                // Then set the col and row to the provided values
                nextCol = getParameter("col", Integer.class) * Main.game.tileSize;
                nextRow = getParameter("row", Integer.class) * Main.game.tileSize;
            } else {
                // Otherwise, set the col and row to the lowest point so it's set to the spawn point
                nextCol = Integer.MIN_VALUE;
                nextRow = Integer.MIN_VALUE;
            }

            // Set the transition settings
            Main.game.ui.transitioning = true;
            Main.game.ui.setTransitionSettings(Color.BLACK, 0.02f, 0.02f);

            Main.game.ui.transitionAction = () -> {
                Main.game.currentMap = Teleport.nextMap;

                // Set player position
                if (Teleport.nextCol == Integer.MIN_VALUE || Teleport.nextRow == Integer.MIN_VALUE) {
                    Main.game.player.setDefaultPosition();
                } else {
                    Main.game.player.worldX = Teleport.nextCol;
                    Main.game.player.worldY = Teleport.nextRow;
                }
                Main.game.player.direction = Teleport.nextDirection;
                Main.game.environmentManager.lightUpdated = true;

                // Save game (if a slot is selected)
                DataManager.autoSaveData();

                // Play map music
                Sound.playMapMusic();

                // Load entities and events if not already loaded
                AssetSetter.loadAssets();
            };
        }
    }

    @Override
    public void whileHit() {}

    @Override
    public void onLeave() {}
}
