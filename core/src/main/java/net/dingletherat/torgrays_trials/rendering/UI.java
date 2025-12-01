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

    /**
     * Sets up the UI class, so it functions properly.
     * <p>
     * First, this method loads the {@link Stage} that will display UI elements.
     * The stage is also added as an input processor to process mouse clicks.
     * Secondly, the method calls setup methods for each UI state.
     * These methods initialize their state's elements and add them to the {@code UIStates} hashmap to be displayed.
     **/
    public static void setup() {
        // Create the stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Setup all uiStates
        titleScreen();

        Main.LOGGER.info("Loaded UI");
    }

    /**
     * Generates a specific font with a specific size if one isn't in a cache already.
     * <p>
     * @param fileName The name of the font's file (.ttf). The font is taken from
     * the {@code font} directory inside the assets' directory.
     * <p>
     * @param size The size of the font that will be generated.
     * <p>
     * @return A {@link BitmapFont} which is the generated font (the final product).
     * If the font was already generated, it will be taken from a cache to save resources.
     **/
    public static BitmapFont getFont(String fileName, int size) {
        // If the font has already been created, get it from the cache
        if (fontCache.containsKey(fileName) && fontCache.get(fileName).containsKey(size))
            return fontCache.get(fileName).get(size);

        // If not, we'll have to make it
        // Load in the font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + fileName + ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Set the parameters (only one right now)
        parameter.size = size;

        // Generate the font and dispose of the evidence
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        // Store the font in a fontCache
        fontCache.putIfAbsent(fileName, new HashMap<>());
        fontCache.get(fileName).put(size, font);

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

            // Update the table to the new one (if it exists)
            if (uiStates.containsKey(uiState))
                stage.addActor(uiStates.get(uiState));

            // Finally, update the currentUIstate
            currentUIstate = uiState;
        }
        stage.act();
    }

    /// Sets up the elements for the title screen.
    public static void titleScreen() {
        // Create a new table to hold all the items
        Table table = new Table();

        // Position it on the Y axis (e.g., 200 px from bottom)
        table.setY(350);

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

    /**
     * Creates a {@link TextButton} with effects when you hover over it.
     * <p>
     * The effects are a sound when you hover over the button and also arrows that surround the text on hover.
     * @param text The text that will be on the button. Change it with the {@code setText} function.
     * <p>
     * @param style The style of the button. This might be the background, the font, the color.
     * Use {@link TextButton.TextButtonStyle}.
     * <p>
     * @param action A lambda of the action you want to take. Example of what to put in:
     * {@code () -> Gdx.app.exit()} (Closes the app).
     * <p>
     * @return The resulting button with hover effects.
     **/
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
