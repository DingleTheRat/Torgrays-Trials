package net.dingletherat.torgrays_trials.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Objects;

public class InputHandler implements KeyListener {
	private final Game game;
	public HashMap<Integer, Boolean> keyMap = new HashMap<>(){{
		// Movement
		put(KeyEvent.VK_W, false);
		put(KeyEvent.VK_A, false);
		put(KeyEvent.VK_S, false);
		put(KeyEvent.VK_D, false);
		put(KeyEvent.VK_UP, false);
		put(KeyEvent.VK_LEFT, false);
		put(KeyEvent.VK_DOWN, false);
		put(KeyEvent.VK_RIGHT, false);
		// Actions
		put(KeyEvent.VK_ENTER, false);
		put(KeyEvent.VK_SPACE, false);
		put(KeyEvent.VK_ESCAPE, false);
	}};
	public int maxCommandNumber = 0;
	// Debug
	public boolean debug = false;
	
	public InputHandler(Game game) {
		this.game = game;
		Main.LOGGER.info("Loaded InputHandler class");
	}
	
	public void cancelInputs() {
		keyMap.replaceAll((_, _) -> false);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println(e.getKeyChar());
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		keyMap.put(code, true);
		
		if (game.ui.uiState.defaultKeyboardInput) {
			switch (game.gameState) {
				case TITLE -> titleState(code);
				// case PLAY -> playState(code);
				// case GAME_END -> gameOverState(code);
				// case EXCEPTION -> exceptionState(code);
			}
		}
		
		switch (game.ui.uiState) {
			// case DIALOGUE -> dialogueState(code);
			// case PAUSE -> pauseState(code);
			// case TRADE -> tradeState(code);
			// case CHARACTER -> characterState(code);
			// case MAP -> mapState(code);
			// case SAVE -> saveState(code);
		}
	}
	
	private void titleState(int code) {
		if (Objects.equals(game.ui.subUIState, "Main Title")) {
			if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
				game.ui.commandNumber--;
				Sound.playSFX("Cursor");
				if (game.ui.commandNumber < 0) {
					game.ui.commandNumber = 2;
				}
			}
			if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
				game.ui.commandNumber++;
				Sound.playSFX("Cursor");
				if (game.ui.commandNumber > 2) {
					game.ui.commandNumber = 0;
				}
			}
			if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
				if (game.ui.commandNumber == 0) {
					game.ui.subUIState = "Difficulties";
					game.ui.commandNumber = 1;
				} else if (game.ui.commandNumber == 1) {
					game.ui.subUIState = "Saves";
					game.ui.commandNumber = 0;
				} else if (game.ui.commandNumber == 2) {
					System.exit(0);
				}
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keyMap.put(e.getKeyCode(), false);
	}
}
