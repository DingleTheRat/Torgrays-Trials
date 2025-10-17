package net.dinglezz.torgrays_trials.main;

public class States {
    public enum GameStates {
        TITLE,
        PLAY,
        PAUSE,
        DEATH,
        EXCEPTION,
    }
    public enum UIStates {
        NONE("", false),
        JUST_DEFAULT("play", true),
        DIALOGUE("dialogue", true),
        INTERACT("", true),
        PAUSE("", true),
        CHARACTER("character", false),
        TRADE("trade", false),
        MAP("map", false),
        SAVE("", false);

        final String inputGroup;
        final boolean defaultUI;
        UIStates(String inputGroup, boolean defaultUI) {
            this.inputGroup = inputGroup;
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
