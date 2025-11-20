package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.entity.Player;
import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.monster.Monster;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DataManager implements Serializable {
    // Stuff to save/load
    int slot;
    int darknessCounter;
    States.DarknessStates darknessState;
    String difficulty;

    Player player;
    public HashMap<String, ArrayList<Entity>> objects;
    public HashMap<String, ArrayList<Item>> items;
    public HashMap<String, ArrayList<Mob>> npcs;
    public HashMap<String, ArrayList<Monster>> monsters;

    // Save/load data methods
    public static void saveData(int slot) {
        // Return if the slot is higher than the limit
        if (slot < 0 | slot > 3) return;

        try {
            // Make a .torgray directory if it doesn't exist
            String userHome = System.getProperty("user.home");
            File directory = new File(userHome, ".torgray");
            if (!directory.exists()) directory.mkdir();

            // Get output stream to save the data in
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(directory, "torgrays-trials-save-" + slot + ".dat")));

            // Make a new data manager class instance and store the necessary data
            DataManager dataManager = new DataManager();
            dataManager.slot = slot;
            dataManager.darknessCounter = Main.game.environmentManager.lighting.darknessCounter;
            dataManager.darknessState = Main.game.environmentManager.lighting.darknessState;
            dataManager.difficulty = Main.game.difficulty;

            dataManager.player = Main.game.player;
            dataManager.objects = Main.game.objects;
            dataManager.items = Main.game.items;
            dataManager.npcs = Main.game.npcs;
            dataManager.monsters = Main.game.monsters;

            // Write the data to the file
            objectOutputStream.writeObject(dataManager);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    public static void autoSaveData() {
        if (Main.game.saveSlot != 0) {
            saveData(Main.game.saveSlot);
            Main.game.ui.addMiniNotification("Game auto-saved!");
        }
    }

    public static boolean loadData(int slot) {
        // Return if the slot is higher than the limit
        if (slot < 0 | slot > 3) return false;

        // Make a .torgray directory if it doesn't exist
        String userHome = System.getProperty("user.home");
        File directory = new File(userHome, ".torgray");
        if (!directory.exists()) directory.mkdir();

        // Return if the save file doesn't exist
        if (!new File(directory, "torgrays-trials-save-" + slot + ".dat").exists()) return false;

        try {
            // Get input stream to read the data from
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(directory, "torgrays-trials-save-" + slot + ".dat")));

            // Read the data from the file and retrieve the necessary data
            DataManager dataManager = (DataManager) objectInputStream.readObject();
            Main.game.saveSlot = slot;
            Main.game.environmentManager.lighting.darknessCounter = dataManager.darknessCounter;
            Main.game.environmentManager.lighting.darknessState = dataManager.darknessState;
            Main.game.difficulty = dataManager.difficulty;

            Main.game.player = dataManager.player;
            Main.game.objects = dataManager.objects;
            Main.game.items = dataManager.items;
            Main.game.npcs = dataManager.npcs;
            Main.game.monsters = dataManager.monsters;
            return true;
        } catch (InvalidClassException exception) {
            // Make an archive directory (if it doesn't exist)
            File archive = new File(directory, "archive");
            if (!archive.exists()) archive.mkdir();

            // Store the old save file in the archive
            new File(directory, "torgrays-trials-save-" + slot + ".dat").renameTo(new File(archive, "torgrays-trials-save-archive-" + slot + ".dat"));

            System.err.println("Warning: Save file for slot " + slot + " doesn't contain the right data, saving to archive.");

            // Act like nothing happened!
            return false;
        } catch (IOException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void saveConfig() {
        try {
            // Make a .torgray directory if it doesn't exist
            String userHome = System.getProperty("user.home");
            File directory = new File(userHome, ".torgray");
            if (!directory.exists()) directory.mkdir();

            File configFile = new File(directory, "torgrays-trials-config.txt");
            
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));

            // Music Volume
            bufferedWriter.write(String.valueOf(Sound.music.volumeScale));
            bufferedWriter.newLine();

            // Sound Volume
            bufferedWriter.write(String.valueOf(Sound.sfx.volumeScale));
            bufferedWriter.newLine();

            // Full screen
            bufferedWriter.write(String.valueOf(Main.game.fullScreen));
            bufferedWriter.newLine();

            // BRendering
            bufferedWriter.write(String.valueOf(Main.game.BRendering));
            bufferedWriter.newLine();

            // Pathfinding
            bufferedWriter.write(String.valueOf(Main.game.pathFinding));
            bufferedWriter.newLine();

            bufferedWriter.close();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    public static void loadConfig() {
        try {
            // Make a .torgray directory, if it doesn't exist
            String userHome = System.getProperty("user.home");
            File directory = new File(userHome, ".torgray");
            if (!directory.exists()) directory.mkdir();

            File configFile = new File(directory, "torgrays-trials-config.txt");
            if (configFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write("3\n");
                fileWriter.write("3\n");
                fileWriter.write("false\n");
                fileWriter.write("false\n");
                fileWriter.write("false\n");
                fileWriter.close();
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
            String s = bufferedReader.readLine();

            // Music Volume
            Sound.music.volumeScale = Integer.parseInt(s);

            // Sound Volume
            s = bufferedReader.readLine();
            Sound.sfx.volumeScale = Integer.parseInt(s);

            // Full screen
            s = bufferedReader.readLine();
            Main.game.fullScreen = s.equals("true");

            // BRendering
            s = bufferedReader.readLine();
            Main.game.BRendering = s.equals("true");

            // Pathfinding
            s = bufferedReader.readLine();
            Main.game.pathFinding = s.equals("true");

            bufferedReader.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } catch (Exception exception) {
            System.err.println("Error: Couldn't read config file, creating a new one.");
            try {
                String userHome = System.getProperty("user.home");
                File directory = new File(userHome, ".torgray");
                File configFile = new File(directory, "torgrays-trials-config.txt");
                FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write("3\n");
                fileWriter.write("3\n");
                fileWriter.write("false\n");
                fileWriter.write("false\n");
                fileWriter.write("false\n");
                fileWriter.close();
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }
}
