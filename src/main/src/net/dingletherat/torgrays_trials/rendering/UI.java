// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

import net.dingletherat.torgrays_trials.main.Translations;
import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.main.States;

import javax.swing.*;

public class UI {
    Font maruMonica;
    Graphics graphics;

    public UI(Game game) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/font/Maru_Monica.ttf");
            assert inputStream != null;
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException exception) {
            Main.handleException(exception);
        }

        titleScreen();

        Main.LOGGER.info("Loaded UI class");
    }

    public void titleScreen() {
        JLabel frame = makeFrame(Component.LEFT_ALIGNMENT, Component.CENTER_ALIGNMENT, 0, 0);
        frame.setText(Translations.translatableText(Main.game.identifier, "new_game"));
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (Main.game.gameState == States.GameStates.PLAY) Main.game.gameState = States.GameStates.TITLE;
                else Main.game.gameState = States.GameStates.PLAY;
            }
        });
        Main.game.add(frame);

        Main.game.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel frame2 = makeFrame(Component.LEFT_ALIGNMENT, Component.CENTER_ALIGNMENT, 0, 0);
        frame2.setText(Translations.translatableText(Main.game.identifier, "load_game"));
        Main.game.add(frame2);
    }

    public void draw(Graphics graphics) {
        this.graphics = graphics;
        graphics.setFont(maruMonica);
        graphics.setColor(Color.white);
    }

    private JLabel makeFrame(float alignmentX, float alignmentY, int width, int height) {
        // Create the frame and make it visible (using a JLabel)
        JLabel frame = new JLabel();
        frame.setOpaque(true);

        // Set it's size
        frame.setSize(width, height);

        /* Set the alignment (the position of the frame).
        The benefit of using this over X and Y coordinates is that the frame always sticks to the alignment, no matter the frame's size
        Also coordinates don't work with layouts, so this is the only option */
        frame.setAlignmentX(alignmentX);
        frame.setAlignmentY(alignmentY);

        // Set the decorations (A white, rounded border and a semi-transparent black background)
        frame.setBackground(new Color(0, 0, 0, 210));
        frame.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 5, true),
                BorderFactory.createEmptyBorder(frame.getWidth(), frame.getHeight(), frame.getWidth(), frame.getHeight())));

        // Change some text properties in case text is added after creation
        frame.setForeground(Color.WHITE);
        frame.setFont(maruMonica.deriveFont(100f));

        // Return the frame if any additional stuff is added afterward
        return frame;
    }

    public int getCentreX(String text) {
        int length = (int) graphics.getFontMetrics().getStringBounds(text, graphics).getWidth();
        return Main.game.screenWidth / 2 - length / 2;
    }
}
