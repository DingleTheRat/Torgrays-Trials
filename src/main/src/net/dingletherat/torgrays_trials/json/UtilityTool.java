// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import net.dingletherat.torgrays_trials.main.Main;

/**
* The toolbox of the game.
 * It has many miscellaneous methods that are used throughout the code.
 **/
public class UtilityTool {
    /**
     * Converts a given JSON file into a JSONObject.
     *
     * @param filePath The file path for the file, which has to be a JSON, that is going to be converted to a JSONObject. EX: "values/translations/english.json"
     * @return Returns a JSONObject that was made from the file
     **/
    public static JSONObject getJsonObject(String filePath) {
        // Firstly, get the inputStream from the filePath given
        try (InputStream inputStream = UtilityTool.class.getResourceAsStream(filePath)) {
            // Warn if the inputStream is null, meaning it couldn't find the path
            if (inputStream == null) {
                Main.LOGGER.warn("From: getJsonObject(), \"" + filePath + "\" is not a valid path");
                return null;
            }

            // Warn if the filePath does not contain ".json", meaning it's not a JSON file
            if (!filePath.endsWith(".json")) {
                Main.LOGGER.warn("From: getJsonObject(): \"" + filePath + "\" does not lead to a json file");
                return null;
            }

            /* Next, with the BufferedReader, read the contents of the inputStream (the file).
             * The buffered reader reads the contents of the file line by line and adds it to a StringBuilder
             * The stringBuilder is later converted into a JSONObject and returned
             */
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) stringBuilder.append(line);
                return new JSONObject(stringBuilder.toString());
            }
        } catch (IOException exception) {
            Main.handleException(exception);
            return null;
        }
    }

    /**
     * Gets all the names of the contents of a given directory.
     * @param directoryPath The path to the directory you want the method to get the names of the contents of. EX: "values/translations"
     * @return A String array of all the names of the files contained in the directory
     **/
    public static String[] getFileNames(String directoryPath) {
        // Make an input stream from the directoryPath
        try (InputStream inputStream = UtilityTool.class.getResourceAsStream(directoryPath)) {
            // If the inputStream is null, meaning something is wrong with the directoryPath, then return and warn
            if (inputStream == null) {
                Main.LOGGER.warn("From: getFileNames(): \"" + directoryPath + "\" is not a valid path.");
                return null;
            }

            /*
             * Make a bufferedReader for the directory in the inputStream
             * With the reader, we can get all the lines (contents) of the directoryPath
             * The contents are simply the fileNames, and nothing else
             * Those contents are converted into an arrayList of Strings
             */
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return bufferedReader.lines().toArray(String[]::new);
        } catch (IOException exception) {
            Main.handleException(exception);
            return null;
        }
    }
}
