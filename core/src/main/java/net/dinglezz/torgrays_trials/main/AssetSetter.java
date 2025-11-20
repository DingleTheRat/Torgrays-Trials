package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.tile.MapHandler;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AssetSetter {
    /// Calls all the asset settings methods
    /// @param removePrevious If true, the previous objects, NPCs, and Monsters in the current map will be all cleared
    public static void setAssets(boolean removePrevious) {
        AssetSetter.setObjects(removePrevious);
        AssetSetter.setNPCs(removePrevious);
        AssetSetter.setMonsters(removePrevious);
        AssetSetter.setItems(removePrevious);
        AssetSetter.setEvents();
    }
    /**
     * Loads assets for the current map, ensuring that all necessary game resources are initialized.
     * This method verifies the existence of objects, items, NPCs, and monsters for the current map
     * and initializes them with default configurations if they are not already set. It also sets up
     * events for the current map.
     * <p>
     * - If objects, items, NPCs, or monsters for the current map are missing, their respective setter
     *   methods (setObjects, setItems, setNPCs, setMonsters) are invoked with a `false` parameter
     *   to ensure previously existing data is retained.
     * <p>
     * - Events for the current map are initialized by calling the `setEvents` method.
     */
    public static void loadAssets() {
        if (Main.game.objects.get(Main.game.currentMap) == null) AssetSetter.setObjects(false);
        if (Main.game.items.get(Main.game.currentMap) == null) AssetSetter.setItems(false);
        if (Main.game.npcs.get(Main.game.currentMap) == null) AssetSetter.setNPCs(false);
        if (Main.game.monsters.get(Main.game.currentMap) == null) AssetSetter.setMonsters(false);
        AssetSetter.setEvents();
    }
    /**
     * Initializes and sets up game objects for the current map based on its JSON configuration.
     * This method reads the map's JSON file and processes its "entities" section to locate
     * and position objects on the current map. The objects can have optional attributes such as
     * a loot table, and their settings are managed through utility methods.
     * The method also handles clearing previously existing objects if specified.
     *
     * @param removePrevious If true, all previously existing objects in the current map
     *                       are cleared before setting up the new objects.
     */
    public static void setObjects(boolean removePrevious) {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        if (file != null) {
            Main.game.objects.putIfAbsent(Main.game.currentMap, new ArrayList<>());
            if (removePrevious) {
                Main.game.objects.get(Main.game.currentMap).clear();
            }

            try {
                JSONArray objects = file.getJSONObject("entities").getJSONArray("objects");

                for (int i = 0; i < objects.length(); i++) {
                    JSONObject object = objects.getJSONObject(i);
                    String path = object.getString("path");
                    int col = object.getInt("col");
                    int row = object.getInt("row");

                    if (object.has("loot table")) {
                        String lootTable = object.getString("loot table");
                        Main.game.objects.get(Main.game.currentMap).add(i, UtilityTool.generateEntity(path, new TilePoint(file.getString("name"), col, row), lootTable));
                    } else {
                        Main.game.objects.get(Main.game.currentMap).add(i, UtilityTool.generateEntity(path, new TilePoint(file.getString("name"), col, row)));
                    }
                    Main.game.objects.get(Main.game.currentMap).get(i).worldX = col * Main.game.tileSize;
                    Main.game.objects.get(Main.game.currentMap).get(i).worldY = row * Main.game.tileSize;
                }
            } catch (JSONException jsonException) {
                System.err.println("Couldn't load objects");
                System.err.println("Missing objects data in loot table by the name of '" + file.getString("name") + "'");
            }
        }
    }
    /**
     * Initializes and sets up game items for the current map based on its JSON configuration.
     * This method reads the map's JSON file and processes its "entities" section to locate
     * and position items on the current map. The items can have optional attributes such as
     * a loot table, and their settings are managed through utility methods.
     * The method also handles clearing previously existing items if specified.
     *
     * @param removePrevious If true, all previously existing items in the current map
     *                       are cleared before setting up the new items.
     */
    public static void setItems(boolean removePrevious) {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        if (file != null) {
            Main.game.items.putIfAbsent(Main.game.currentMap, new ArrayList<>());
            if (removePrevious) {
                Main.game.items.get(Main.game.currentMap).clear();
            }

            try {
                JSONArray items = file.getJSONObject("entities").getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject object = items.getJSONObject(i);
                    String path = object.getString("path");
                    int col = object.getInt("col");
                    int row = object.getInt("row");

                    if (object.has("loot table")) {
                        String lootTable = object.getString("loot table");
                        Main.game.items.get(Main.game.currentMap).add(i, UtilityTool.generateEntity(path, new TilePoint(file.getString("name"), col, row), lootTable));
                    } else {
                        Main.game.items.get(Main.game.currentMap).add(i, UtilityTool.generateEntity(path, new TilePoint(file.getString("name"), col, row)));
                    }
                    Main.game.items.get(Main.game.currentMap).get(i).worldX = col * Main.game.tileSize;
                    Main.game.items.get(Main.game.currentMap).get(i).worldY = row * Main.game.tileSize;
                }
            } catch (JSONException jsonException) {
                System.err.println("Couldn't load items");
                System.err.println("Missing items data in loot table by the name of '" + file.getString("name") + "'");
            }
        }
    }
    /**
     * Reads the map file of the current map and initializes the NPCs listed in the file.
     * This method sets up NPCs based on their configuration in the map's JSON data,
     * associating them with their specified locations, attributes, and optional loot tables.
     *
     * @param removePrevious If true, all previously existing NPCs in the current map are cleared
     *                       before setting up the new NPCs.
     */
    public static void setNPCs(boolean removePrevious) {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        if (file != null) {
            Main.game.npcs.putIfAbsent(Main.game.currentMap, new ArrayList<>());
            if (removePrevious) {
                Main.game.npcs.get(Main.game.currentMap).clear();
            }

            try {
                JSONArray npcs = file.getJSONObject("entities").getJSONArray("npcs");

                for (int i = 0; i < npcs.length(); i++) {
                    JSONObject npc = npcs.getJSONObject(i);
                    String path = npc.getString("path");
                    int col = npc.getInt("col");
                    int row = npc.getInt("row");

                    if (npc.has("loot table")) {
                        String lootTable = npc.getString("loot table");
                        Main.game.npcs.get(Main.game.currentMap).add(i, (Mob) UtilityTool.generateEntity(path, new TilePoint(file.getString("name"), col, row), lootTable));
                    } else {
                        Main.game.npcs.get(Main.game.currentMap).add(i, (Mob) UtilityTool.generateEntity(path,new TilePoint(file.getString("name"), col, row)));
                    }
                    Main.game.npcs.get(Main.game.currentMap).get(i).worldX = col * Main.game.tileSize;
                    Main.game.npcs.get(Main.game.currentMap).get(i).worldY = row * Main.game.tileSize;
                }
            } catch (JSONException jsonException) {
                System.err.println("Couldn't load npcs");
                System.err.println("Missing npcs data in loot table by the name of '" + file.getString("name") + "'");
            }
        }
    }
    /**
     * Reads the map file of the current map and initializes the monsters listed in the file.
     * This method sets up monsters based on their configuration in the map's JSON data,
     * associating them with their specified locations, attributes, and optional loot tables.
     *
     * @param removePrevious If true, all previously existing monsters in the current map are cleared
     *                        before setting up the new monsters.
     */
    public static void setMonsters(boolean removePrevious) {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        if (file != null) {
            Main.game.monsters.putIfAbsent(Main.game.currentMap, new ArrayList<>());
            if (removePrevious) {
                Main.game.monsters.get(Main.game.currentMap).clear();
            }

            try {
                JSONArray monsters = file.getJSONObject("entities").getJSONArray("monsters");

                for (int i = 0; i < monsters.length(); i++) {
                    JSONObject monster = monsters.getJSONObject(i);
                    String path = monster.getString("path");
                    int col = monster.getInt("col");
                    int row = monster.getInt("row");

                    if (monster.has("loot table")) {
                        String lootTable = monster.getString("loot table");
                        Main.game.monsters.get(Main.game.currentMap).add(i, UtilityTool.generateEntity(path, new TilePoint(file.getString("name"), col, row),lootTable));
                    } else {
                        Main.game.monsters.get(Main.game.currentMap).add(i, UtilityTool.generateEntity(path, new TilePoint(file.getString("name"), col, row)));
                    }
                    Main.game.monsters.get(Main.game.currentMap).get(i).worldX = col * Main.game.tileSize;
                    Main.game.monsters.get(Main.game.currentMap).get(i).worldY = row * Main.game.tileSize;
                }
            } catch (JSONException jsonException) {
                System.err.println("Couldn't load monsters");
                System.err.println("Missing monsters data in loot table by the name of '" + file.getString("name") + "'");
            }
        }
    }

    /**
     * Configures and initializes events for the current map based on its JSON configuration.
     * This method retrieves the "events" section from the JSON file associated with the current map
     * and processes each event to generate it within the game. For each event, the method identifies
     * its configuration details such as file path, grid position (column and row), and optional parameters.
     * Events are generated through a utility method that handles their creation and placement on the map.
     * <p>
     * If the map file does not contain event data or there is an error in processing the JSON data, the
     * method logs an error message to the console.
     */
    public static void setEvents() {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        if (file != null) {
            try {
                JSONArray events = file.getJSONArray("events");

                for (int i = 0; i < events.length(); i++) {
                    // Event objects
                    JSONObject event = events.getJSONObject(i);

                    // Settings
                    String path = event.getString("path");
                    int col = event.getInt("col");
                    int row = event.getInt("row");
                    JSONObject parameters = new JSONObject();

                    // Check if the event has parameters and assign it if it does
                    if (event.has("parameters")) {
                        parameters = event.getJSONObject("parameters");
                    }

                    // Finally, create the event
                    UtilityTool.generateEvent(path, Main.game.currentMap, col, row, parameters);

                }
            } catch (JSONException jsonException) {
                System.err.println("Couldn't load events");
                System.err.println("Missing event data in loot table by the name of '" + file.getString("name") + "'");
            }
        }
    }
}
