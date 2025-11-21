// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.main.Translations;

public class UI {
    static BitmapFont maruMonica;

    // UI
    public static int uiSelected = 0;
    public static int uiMaxOptions = 2;

    public static void init() {
        // Load in the font
        maruMonica = getFont("Maru_Monica");

        Main.LOGGER.info("Loaded UI class");
    }

    public static BitmapFont getFont(String name) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + name + ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 128; // font size in pixels
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    /// Updates the UI elements via keyboard input
    public static void update() {
        // Input canceling so that you can't press and hold
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            uiSelected--;
            if (uiSelected < 0) uiSelected = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            uiSelected++;
            if (uiSelected > uiMaxOptions) uiSelected = uiMaxOptions;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (uiSelected) {
                case 0 -> Main.game.loadGame();
                case 1 -> {}
                case 2 -> System.exit(0);
            }
        }
    }

    public static void drawTitle() {
        Main.batch.begin();
        maruMonica.setColor(0.234375f, 0.12109375f, 0.75390625f, 1f);
        maruMonica.getData().setScale(0.8f);
        drawCenteredString(maruMonica, Translations.get(Main.game.identifier, "title"), Game.screenWidth / 2, 500);

        maruMonica.getData().setScale(0.5f);
        maruMonica.setColor(1f, 1f, 1f, 1f);
        drawButton("new_game", 300, 0);
        drawButton("load_game", 220, 1);
        drawButton("quit", 140, 2);
        Main.batch.end();
    }

    public static void drawButton(String textName, int y, int i) {
        if (i == uiSelected) {
            drawCenteredString(maruMonica, "> " + Translations.get(Main.game.identifier, textName) + " <",
                Game.screenWidth / 2, y);
        } else {
            drawCenteredString(maruMonica, Translations.get(Main.game.identifier, textName), Game.screenWidth / 2, y);
        }
    }

    public static void drawCenteredString(BitmapFont font, String text, int x1, int y1) {
        // Create a layout to measure the text
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, text);

        // Calculate center position
        float x = x1 - (layout.width) / 2;
        float y = y1 + (layout.height) / 2; // for vertical centering

        font.draw(Main.batch, layout, x, y);
    }

    public static void dispose() {
        maruMonica.dispose();
    }
}
