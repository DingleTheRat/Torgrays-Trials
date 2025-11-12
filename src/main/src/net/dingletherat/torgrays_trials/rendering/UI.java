// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import net.dingletherat.torgrays_trials.main.Translations;
import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.main.Sound;

import javax.swing.*;

public class UI {
    /**
     * The hashMap that holds all UI componenets for each UI state.
     * Create a UI state by simple making an entry into this hashmap with the key being the name of the uiState.
     * All the componenets listed in the ArrayList will enable once the UIState variable is equal to the name.
     **/
    public HashMap<String, ArrayList<Component>> uiStates = new HashMap<>();
    /// Depending on what the string is, it will display the corresponding componenets inside the uiStates HashMap.
    public String uiState = "Title";
    private String currentUIstate;

    Font maruMonica;
    Graphics graphics;

    public UI(Game game) {
        // Load in the font
        try {
            InputStream inputStream = getClass().getResourceAsStream("/font/Maru_Monica.ttf");
            assert inputStream != null;
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException exception) {
            Main.handleException(exception);
        }

        // Load all UI states
        titleScreen();

        Main.LOGGER.info("Loaded UI class");
    }

    public void draw(Graphics graphics) {
        this.graphics = graphics;
        graphics.setFont(maruMonica);
        graphics.setColor(Color.white);
    }

    /**
     * Updates the currently displayed components.
     * Displayed components are obtained from the {@code uiStates} HashMap.
     * If a key of an entry is equal to the {@code uiState} field, then it's displayed
     **/
    public void update() {
        /*
         * If the currentUIstate isn't equal to the uiState, meaning the uiState has changed, update the components.
         * This is done to save preformance, as there is no point to update components after the uiState changed.
         */
        if (currentUIstate != uiState) {
            // Remove each componenet from the old uiState from the game, but only if it exists
            if (uiStates.containsKey(currentUIstate))
                uiStates.get(currentUIstate).forEach(Main.game::remove);

            // Add each componenet from the new uiState to the game, but also only if it exists
            if (uiStates.containsKey(uiState))
                uiStates.get(uiState).forEach(Main.game::add);
            
            // Finally, update the currentUIstate
            currentUIstate = uiState;
        }
    }

    public void titleScreen() {
        // Create componenet pool
        ArrayList<Component> componentPool = new ArrayList<>();

        // Create a blank space to make the following content be on the middle of the Y axis
        componentPool.add(Box.createRigidArea(new Dimension(0, 150)));

        // Create the title label, which is bold and slightly red
        JLabel title = createBasicText(Translations.translatableText(Main.game.identifier, "title"), 0f);
        title.setFont(maruMonica.deriveFont(Font.BOLD, 140f));
        title.setForeground(new Color(209, 25, 25));
        componentPool.add(title);

        // Create a small blank space to seperate the title and buttons
        componentPool.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create the first button that starts the game when pressed
        JLabel newGame = createBasicButton(Translations.translatableText(Main.game.identifier, "new_game"), 80f,
                () -> Main.game.loadGame());
        componentPool.add(newGame);

        // Create a blank space to not make the buttons stick
        componentPool.add(Box.createRigidArea(new Dimension(0, 5)));

        // A load button (no functionallity for now)
        JLabel loadGame = createBasicText(Translations.translatableText(Main.game.identifier, "load_game"), 80f);
        componentPool.add(loadGame);

        // Another blank space for the same resons as the last one
        componentPool.add(Box.createRigidArea(new Dimension(0, 5)));

        // Finally, a quit button, so players can touch grass
        JLabel quit = createBasicButton(Translations.translatableText(Main.game.identifier, "quit"), 80f,
                () -> System.exit(0));
        componentPool.add(quit);

        // Create the uiState
        uiStates.put("Title", componentPool);
    }

    /**
     * Creates a new {@link JLabel} with plain white text.
     * The text is displayed in the Maru Monica font with the size of your choice.
     * Worth noting that the text label is automatically aligned to the middle,
     * if you wanna change that call the setAlignmentX or Y methods, and set your alignment.
     * <p>
     * @param text The text that will be displayed when the label is shown.
     * @param size The size of the text that will be displayed when the label is shown.
     * @return The label that is created. Feel free to modify it further.
     **/
    public JLabel createBasicText(String text, float size) {
        JLabel label = new JLabel(text);

        // Set the font to Maru Monica, and it's size. As well as make the text color white by making the foreground white.
        label.setFont(maruMonica.deriveFont(size));
        label.setForeground(Color.WHITE);

        /* Set the alignment (the position of the frame).
        The benefit of using this over X and Y coordinates is that the frame always sticks to the alignment, no matter the frame's size
        Also coordinates don't work with layouts, so this is the only option */
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);

        return label;
    }

    /**
     * Creates a button with hover effects.
     * Using the {@code createBasicText} method, this method creates a basic text label.
     * The implement's main point is that it attaches a {@link MouseListener} to the label to create a hover effect.
     * The hover effect is just two arrows surrounding the text, and a sound.
     * <p>
     * @param text The text that will be displayed when the label is shown.
     * @param size The size of the text that will be displayed when the label is shown.
     * @param action What happens when the button is clicked?
     * @return The label that is created. Feel free to modify it further.
     **/
    public JLabel createBasicButton(String text, float size, Runnable action) {
        // Create label using createBasicText because it does most of the stuff we need
        JLabel label = createBasicText(text, size);

        // Now for what this method is actually about, hover animations :D
        label.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseEntered(MouseEvent event) {
               // If the mouse entered, let the player know they can press it by putting arrows around it and playing a sound
               label.setText("> " + text + " <");
               Sound.playSFX("Cursor");
           }

           @Override
           public void mouseExited(MouseEvent event) {
               // Once the cursor leaves, undo the last method's work
               label.setText(text);
           }

           @Override
           public void mouseClicked(MouseEvent event) {
               // Just run the lambda for clicking
               action.run();
           }
        });

        return label;
    }
}
