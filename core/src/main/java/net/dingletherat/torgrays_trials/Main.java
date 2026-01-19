// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import net.dingletherat.torgrays_trials.entity.Entity;
import net.dingletherat.torgrays_trials.entity.EntityHandler;
import net.dingletherat.torgrays_trials.entity.component.SpriteSheetComponent;
import net.dingletherat.torgrays_trials.entity.custom.Firefly;
import net.dingletherat.torgrays_trials.main.World;
import net.dingletherat.torgrays_trials.main.Sounds;
import net.dingletherat.torgrays_trials.main.States;
import net.dingletherat.torgrays_trials.main.Translations;
import net.dingletherat.torgrays_trials.rendering.Darkness;
import net.dingletherat.torgrays_trials.rendering.DataImage;
import net.dingletherat.torgrays_trials.rendering.UI;

import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    // Tile Settings
    final static int originalTileSize = 16; // 16x16 tile
    public static int scale = 4;
    public static int tileSize = originalTileSize * scale; // 48x48 tile

    // Screen Settings
    public static final int screenColumns = 20;
    public static final int screenRows = 12;
    public static int screenWidth = tileSize * screenColumns; // 960 pixels
    public static int screenHeight = tileSize * screenRows; // 576 pixels

    // General Settings
    public static String identifier = "vanilla";
    public static String language = "english";

    // Essential, but miscellaneous
    public static World world;
    public static SpriteBatch batch;
    public static ShapeRenderer shapes;
    public static Random random = new Random();
    public static States.GameStates gameState = States.GameStates.TITLE;
    public static final Logger LOGGER = LoggerFactory.getLogger("Torgray's Trials");

    // Title screen elements
    static DataImage backdrop;
    public static Darkness titleDarkness;
    public static ArrayList<Entity> titleFireflies = new ArrayList<>();
    public static long titleMusic;

    @Override
    public void create() {
        LOGGER.info("Program started");
        LOGGER.info("--Setting up game--");

        // Setup exception handler to handle any future exception caused by this method or something else
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            Main.handleException(new Exception(exception));
        });

        // Create rendering stuff
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();

        // Load JSON files
        Translations.loadFiles();
        EntityHandler.registerAllComponents();
        EntityHandler.generateTemplates();

        // Setup UI
        UI.setup();

        // Load sound library and play title music
        Sounds.loadLibrary();
        titleMusic = Sounds.playMusic("Tech Geek");

        // Add 100 fireflies to the title darkness (which will also be created) :D
        titleDarkness = new Darkness();
        for (int i = 0; i < 50; i++) {
            // Create a new fireflie at a random position and add it to the titleFireflies ArrayList to be updated
            Entity firefly = new Firefly(random.nextFloat() * screenWidth - (screenWidth / 2),
                    random.nextFloat() * screenHeight - (screenHeight / 2));
            titleFireflies.add(firefly);

            // Add the firefly as a light source so it's visible
            titleDarkness.addLightSource(firefly);
        }

        // Lastly, load the backdrop
        backdrop = DataImage.loadImage("backdrop");

        LOGGER.info("--Setup complete!--");
        LOGGER.info("Window enabled");
    }

    public void draw() {
        // Clear the screen from the last drawing
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        switch (gameState) {
            case TITLE -> {
                // Draw the background
                batch.begin();
                batch.draw(backdrop.getTexture(), 0, 0);
                batch.end();

                // Draw the darkness
                titleDarkness.draw();

                // Draw UI
                UI.stage.draw();
            }
            case PLAY -> world.draw();
        }
    }
    public void update() {
        switch (gameState) {
            case TITLE -> {
                // Update UI and fireflies
                UI.update();
                titleFireflies.stream().forEach(Entity::update);
            }
            case PLAY -> world.update();
        }
    }

    @Override
    public void render() {
        update();
        draw();
    }

    @Override
    public void dispose() {
        LOGGER.info("Ending program...");
        batch.dispose();
        shapes.dispose();
        Sounds.dispose();
        LOGGER.info("Program ended");
    }

    public static void handleException(Exception exception) {
        LOGGER.error("Torgray's Trials has encountered an error", exception);
        Gdx.app.exit();
    }
}
