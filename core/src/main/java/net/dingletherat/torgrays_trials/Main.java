// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import net.dingletherat.torgrays_trials.main.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public static Game game;
    public static SpriteBatch batch;
    public static final Logger LOGGER = LoggerFactory.getLogger("Torgray's Trials");
    public static ShapeRenderer shapes;

    @Override
    public void create() {
        LOGGER.info("Program started");
        batch = new SpriteBatch();
        shapes = new ShapeRenderer();

        game = new Game();
        game.setup();

        LOGGER.info("Window enabled");
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        game.update();
        game.draw();
    }

    @Override
    public void dispose() {
        LOGGER.info("Ending program...");
        batch.dispose();
        shapes.dispose();
        //UI.dispose();
        LOGGER.info("Program ended");
    }

    public static void handleException(Exception exception) {
        LOGGER.error("Torgray's Trials has encountered an error!");
        LOGGER.error("This is likely not your fault, please create an issue on GitHub with the error below.");
        LOGGER.error("---------------------------------------------------------------------------------------", exception);
        Gdx.app.exit();
    }
}
