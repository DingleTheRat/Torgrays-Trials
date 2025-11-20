package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.main.UtilityTool;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class LootTableHandler {
    public static final HashMap<String, JSONObject> lootTables = new HashMap<>();

    /// Adds a loot table file to the lootTables hashmap
    public static void registerLootTable(String fileName) {
        JSONObject file = UtilityTool.getJsonObject("/values/loot_tables/" + fileName + ".json");
        try {
            String name = file.getString("name");
            lootTables.put(name, file);
        } catch (JSONException exception) {
            System.err.println("Couldn't load loot table");
            System.err.println("Missing loot table data in " + fileName + ".json");
        }
    }

    /// Looks through the loot tables folder and loads all loot tables (limit 1 package deep)
     public static void loadLootTables() {
         String[] mapFiles = UtilityTool.getResourceFileNames("/values/loot_tables");
         for (String mapFile : mapFiles) {
             if (mapFile.endsWith(".json")) {
                 String mapName = mapFile.substring(0, mapFile.lastIndexOf(".json"));
                 registerLootTable(mapName);
             } else if (!mapFile.contains(".")) { // Check if it's a directory
                 String[] subFiles = UtilityTool.getResourceFileNames("/values/loot_tables/" + mapFile);
                 for (String subFile : subFiles) {
                     if (subFile.endsWith(".json")) {
                         String mapName = subFile.substring(0, subFile.lastIndexOf(".json"));
                         registerLootTable(mapFile + "/" + mapName);
                     }
                 }
             }
         }
     }
    public static ArrayList<Item> generateLoot(JSONObject lootTable) {
        // The return array list
        ArrayList<Item> finalLoot = new ArrayList<>();

        // Get the multi-select loot array
        JSONArray multiSelectLoot;
        try {
            multiSelectLoot = lootTable.getJSONArray("loot");
        } catch (JSONException | NullPointerException exception) {
            System.err.println("Missing loot table array in the loot table JSON by the name of '" + lootTable.optString("name", "unknown") + "'");
            return finalLoot; // Return early if the loot array is missing
        }

        // Get optional minimum and maximum
        int minimum = 0;
        int maximum = 0;
        if (lootTable.has("minimum")) minimum = lootTable.getInt("minimum");
        if (lootTable.has("maximum")) maximum = lootTable.getInt("maximum");

        int step = 0;
        int selects = 0;
        while (step <= 100) {
            step++;
            // Do multi select
            for (int i = 0; i < multiSelectLoot.length(); i++) {
                JSONObject loot = multiSelectLoot.getJSONObject(i);
                float multiRandom = new Random().nextFloat(); // Random number between 0 and 1

                try {
                    // If so, do single-select
                    if (multiRandom <= loot.getFloat("chance")) {
                        // Minimum and maximum
                        selects++;
                        if (selects < minimum && minimum != 0) break;
                        if (selects > maximum && maximum != 0) break;

                        JSONArray singleSelectLoot = loot.optJSONArray("loot");

                        // Check if the chance adds up to 1f
                        float totalChance = 0f;

                        for (int j = 0; j < singleSelectLoot.length(); j++) {
                            JSONObject lootItem = singleSelectLoot.optJSONObject(j);
                            if (lootItem.has("chance")) {
                                totalChance += lootItem.getFloat("chance");
                            } else {
                                System.err.println("Missing loot items chance in " + lootItem + " in loot table by the name of '" + lootTable.getString("name") + "'");
                                return finalLoot;
                            }
                        }
                        if (Math.abs(totalChance - 1f) > 1e-6) {
                            System.err.println("Loot items chances do not add up to 1f in " + singleSelectLoot + " in loot table by the name of '" + lootTable.getString("name") + "'");
                            return finalLoot;
                        }

                        float singleRandom = new Random().nextFloat(); // Random number between 0 and 1
                        float cumulativeChance = 0f;

                        for (int j = 0; j < singleSelectLoot.length(); j++) {
                            JSONObject lootItem = singleSelectLoot.optJSONObject(j);

                            try {
                                float chance = lootItem.getFloat("chance");
                                cumulativeChance += chance;

                                if (singleRandom <= cumulativeChance) {
                                    String itemPath = lootItem.getString("item");

                                    if (!itemPath.isEmpty()) {
                                        Item item = UtilityTool.generateEntity(itemPath, null);
                                        item.amount = lootItem.getInt("amount");
                                        finalLoot.add(item);
                                    } else finalLoot.add(null);
                                    break;
                                }
                            } catch (JSONException | NullPointerException exception) {
                                System.err.println("Couldn't load loot items");
                                System.err.println("Missing loot items data in " + lootItem + " in loot table by the name of '" + lootTable.getString("name") + "'");
                                return finalLoot;
                            } catch (NumberFormatException exception) {
                                System.err.println("Invalid coin format in loot items. Ensure it is 'COIN_X', where X is a number.");
                                return finalLoot;
                            }
                        }

                    }
                } catch (JSONException | NullPointerException exception) {
                    System.err.println("Couldn't load loot items");
                    System.err.println("Missing loot items data in " + loot + " in loot table by the name of '" + lootTable.getString("name") + "'");
                    return finalLoot;
                }
            }
            if (!finalLoot.isEmpty()) {
                break;
            }
            if (step >= 100) {
               System.err.println("Couldn't reach loot table minimum and maximum in 100 steps in loot table by the name of '" + lootTable.getString("name") + "'");
            }
        }
        return finalLoot;
    }
}
