// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

public class States {
	public enum GameStates {
		TITLE,
		PLAY,
		PAUSE,
		GAME_END,
		EXCEPTION,
	}
    public enum MobStates {
	IDLE,
	WALKING
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
