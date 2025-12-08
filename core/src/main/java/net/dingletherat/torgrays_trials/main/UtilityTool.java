// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.dingletherat.torgrays_trials.Main;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The toolbox of the game.
 * It has many miscellaneous methods that are used throughout the code.
 **/
public class UtilityTool {
    /**
     * Converts a given JSON file into a {@code JSONObject}.
     *
     * @param path The file path for the file, which has to be a JSON, that is going to be converted to a {@code
     * JSONObject}. {@code EX: "values/translations/english.json"}
     * @return Returns a {@code JSONObject} that was made from the file
     **/
    public static JSONObject getJsonObject(String path) {
        try {
            FileHandle file = Gdx.files.internal(path);

            if (!file.exists()) {
                Main.LOGGER.error("Warning: \"{}\" is not a valid path.", path);
                return null;
            }

            // Warn if the filePath does not contain ".json", meaning it's not a JSON file
            if (!file.name().endsWith(".json")) {
                Main.LOGGER.warn("From: getJsonObject(): \"{}\" does not lead to a json file", file);
                return null;
            }

            String content = file.readString("UTF-8");
            return new JSONObject(content);

        } catch (Exception e) {
            Main.handleException(e);
            return new JSONObject();
        }
    }

    /**
     * Gets all the names of the contents of a given directory.
     * @param directoryPath The path to the directory you want the method to get the names of the contents of. {@code EX: "values/translations"}
     * @return A String array of all the names of the files contained in the directory
     **/
    public static List<String> getFileNames(String directoryPath) {
        // This is the LibGDX way of getting files
        FileHandle dir = Gdx.files.internal(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            Main.LOGGER.warn("Invalid directory: {}", directoryPath);
            return Collections.emptyList();
        }

        return Arrays.stream(dir.list())
            .map(FileHandle::name)
            .collect(Collectors.toList());
    }
}
