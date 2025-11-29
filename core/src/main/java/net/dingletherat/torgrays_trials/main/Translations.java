// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import net.dingletherat.torgrays_trials.Main;
import org.json.JSONObject;
import java.util.HashMap;

public class Translations {
    private static final HashMap<String, JSONObject> TRANSLATION_FILES = new HashMap<>();

    /**
     * Gets the translation from the current language's translation files using a {@code translationKey}.
     * The {@code translationKey} is a combination of the {@code identifier + ":" + key}, and is what the program searches for in
     * a language's translation files.
     * @param identifier An identifier of whom the key belongs to exists to keep things more organized.
     * If a key belongs to the vanilla game, it would be "vanilla". If it belongs to a mod, it could be "random_mod".
     * The identifier is appended to a key with ":" in between to make the translation key.
     * <p>
     * @param key This could be anything you want, as long as it's a different thing as something else under the same identifier.
     * This is what's added to the end of the {@code translationKey}, so the format would be identifier + ":" + key (which is this parameter)
     * @return The result of what's found in the translation file with the translation key.
     * If something is not found, it returns the {@code translationKey} for debugging.
     **/
    public static String get(String identifier, String key) {
        // Firstly, get the JSON file of the current language and get translationKey by combining the caller and key strings
        JSONObject languageFile = TRANSLATION_FILES.get(Main.game.language);
        String translationKey = identifier + ":" + key;

        // If language files do not contain a translation for that key: warn, and return the translationKey
        if (languageFile.getString(translationKey) == null) {
	        Main.LOGGER.warn("Couldn't find translation for {} in {}", translationKey, languageFile);
            return translationKey;
        }

        // If it does have the key, simply return the value stored behind that key
        return languageFile.getString(translationKey);
    }

    /**
     * Loads all the translation files for the game and adds them to the {@code TRANSLATION_FILES} HashMap
     * with the fileName as the key, and the contents in {@code JSONObject} form as the value.
     * These files are being loaded from the "values/translations" directory.
     * <p>
     * Note: Non JSON files will not be added to the HashMap
     **/
    public static void loadFiles() {
        // Get the names of all the files in "/values/translations"
        String[] translationFiles = UtilityTool.getFileNames("values/translations");

        // Loop through the translation files and add them to the TRANSLATION_FILES HashMap
        for (String file : translationFiles) {
            /* Get the filePath of the file and use it to convert the contents of the file
            into a getJsonObject to be able to add it to the TRANSLATION_FILES HashMap */
            String filePath = "values/translations/" + file;
            JSONObject jsonObject = UtilityTool.getJsonObject(filePath);
            if (jsonObject != null) TRANSLATION_FILES.put(file.replace(".json", ""), jsonObject);
        }
	    Main.LOGGER.info("Loaded {} translation files", TRANSLATION_FILES.size());
    }
}
