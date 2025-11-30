// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import com.badlogic.gdx.math.Matrix4;
import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.entity.Entity;
import net.dingletherat.torgrays_trials.entity.Player;
import net.dingletherat.torgrays_trials.main.States.GameStates;
import net.dingletherat.torgrays_trials.rendering.Darkness;
import net.dingletherat.torgrays_trials.rendering.MapHandler;
import net.dingletherat.torgrays_trials.rendering.TileManager;
import net.dingletherat.torgrays_trials.rendering.UI;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    // Screen settings
    final static int originalTileSize = 16; // 16x16 tile
    public static int scale = 4;

    public static int tileSize = originalTileSize * scale; // 48x48 tile
    public static final int maxScreenCol = 20;
    public static final int maxScreenRow = 12;
    public static int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public static int screenHeight = tileSize * maxScreenRow; // 576 pixels
    public static String identifier = "vanilla";

    // Classes
    public Darkness darkness;
    public Random random = new Random();

    // Entities
    public Player player;
    public ArrayList<Entity> entities = new ArrayList<>();

    // State
    public States.GameStates gameState = States.GameStates.TITLE;

    // Game settings
    public String difficulty;
    public String language = "english"; // English is the default

    public String currentMap = "Main Island";

    public void setup() {
        Main.LOGGER.info("--Setting up game--");

        // Add a keyboard listener
        TileManager.loadTiles();

        // Load JSON files
        Translations.loadFiles();
        MapHandler.loadMaps();

        // Initialize UI
        UI.setup();
        darkness = new Darkness();

        // Load in Torgray :D
        player = new Player();

        // Light test fireflies
        for (int i = 0; i < 100; i++) {
            Entity entity = new Entity("wassup", Game.tileSize * Main.game.random.nextInt(50),
                Game.tileSize * Main.game.random.nextInt(50)){
                float vX, vY = 0;
                public void update() {
                    if (Main.game.random.nextFloat() > 0.5) properties.put("light_intensity", 0.3f * ((Main.game.random.nextFloat() - 0.5f) / 5f + 1));
                    if (Main.game.random.nextFloat() > 0.9) {
                        vX += (Main.game.random.nextFloat() - 0.5f) * 2;
                        vY += (Main.game.random.nextFloat() - 0.5f) * 2;
                        if (vX > 2) vX = 2;
                        if (vX < -2) vX = -2;
                        if (vY > 2) vY = 2;
                        if (vY < -2) vY = -2;
                    }
                    x += vX;
                    y += vY;
                }
            };
            entity.collision = false;
            entity.properties.put("light_radius", (float) Main.game.random.nextInt(15) + 1);
            entity.properties.put("light_intensity", 0.3f);
            entities.add(entity);
            darkness.addLightSource(entity);
        }

        // Load sound library and play music
        Sound.loadLibrary();
        Sound.playMusic("Tech Geek");

        Main.LOGGER.info("--Completed setup for game--");
    }

    public void draw() {
        Matrix4 original = new Matrix4(Main.batch.getProjectionMatrix());    // Flips the y-axis
        Main.batch.getProjectionMatrix().setToOrtho(0, Game.screenWidth, Game.screenHeight, 0, 0, 1);
        if (gameState == States.GameStates.TITLE) {
            TileManager.draw(); // TEMPORARY! will relace this with better code later
            darkness.draw();
            entities.forEach(Entity::draw);
        } else if (gameState == States.GameStates.PLAY) {
            TileManager.draw(); // TEMPORARY! will relace this with better code later

            // Draw every entity inside the entities hashmap and the player
            Main.batch.begin();
            player.draw();
            entities.forEach(Entity::draw);
            Main.batch.end();

            darkness.draw();
        }

        Main.batch.setProjectionMatrix(original);    // Unflips the y-axis so UI is drawn correctly
        UI.stage.draw();
    }

    public void update() {
        // Update UI
        UI.update();

        if (gameState == States.GameStates.TITLE) {
            entities.forEach(Entity::update);
        } else if (gameState == States.GameStates.PLAY) {
            // Update every entity inside the entities hashmap and the player
            player.update();
            entities.forEach(Entity::update);
        }
    }

    public void loadGame() {
        // Set the state to play, so mobs and stuff could be updated and drawn. As well as the uiState for the, well, UI
        gameState = GameStates.PLAY;

        // TODO: Whenever there is an inventory system, make this only work with items
        darkness.addLightSource(player);

        // Change the music to the "playing music"
        Sound.stopMusic();
        Sound.playMusic("Umbral Force");
    }
}
