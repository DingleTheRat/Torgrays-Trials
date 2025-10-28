// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import net.dingletherat.torgrays_trials.json.Translations;
import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.main.States;

public class UI {
	Game game;
	Font maruMonica;
	Graphics g;
	private String currentDialogue = "";
	public int commandNumber = 0;  // Which menu option is selected?
	
	public States.UIStates uiState = States.UIStates.JUST_DEFAULT;
	public String subUIState = "Main Title";
	
	public UI(Game game) {
		this.game = game;
		
		try {
			InputStream inputStream = getClass().getResourceAsStream("/font/Maru_Monica.ttf");
			assert inputStream != null;
			maruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
		} catch (FontFormatException | IOException e) {
			Main.handleException(e);
		}
		
		Main.LOGGER.info("Loaded UI class");
	}
	
	public void draw(Graphics g) {
		this.g = g;
		g.setFont(maruMonica);
		g.setColor(Color.white);
        if (game.gameState == States.GameStates.TITLE) {drawTitleScreen();}
	}
	
	private void drawTitleScreen() {
		if (Objects.equals(subUIState, "Main Title")) {
			// Title Text
			g.setFont(g.getFont().deriveFont(Font.BOLD, 90f));
			String text = Translations.translatableText(game.identifier, "title");
			int x = getCentreX(text);
			int y = game.tileSize * 3;
			
			// Title Shadow
			g.setColor(new Color(147, 37, 37));
			g.drawString(text, x + 4, y + 4);
			
			// Main Title
			g.setColor(new Color(209, 25, 25));
			g.drawString(text, x, y);
			
			// Torgray Image
			x = game.screenWidth / 2 - (game.tileSize * 2) / 2;
			y += game.tileSize + (game.tileSize / 2);
			g.drawImage(Images.loadImage("entity/player/walking/torgray_down_1"),
					x, y, game.tileSize * 2, game.tileSize * 2, null);
			
			// Menu Options
			g.setColor(Color.white);
			
			g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));
			text = Translations.translatableText(game.identifier, "new_game");
			x = getCentreX(text);
			y += (game.tileSize * 3) + (game.tileSize / 2);
			g.drawString(text, x, y);
			if (commandNumber == 0) {
				g.drawString(">", x - game.tileSize,  y);
			}
			
			g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));
			text = Translations.translatableText(game.identifier, "load_game");
			x = getCentreX(text);
			y += game.tileSize;
			g.drawString(text, x, y);
			if (commandNumber == 1) {
				g.drawString(">", x - game.tileSize,  y);
			}
			
			g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));
			text = Translations.translatableText(game.identifier, "quit");
			x = getCentreX(text);
			y += game.tileSize;
			g.drawString(text, x, y);
			if (commandNumber == 2) {
				g.drawString(">", x - game.tileSize,  y);
			}
		} else if (Objects.equals(subUIState, "Difficulties")) {
			g.setColor(Color.white);
			g.setFont(g.getFont().deriveFont(Font.BOLD, 42f));
			
			String text = "Select a Difficulty";
			int x = getCentreX(text);
			int y = game.tileSize * 3;
			g.drawString(text, x, y);
			
			g.setFont(g.getFont().deriveFont(Font.PLAIN, 42f));
			
			text = "Easy";
			x = getCentreX(text);
			y += game.tileSize * 3;
			g.drawString(text, x, y);
			if (commandNumber == 0) {
				g.drawString(">", x - game.tileSize, y);
			}
			
			text = "Medium";
			x = getCentreX(text);
			y += game.tileSize;
			g.drawString(text, x, y);
			if (commandNumber == 1) {
				g.drawString(">", x - game.tileSize, y);
			}
			
			text = "Hard";
			x = getCentreX(text);
			y += game.tileSize;
			g.drawString(text, x, y);
			if (commandNumber == 2) {
				g.drawString(">", x - game.tileSize, y);
			}
			
			text = "Back";
			y += game.tileSize * 2;
			g.drawString(text, x, y);
			if (commandNumber == 3) {
				g.drawString(">", x - game.tileSize, y);
			}
		}
	}
		
	public int getCentreX(String text) {
		int length = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
		return game.screenWidth / 2 - length / 2;
	}
}
