package net.dingletherat.torgrays_trials.main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class UI {
	Game game;
	Font maruMonica;
	Graphics g;
	
	public ST.UIStates uiState = ST.UIStates.JUST_DEFAULT;
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
		
		Main.LOGGER.info("Loaded UI class.");
	}
	
	public void draw(Graphics g) {
		this.g = g;
		g.setFont(maruMonica);
		g.setColor(Color.white);
		
		if (uiState.defaultUI) {
			switch (game.gameState) {
				// case TITLE -> drawTitleScreen();
				// case PLAY, PAUSE -> drawBasics();
				// case GAME_END -> drawGameOverScreen();
				// case EXCEPTION -> drawExceptionScreen();
			}
		}
		FontMetrics metrics = g.getFontMetrics(maruMonica);
		g.drawString("Torgray's Trials", (game.screenWidth - metrics.stringWidth("Torgray's Trials")) / 2, 100);
		
		switch (uiState) {
			// case DIALOGUE -> drawDialogueScreen();
			// case INTERACT -> drawInteractScreen();
			// case PAUSE -> drawPauseScreen();
			// case TRADE -> drawTradeScreen();
			// case CHARACTER -> drawCharacterScreen();
			// case MAP -> drawMapScreen();
			// case SAVE -> drawSaveScreen();
		}
		
	}
}
