package net.dinglezz.torgrays_trials.environment;

import net.dinglezz.torgrays_trials.main.AssetSetter;
import net.dinglezz.torgrays_trials.main.DataManager;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.MapHandler;
import org.json.JSONException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Lighting {
    Game game;
    BufferedImage darknessFilter;

    // Main Darkness Stuff
    public States.DarknessStates darknessState = States.DarknessStates.NIGHT;
    public States.DarknessStates nextGloom;
    public int darknessCounter = 0;

    // Darkness Settings
    public int nightLength = 12000;
    public int gloomLength = 10000;
    public int gloomChance = 50;
    public int lightGloomChance = 25;
    public int darkGloomChance = 25;

    public Lighting(Game game) {
        this.game = game;
        setLightSource();
        nextGloom = chooseNextGloom();
    }

    public void setLightSource() {
        // Make buffered image for darkness filter
        darknessFilter = new BufferedImage(game.screenWidth, game.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) darknessFilter.getGraphics();

        // Calculate the center of the player
        int centreX = game.player.screenX + (game.tileSize) / 2;
        int centreY = game.player.screenY + (game.tileSize) / 2;

        // Create a radial gradient paint depending on the darkness state
        Color[] color = new Color[12];
        float[] fraction = new float[12];

        if ((darknessState == States.DarknessStates.NIGHT) ||
                (darknessState == States.DarknessStates.NEW_DUSK && !game.ui.isFadingBack()) ||
                (darknessState == States.DarknessStates.DUSK && game.ui.isFadingBack())) {
            color[0] = new Color(0, 0, 0.1f, 0.1f);
            color[1] = new Color(0, 0, 0.1f, 0.15f);
            color[2] = new Color(0, 0, 0.1f, 0.2f);
            color[3] = new Color(0, 0, 0.1f, 0.3f);
            color[4] = new Color(0, 0, 0.1f, 0.4f);
            color[5] = new Color(0, 0, 0.1f, 0.5f);
            color[6] = new Color(0, 0, 0.1f, 0.6f);
            color[7] = new Color(0, 0, 0.1f, 0.7f);
            color[8] = new Color(0, 0, 0.1f, 0.78f);
            color[9] = new Color(0, 0, 0.1f, 0.84f);
            color[10] = new Color(0, 0, 0.1f, 0.88f);
            color[11] = new Color(0, 0, 0.1f, 0.88f);

            fraction[0] = 0f;
            fraction[1] = 0.4f;
            fraction[2] = 0.5f;
            fraction[3] = 0.6f;
            fraction[4] = 0.65f;
            fraction[5] = 0.7f;
            fraction[6] = 0.75f;
            fraction[7] = 0.8f;
            fraction[8] = 0.85f;
            fraction[9] = 0.9f;
            fraction[10] = 0.95f;
            fraction[11] = 1f;
        }
        if ((darknessState == States.DarknessStates.GLOOM || darknessState == States.DarknessStates.LIGHT_GLOOM || darknessState == States.DarknessStates.DARK_GLOOM) ||
                (darknessState == States.DarknessStates.DUSK && !game.ui.isFadingBack()) ||
                (darknessState == States.DarknessStates.NEW_DUSK && game.ui.isFadingBack())) {
            color[0] = new Color(0, 0, 0.1f, 0.1f);
            color[1] = new Color(0, 0, 0.1f, 0.42f);
            color[2] = new Color(0, 0, 0.1f, 0.52f);
            color[3] = new Color(0, 0, 0.1f, 0.61f);
            color[4] = new Color(0, 0, 0.1f, 0.69f);
            color[5] = new Color(0, 0, 0.1f, 0.76f);
            color[6] = new Color(0, 0, 0.1f, 0.82f);
            color[7] = new Color(0, 0, 0.1f, 0.87f);
            color[8] = new Color(0, 0, 0.1f, 0.91f);
            color[9] = new Color(0, 0, 0.1f, 0.94f);
            color[10] = new Color(0, 0, 0.1f, 0.96f);
            color[11] = new Color(0, 0, 0.1f, 0.99f);

            fraction[0] = 0f;
            fraction[1] = 0.4f;
            fraction[2] = 0.5f;
            fraction[3] = 0.6f;
            fraction[4] = 0.65f;
            fraction[5] = 0.7f;
            fraction[6] = 0.75f;
            fraction[7] = 0.8f;
            fraction[8] = 0.85f;
            fraction[9] = 0.9f;
            fraction[10] = 0.95f;
            fraction[11] = 1f;
        }

        // Set the radius of the darkness filter
        try {
            RadialGradientPaint gradientPaint = new RadialGradientPaint(centreX, centreY, MapHandler.mapFiles.get(game.currentMap).getInt("light radius"), fraction, color);
            graphics2D.setPaint(gradientPaint);
        } catch (JSONException jsonException) {
            if (game.player.currentLight == null) {
                graphics2D.setColor(color[11]);
            } else {
                // Light Adjustments
                int lightRadiusAdjustment = getLightRadiusAdjustment();

                RadialGradientPaint gradientPaint = new RadialGradientPaint(centreX, centreY, game.player.currentLight.lightRadius + lightRadiusAdjustment, fraction, color);
                graphics2D.setPaint(gradientPaint);
            }
        }

        // Fill the buffered image with the radial gradient paint
        graphics2D.fillRect(0, 0, game.screenWidth, game.screenHeight);
        graphics2D.dispose();
    }

    public void update() {
        if (game.environmentManager.lightUpdated) {
            setLightSource();
            game.environmentManager.lightUpdated = false;
        }

        // Darkness state stuff
        switch (darknessState) {
            case NIGHT: updateDarknessState(States.DarknessStates.NEW_DUSK, nightLength, true); break;
            case NEW_DUSK: updateDarknessState(nextGloom, 1, false); break;
            case GLOOM, LIGHT_GLOOM, DARK_GLOOM: updateDarknessState(States.DarknessStates.DUSK, gloomLength, true); break;
            case DUSK: updateDarknessState(States.DarknessStates.NIGHT, 1, false); break;
        }
    }
    public void draw(Graphics2D graphics2D) {
        // Draw a blue effect
        try {
            if (MapHandler.mapFiles.get(game.currentMap).getBoolean("blue effect")) {
                graphics2D.setColor(new Color(0, 0, 0.1f, 0.25f));
                graphics2D.fillRect(0, 0, game.screenWidth, game.screenHeight);
            }
        } catch (JSONException jsonException) {
            graphics2D.setColor(new Color(0, 0, 0.1f, 0.25f));
            graphics2D.fillRect(0, 0, game.screenWidth, game.screenHeight);
        }

        graphics2D.drawImage(darknessFilter, 0, 0, null);
    }

    public void updateDarknessState(States.DarknessStates nextDarknessState, int threshold, boolean transitionWhenDone) {
        if (game.ui.transitioning) return;
        darknessCounter++;
        if (darknessCounter < threshold) return;

        darknessCounter = 0;
        darknessState = nextDarknessState;
        DataManager.autoSaveData();
        if (nextDarknessState == nextGloom) {
            nextGloom = chooseNextGloom();
            AssetSetter.setMonsters(true);
        } else if (nextDarknessState == States.DarknessStates.NIGHT) AssetSetter.setMonsters(false);


        if (transitionWhenDone) {
            game.ui.transitioning = true;

            // Set the transition settings
            float openSpeed = (darknessState == States.DarknessStates.NEW_DUSK) ? 0.005f : 0.01f;
            float closeSpeed = (darknessState == States.DarknessStates.DUSK) ? 0.01f : 0.005f;
            game.ui.setTransitionSettings(new Color(0, 0, 0.1f), openSpeed, closeSpeed);
            game.ui.transitionAction = () -> game.environmentManager.lightUpdated = true;
        }
    }

    public int getLightRadiusAdjustment() {
        int lightRadiusAdjustment = switch (darknessState) {
            case LIGHT_GLOOM -> 50;
            case DARK_GLOOM -> -50;
            default -> 0;
        };
        if (darknessState == States.DarknessStates.NEW_DUSK && game.ui.isFadingBack()) {
            if (nextGloom == States.DarknessStates.LIGHT_GLOOM) lightRadiusAdjustment = 50;
            else if (nextGloom == States.DarknessStates.DARK_GLOOM) lightRadiusAdjustment = -50;
        } else if (darknessState == States.DarknessStates.DUSK && !game.ui.isFadingBack()) {
            if (nextGloom == States.DarknessStates.LIGHT_GLOOM) lightRadiusAdjustment = 50;
            else if (nextGloom == States.DarknessStates.DARK_GLOOM) lightRadiusAdjustment = -50;
        }
        return lightRadiusAdjustment;
    }

    public States.DarknessStates chooseNextGloom() {
        int random = new Random().nextInt(100) + 1;

        if (random <= gloomChance) {
            return States.DarknessStates.GLOOM;
        } else if (random <= gloomChance + lightGloomChance) {
            return States.DarknessStates.LIGHT_GLOOM;
        } else if (random <= gloomChance + lightGloomChance + darkGloomChance) {
            return States.DarknessStates.DARK_GLOOM;
        }

        System.err.println("Warning: No gloom state chosen");
        System.err.println("Number chosen: " + random);
        return States.DarknessStates.GLOOM;
    }
}
