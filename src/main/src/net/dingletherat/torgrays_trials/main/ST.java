package net.dingletherat.torgrays_trials.main;

public class ST {
	public enum GameStates {
		TITLE,
		PLAY,
		PAUSE,
		GAME_END,
		EXCEPTION,
	}
	public enum UIStates {
		NONE(false, false), // No UI
		JUST_DEFAULT(true, true), // Only default HUD
		DIALOGUE(true, true), // Dialogue box
		INTERACT(true, true), // Interacting with an interactable object
		PAUSE(true, true), // Pause menu
		CHARACTER(false, false),
		TRADE(false, false), // Trading menu
		MAP(false, false), // Map screen
		SAVE(false, false); // Save menu
		
		final boolean defaultKeyboardInput;
		final boolean defaultUI;
		
		UIStates(boolean defaultKeyboardInput, boolean defaultUI) {
			this.defaultKeyboardInput = defaultKeyboardInput;
			this.defaultUI = defaultUI;
		}
	}
	public enum DarknessStates {
		NIGHT,
		NEW_DUSK,
		GLOOM,
		LIGHT_GLOOM,
		DARK_GLOOM,
		DUSK
	}
	public enum ExceptionStates {
		NOTHING,
		ONLY_IGNORABLE,
		IGNORABLE_QUITABLE,
		ONLY_QUITABLE,
		INSTANT_QUIT,
	}
}