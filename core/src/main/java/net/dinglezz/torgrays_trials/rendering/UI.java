// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dinglezz.torgrays_trials.rendering;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.main.Sound;
import net.dingletherat.torgrays_trials.main.Translations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UI {
    /**
     * The hashMap that holds all UI components for each UI state.
     * Create a UI state by simple making an entry into this hashmap with the key being the name of the uiState.
     * All the components listed in the ArrayList will be enabled once the UIState variable is equal to the name.
     **/
    public HashMap<String, ArrayList<Component>> uiStates = new HashMap<>();
    /// Depending on what the string is, it will display the corresponding components inside the uiStates HashMap.
    public String uiState = "Title";
    private String currentUIstate;

    Font maruMonica;
    Graphics graphics;

    public UI() {
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
         * This is done to save performance, as there is no point in updating components after the uiState changed.
         */
        if (!Objects.equals(currentUIstate, uiState)) {
            // Remove each component from the old uiState from the game, but only if it exists
            if (uiStates.containsKey(currentUIstate))
                uiStates.get(currentUIstate).forEach(Main.game::remove);

            // Add each component from the new uiState to the game, but also only if it exists
            if (uiStates.containsKey(uiState))
                uiStates.get(uiState).forEach(Main.game::add);

            // Finally, update the currentUIstate
            currentUIstate = uiState;
        }
    }

    public void titleScreen() {
        // Create a component pool
        ArrayList<Component> componentPool = new ArrayList<>();

        // Create a blank space to make the following content be in the middle of the Y axis
        componentPool.add(Box.createRigidArea(new Dimension(0, 150)));

        // Create the title label, which is bold and slightly red
        JLabel title = createBasicText(Translations.translatableText(Main.game.identifier, "title"), 0f);
        title.setFont(maruMonica.deriveFont(Font.BOLD, 140f));
        title.setForeground(new Color(209, 25, 25));
        componentPool.add(title);

        // Create a small blank space to separate the title and buttons
        componentPool.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create the first button that starts the game when pressed
        JLabel newGame = createBasicButton(Translations.translatableText(Main.game.identifier, "new_game"), 80f,
                () -> Main.game.loadGame());
        componentPool.add(newGame);

        // Create a blank space to not make the buttons stick
        componentPool.add(Box.createRigidArea(new Dimension(0, 5)));

        // A load button (no functionality for now)
        JLabel loadGame = createBasicText(Translations.translatableText(Main.game.identifier, "load_game"), 80f);
        componentPool.add(loadGame);

        // Another blank space for the same reasons as the last one
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
     * if you want to change that, call the setAlignmentX or Y methods and set your alignment.
     * <p>
     * @param text The text that will be displayed when the label is shown.
     * @param size The size of the text that will be displayed when the label is shown.
     * @return The label that is created. Feel free to modify it further.
     **/
    public JLabel createBasicText(String text, float size) {
        JLabel label = new JLabel(text);

        // Set the font to Maru Monica, and it's size. As well as making the text color white by making the foreground white.
        label.setFont(maruMonica.deriveFont(size));
        label.setForeground(Color.WHITE);

        /*
        Set the alignment (the position of the frame).
        The benefit of using this over X and Y coordinates is that the frame always sticks to the alignment, no matter the frame's size
        Also coordinates don't work with layouts, so this is the only option
         */
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
        Font font = maruMonica.deriveFont(size);
        return new HoverButton(text, font, action);
    }
}

/**
 * JLabel that draws left/right arrow glyphs when hovered without changing the text,
 * avoiding any relayout / 1px shift caused by swapping the label text.
 */
class HoverButton extends JLabel {
    private boolean hovered = false;
	private final String leftArrow = ">";
    private final String rightArrow = "<";

    public HoverButton(String text, Font font, Runnable action) {
        super(text);
        setFont(font);
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(Component.CENTER_ALIGNMENT);

        FontMetrics fm = getFontMetrics(getFont());
        int textWidth = fm.stringWidth(text);
        int arrowWidth = Math.max(fm.stringWidth(leftArrow), fm.stringWidth(rightArrow));
	    int horizontalPadding = 20;
	    int width = textWidth + arrowWidth * 2 + horizontalPadding * 2;
        int height = fm.getHeight() + 6;

        Dimension fixedSize = new Dimension(width, height);
        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
                Sound.playSFX("Cursor");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fm = g.getFontMetrics(getFont());
        int textWidth = fm.stringWidth(getText());
        int centerX = getWidth() / 2;
        int textStartX = centerX - textWidth / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

        if (hovered) {
            g.setColor(getForeground());

            // Place arrows relative to the text edges with arrowSpacing
            int leftX = textStartX - 20 - fm.stringWidth(leftArrow);
            g.drawString(leftArrow, leftX, y);

            int rightX = textStartX + textWidth + 20;
            g.drawString(rightArrow, rightX, y);
        }
    }
}
