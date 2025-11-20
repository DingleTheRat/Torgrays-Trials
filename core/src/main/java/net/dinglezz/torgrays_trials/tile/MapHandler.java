package net.dinglezz.torgrays_trials.tile;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.UtilityTool;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class MapHandler {
    public static final HashMap<String, JSONObject> mapFiles = new HashMap<>();
    public static final ArrayList<String> maps = new ArrayList<>();

    public static HashMap<String, HashMap<String, BufferedImage>> worldMap = new HashMap<>();

    public static void loadMaps() {
        String[] mapFiles = UtilityTool.getResourceFileNames("/values/maps");
        for (String mapFile : mapFiles) {
            if (mapFile.endsWith(".json")) {
                String mapName = mapFile.substring(0, mapFile.lastIndexOf(".json"));
                loadMap(mapName);
            } else if (!mapFile.contains(".")) { // Check if it's a directory
                String[] subFiles = UtilityTool.getResourceFileNames("/values/maps/" + mapFile);
                for (String subFile : subFiles) {
                    if (subFile.endsWith(".json")) {
                        String mapName = subFile.substring(0, subFile.lastIndexOf(".json"));
                        loadMap(mapFile + "/" + mapName);
                    }
                }
            }
        }

        createWorldMap("ground");
        createWorldMap("foreground");
    }

    public static void loadMap(String fileName) {
        // Get the file and check if it exists
        JSONObject file = UtilityTool.getJsonObject("/values/maps/" + fileName + ".json");
        if (file == null) {
            file = UtilityTool.getJsonObject("/values/maps/disabled.json");
        }

        // Get required values
        String name;
        JSONObject map;
        JSONArray ground;

        try {
            name = file.getString("name");
            map = file.getJSONObject("map");
            ground = map.getJSONArray("ground");
        } catch (JSONException jsonException) {
            System.err.println("Failed to find essential map data in " + fileName + ".json. Using default map.");
            file = UtilityTool.getJsonObject("/values/maps/disabled.json");
            name = file.getString("name");
            map = file.getJSONObject("map");
            ground = map.getJSONArray("ground");
        }

        JSONArray foreground = null;
        if (map.has("foreground")) {
            foreground = map.getJSONArray("foreground");
        }

        // Read the map file
        for (String layer : TileManager.mapTileNumbers.keySet()) {
            if (layer.equals("foreground") && foreground == null) {
                continue;
            }
            JSONArray layerArray = switch (layer) {
                case "ground" -> ground;
                case "foreground" -> foreground;
                default -> throw new IllegalStateException("Unexpected layer: " + layer);
            };

            int col = 0;
            int row = 0;
            while (row < Main.game.maxWorldRow) {
                String mapLine = layerArray.getString(row);
                while (col < Main.game.maxWorldCol) {
                    String[] numbers = mapLine.split(" ");
                    int number = Integer.parseInt(numbers[col]);
                    TileManager.mapTileNumbers.get(layer).put(new TilePoint(name, col, row), number);

                    // If the tile number is not registered, register it as a disabled tile
                    if (TileManager.tile.get(number) == null) {
                        System.err.println("Index " + number + " is not a valid tile in " + name + " map.");
                        TileManager.registerTile(number, "", false);
                    }

                    // Warn if a collision tile is not on the foreground layer
                    if (TileManager.tile.get(number).collision && !layer.equals("foreground")) {
                        System.err.println("Collision tile " + number + " is not on the foreground layer in " +  name + " map.");
                        System.err.println("Collisions are only checked on the foreground layer.");
                    }
                    col++;
                }
                if (col == Main.game.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        }

        // Add to some useful HashMap and Array List
        mapFiles.put(name, file);
        maps.add(name);
    }

    public static void createWorldMap(String layer) {
        int worldMapWidth = Main.game.tileSize * Main.game.maxWorldCol;
        int worldMapHeight = Main.game.tileSize * Main.game.maxWorldRow;

        for (String map : maps) {
            HashMap<String, BufferedImage> layerMap = worldMap.computeIfAbsent(map, k -> new HashMap<>());
            layerMap.put(layer, new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB));
            Graphics2D graphics2D = layerMap.get(layer).createGraphics();

            int col = 0;
            int row = 0;
            while (row < Main.game.maxWorldRow) {
                while (col < Main.game.maxWorldCol) {
                    int tileNumber = TileManager.mapTileNumbers.get(layer).get(new TilePoint(map, col, row));
                    int x = col * Main.game.tileSize;
                    int y = row * Main.game.tileSize;

                    // Draw Tiles
                    if (TileManager.tile.get(tileNumber) != null)
                        graphics2D.drawImage(TileManager.tile.get(tileNumber).image, x, y, null);
                    col++;
                }
                col = 0;
                row++;
            }
            graphics2D.dispose();
        }
    }
}
