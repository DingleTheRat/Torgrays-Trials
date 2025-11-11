// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import net.dingletherat.torgrays_trials.entity.Entity;
import net.dingletherat.torgrays_trials.entity.Player;
import net.dingletherat.torgrays_trials.rendering.Darkness;
import net.dingletherat.torgrays_trials.rendering.MapHandler;
import net.dingletherat.torgrays_trials.rendering.TileManager;
import net.dingletherat.torgrays_trials.rendering.UI;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel {
    // Screen settings
    final int originalTileSize = 16; // 16x16 tile
    public int scale = 4;

    public int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public int screenHeight = tileSize * maxScreenRow; // 576 pixels

    public Thread gameThread;
    public double FPS = 0;

    // Classes
    public UI ui;
    public InputHandler inputHandler;
    public Darkness darkness;
    public Random random = new Random();

    // Entities
    public Player player;
    public ArrayList<Entity> entities = new ArrayList<>();

    // States
    public States.GameStates gameState = States.GameStates.PLAY;
    public States.UIStates uiState = States.UIStates.NONE;

    // Game settings
    public String difficulty;
    public String language = "english"; // English is the default
    public String identifier = "vanilla";

    public String currentMap = "Main Island";

    public void setup() {
        Main.LOGGER.info("--Setting up game--");
        gameThread = new Thread(this::gameLoop);

        // Extra window settings
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.BLACK);
        setFocusable(true); // So inputs work

        // Add a keyboard listener
        addKeyListener(inputHandler = new InputHandler());
        TileManager.loadTiles();

        // Load JSON files
        Translations.loadFiles();
        MapHandler.loadMaps();

        // Change the layout to Box, then load in UI
        setLayout(new BoxLayout(Main.game, BoxLayout.Y_AXIS));
        ui = new UI(this);
        darkness = new Darkness();

        // Load in Torgray :D
        player = new Player();
        entities.add(player);
        darkness.addLightSource(player);
        
        // Light test fireflies
        for (int i = 0; i < 100; i++) {
            Entity entity = new Entity("wassup", Main.game.tileSize * Main.game.random.nextInt(50),
                    Main.game.tileSize * Main.game.random.nextInt(50)){
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

    public void gameLoop() {
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

        while (Main.running) {
            long start = System.nanoTime();

            update();
            repaint();

            long elapsed = System.nanoTime() - start;
            long sleepTime = OPTIMAL_TIME - elapsed;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(Duration.ofNanos(sleepTime - 750_000));
                    elapsed = System.nanoTime() - start;
                    DecimalFormat df = new DecimalFormat("0.00");
                    FPS = Double.parseDouble(df.format(1_000_000_000.0 / elapsed).replace(",", "."));
                } catch (InterruptedException e) {
                    Main.handleException(e);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (gameState == States.GameStates.PLAY) {
            TileManager.draw(graphics); // TEMPORARY! will relace this with better code later

            // Draw every entity inside the entities hashmap
            entities.forEach(entity -> entity.draw(graphics));
            
            darkness.draw(graphics);
        }

        if (gameState == States.GameStates.TITLE) ui.draw(graphics);
    }

    private void update() {
        // Update every entity inside the entities hashmap
        entities.forEach(Entity::update);
    }

    public void adjustDifficulty() {
    }
}
