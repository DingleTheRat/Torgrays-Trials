package net.dingletherat.torgrays_trials.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Objects;

public class InputHandler implements KeyListener {
	public static HashMap<Integer, Boolean> keyMap = new HashMap<>();
	public int maxCommandNumber = 0;
	// Debug
	public boolean debug = false;
	
	public void cancelInputs() {
		keyMap.replaceAll((_, _) -> false);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		keyMap.put(code, true);
		
		if (Main.game.ui.uiState.defaultKeyboardInput) {
			switch (Main.game.gameState) {
				// case TITLE -> titleState(code);
				// case PLAY -> playState(code);
				// case GAME_END -> gameOverState(code);
				// case EXCEPTION -> exceptionState(code);
			}
		}
		
		switch (Main.game.ui.uiState) {
			// case DIALOGUE -> dialogueState(code);
			// case PAUSE -> pauseState(code);
			// case TRADE -> tradeState(code);
			// case CHARACTER -> characterState(code);
			// case MAP -> mapState(code);
			// case SAVE -> saveState(code);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		keyMap.put(e.getKeyCode(), false);
	}
}
