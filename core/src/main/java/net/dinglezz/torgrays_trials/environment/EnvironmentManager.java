package net.dinglezz.torgrays_trials.environment;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;

import java.awt.*;

public class EnvironmentManager {
    Game game;
    public Lighting lighting;
    public boolean lightUpdated = false;

    public EnvironmentManager(Game game) {
        this.game = game;
    }

    public void setup() {
        lighting = new Lighting(game);
    }
    public void update() {
        lighting.update();
    }
    public void draw(Graphics2D g2) {
        if (game.gameState != States.GameStates.DEATH &&
                game.gameState != States.GameStates.EXCEPTION) {
            lighting.draw(g2);
        }
    }

    public String getDarknessStateString() {
        return switch (lighting.darknessState) {
            case NIGHT -> "Night";
            case NEW_DUSK -> "New Dusk";
            case GLOOM -> "Gloom";
            case LIGHT_GLOOM -> "Light Gloom";
            case DARK_GLOOM -> "Dark Gloom";
            case DUSK -> "Dusk";
        };
    }
}
