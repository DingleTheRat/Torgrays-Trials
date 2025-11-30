// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.main.Sound;
import net.dingletherat.torgrays_trials.main.Translations;

public class UI {
    private final static HashMap<String, HashMap<Integer, BitmapFont>> fontCache = new HashMap<>();
    /**
     * The hashMap that holds all UI components for each UI state.
     * Create a UI state by simple making an entry into this hashmap with the key being the name of the uiState.
     * All the components listed in the ArrayList will be enabled once the UIState variable is equal to the name.
     **/
    public static final HashMap<String, Table> uiStates = new HashMap<>();
    /// Depending on what the string is, it will display the corresponding components inside the uiStates HashMap.
    public static String uiState = "Title";
    private static String currentUIstate = "";
    public static Stage stage;

    public static void setup() {
        // Create the stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Setup all uiStates
        titleScreen();

        Main.LOGGER.info("Loaded UI class");
    }

    public static BitmapFont getFont(String name, int size) {
        // If the font has already been created, get it from the cache
        if (fontCache.containsKey(name) && fontCache.get(name).containsKey(size))
            return fontCache.get(name).get(size);

        // If not, we'll have to make it
        // Load in the font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + name + ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Set the parameters (only one right now)
        parameter.size = size;

        // Generate the font and dispose of the evidence
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        // Store the font in a fontCache
        fontCache.putIfAbsent(name, new HashMap<>());
        fontCache.get(name).put(size, font);

        return font;
    }

    /**
     * Updates the currently displayed components.
     * Displayed components are obtained from the {@code uiStates} HashMap.
     * If a key of an entry is equal to the {@code uiState} field, then it's displayed
     **/
    public static void update() {
        /*
         * If the currentUIstate isn't equal to the uiState, meaning the uiState has changed, so update the active table.
         * This is done to save performance, as there is no point in updating table after the uiState changed.
         */
        if (!currentUIstate.equals(uiState)) {
            // Clear the stage to prepare for the next table
            stage.clear();

            // Update the table to the new one
            stage.addActor(uiStates.get(uiState));

            // Finally, update the currentUIstate
            currentUIstate = uiState;
        }
        stage.act();
    }

    public static void titleScreen() {
        // Create a new table to hold all the items
        Table table = new Table();

        // Position it on the Y axis (e.g., 200 px from bottom)
        table.setY(300);

        // Center the table
        table.setPosition((stage.getWidth() - table.getPrefWidth()) / 2f, table.getY());

        // Create the style that will be used for the title
        TextButton.TextButtonStyle titleStyle = new TextButton.TextButtonStyle();
        titleStyle.fontColor = Color.valueOf("#932525");
        titleStyle.font = getFont("Maru_Monica", 140);

        // Create the style that will be used for the buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.font = getFont("Maru_Monica", 80);

        // Create the title
        TextButton title = new TextButton(Translations.get(Game.identifier, "title"), titleStyle);
        table.add(title);

        // New Row
        table.row().pad(5);

        // Create the first actual button that creates a new game
        TextButton newGame = createButton(Translations.get(Game.identifier, "new_game"),
            buttonStyle, () -> Main.game.loadGame());
        table.add(newGame);

        // New Row
        table.row().pad(5);

        // A load button (no functionality for now)
        TextButton loadGame = new TextButton(Translations.get(Game.identifier, "load_game"), buttonStyle);
        table.add(loadGame);

        // New Row
        table.row().pad(5);

        // Finally, a quit button, so players can touch grass
        TextButton quit = createButton(Translations.get(Game.identifier, "quit"),
            buttonStyle, () -> Gdx.app.exit());
        table.add(quit);

        // Create the uiState
        uiStates.put("Title", table);
    }
    public static TextButton createButton(String text, TextButton.TextButtonStyle style, Runnable action) {
        // Create the button
        TextButton button = new TextButton(text, style);

        // Add in a click listener for epik hover effects
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Just run the lambda for clicking
                action.run();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // If the mouse entered, let the player know they can press it by putting arrows around it and playing a sound
                Sound.playSFX("Cursor");
                button.setText("> " + text + " <");
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                // Once the cursor leaves, undo the last method's work
                button.setText(text);
            }
        });

        return button;
    }
}
