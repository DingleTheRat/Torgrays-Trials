// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.dingletherat.torgrays_trials.Main;
import org.json.JSONObject;

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
                Main.LOGGER.warn("From getJsonObject(): \"{}\" does not lead to a json file", file);
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
     * Gets all the names of the files in a given directory.
     * <p>
     * @param directoryPath The path to the directory you want the method to get the names of the files of. EX: {@code "values/translations"}
     * @return A {@link List} of Strings with all the names of the files contained in the directory
     **/
    public static List<String> getFileNames(String directoryPath) {
        // Get the directory that of which we need to get the file names of
        FileHandle directory = Gdx.files.internal(directoryPath);

        // Make sure that the directory exists and that it's a directory. If not, warn
        if (!directory.exists() || !directory.isDirectory()) {
            Main.LOGGER.warn("From getFileNames(): {} is not a directory!", directoryPath);
            return Collections.emptyList();
        }

        /*
         * Get the names of the files in the directory.
         * This is done by turning the directory into an array of the files in the directory.
         * Then, we get the names of those files and turn it into a list.
         */
        List<String> returnList = Arrays.stream(directory.list())
            .map(FileHandle::name)
            .collect(Collectors.toList());

        return returnList;
    }

    /**
     * Gets all the names of the files in a given directory, as long as they are the required file type.
     * <p>
     * @param directoryPath The path to the directory you want the method to get the names of the files of. EX: {@code "values/translations"}
     * @param fileType The type of file you want to get the names of. For instance, {@code .json}.
     * @return A {@link List} of Strings with all the names of the files contained in the directory
     **/
    public static List<String> getFileNames(String directoryPath, String fileType) {
        // Get the fileNames by using the other version of the method
        List<String> fileNames = getFileNames(directoryPath);

        /* Remove every string in the list that doesn't contain the fileType
           This is done by filtering out all the ones that don't contain it, and remove eahc of the remaining */
        fileNames.stream()
            .filter(fileName -> !fileName.contains(fileType))
            .forEach(fileName -> fileNames.remove(fileName));

        return fileNames;
    }

    /**
     * Gets all the names of the files in a given directory and the decendants of those files, as long as they are the required file type.
     * <p>
     * @param directoryPath The path to the directory you want the method to get the names of the files of. EX: {@code "values/translations"}
     * @param fileType The type of file you want to get the names of. For instance, {@code .json}.
     * @return A {@link List} of Strings with all the names of the files contained in the directory and its decendants
     **/
    public static List<String> getDecendantFileNames(String directoryPath, String fileType) {
        // Get the return list to which we will add the fileNames
        List<String> fileNames = new ArrayList<>();

        // Loop through all the fileNames in the directory which are obtained via the getFileNames method
        for (String fileName : getFileNames(directoryPath)) {
            // If the file has is the file type, add it to the list
            if (fileName.contains(fileType)) fileNames.add(fileName);
            // If not, call this method on the file to get all the fileNames of the decendants and add them (if it has any)
            else fileNames.addAll(getDecendantFileNames(directoryPath, fileType));
        }
        return fileNames;
    }
}
