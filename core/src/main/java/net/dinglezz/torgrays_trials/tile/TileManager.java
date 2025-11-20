package net.dinglezz.torgrays_trials.tile;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.IntStream;

public class TileManager {
    public static HashMap<Integer, Tile> tile = new HashMap<>();
    public static final HashMap<String, HashMap<TilePoint, Integer>> mapTileNumbers = new HashMap<>();


    public static void setup() {
        // Add layers to the mapTileNumbers HashMap
        mapTileNumbers.put("ground", new HashMap<>());
        //mapTileNumbers.put("event", new HashMap<>());
        mapTileNumbers.put("foreground", new HashMap<>());

        // Register Tiles
            registerTile(0, "nothing", false);

            // Grass
            registerTile(10, "grass/grass_1", false);
            registerTile(11, "grass/grass_2", false);

            // Water
            registerTile(12, "water/water", false);
            registerTile(13, "water/white_line_water", false);
            registerTile(14, "water/water_corner_1", true);
            registerTile(15, "water/water_edge_3", true);
            registerTile(16, "water/water_corner_3", true);
            registerTile(17, "water/water_edge_4", true);
            registerTile(18, "water/water_edge_2", true);
            registerTile(19, "water/water_corner_2", true);
            registerTile(20, "water/water_edge_1", true);
            registerTile(21, "water/water_corner_4", true);
            registerTile(22, "water/water_outer_corner_1", true);
            registerTile(23, "water/water_outer_corner_2", true);
            registerTile(24, "water/water_outer_corner_3", true);
            registerTile(25, "water/water_outer_corner_4", true);

            // Path
            registerTile(26, "path/path", false);
            registerTile(27, "path/path_corner_1", false);
            registerTile(28, "path/path_edge_1", false);
            registerTile(29, "path/path_corner_2", false);
            registerTile(30, "path/path_edge_4", false);
            registerTile(31, "path/path_edge_2", false);
            registerTile(32, "path/path_corner_3", false);
            registerTile(33, "path/path_edge_3", false);
            registerTile(34, "path/path_corner_4", false);
            registerTile(35, "path/path_outer_corner_1", false);
            registerTile(36, "path/path_outer_corner_2", false);
            registerTile(37, "path/path_outer_corner_3", false);
            registerTile(38, "path/path_outer_corner_4", false);

            // Building Stuff
            registerTile(39, "floor", false);
            registerTile(40, "planks", true);

            // Tree
            registerTile(41, "tree/tree", true);

            // Event Tiles
            registerTile(42, "path/path_pit", false);
            registerTile(43, "grass/grass_pit", false);
            registerTile(44, "grass/grass_healing", false);
            registerTile(45, "coiner's_hut", false);

            // Dark Tiles
            registerTile(46, "tree/dark_tree", true);
            registerTile(47, "grass/dark_grass", false);

            registerTile(48, "tunnel_door", false);
    }
    public static void registerTile(int i, String imageName, boolean collision) {
        try {
            tile.put(i, new Tile());
            try {
                tile.get(i).image = ImageIO.read(TileManager.class.getResourceAsStream("/drawable/tile/" + imageName + ".png"));
            } catch (IllegalArgumentException exception) {
                // If the image is not found, use the disabled image and print a warning
                System.err.println("\"" + imageName + "\" is not a valid path.");
                tile.get(i).image = ImageIO.read(TileManager.class.getResourceAsStream("/drawable/disabled.png"));

                // Then, throw the exception
                Main.game.exceptionState = States.ExceptionStates.ONLY_IGNORABLE;
                throw exception;
            }
            tile.get(i).image = UtilityTool.scaleImage(tile.get(i).image, Main.game.tileSize, Main.game.tileSize);
            tile.get(i).collision = collision;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void draw(Graphics2D graphics2D) {
        float playerWorldX = Main.game.player.worldX;
        float playerWorldY = Main.game.player.worldY;
        int playerScreenX = Main.game.player.screenX;
        int playerScreenY = Main.game.player.screenY;
        int tileSize = Main.game.tileSize;

        for (String layer : TileManager.mapTileNumbers.keySet()) {
            // Skip the layer if it is not present in the map
            if (TileManager.mapTileNumbers.get(layer).get(new TilePoint(Main.game.currentMap, 0, 0)) == null && layer.equals("foreground")) continue;

            IntStream.range(0, Main.game.maxWorldRow).parallel().forEach(
                    worldRow -> IntStream.range(0, Main.game.maxWorldCol).parallel().forEach(worldCol -> {
                        // Position Fields
                        int tileNumber = mapTileNumbers.get(layer).get(new TilePoint(Main.game.currentMap, worldCol, worldRow));
                        int worldX = worldCol * tileSize;
                        int worldY = worldRow * tileSize;
                        float screenX = worldX - playerWorldX + playerScreenX;
                        float screenY = worldY - playerWorldY + playerScreenY;

                        // Check if the tile is within the visible screen
                        if (worldX + tileSize > playerWorldX - playerScreenX &&
                                worldX - tileSize < playerWorldX + playerScreenX &&
                                worldY + tileSize > playerWorldY - playerScreenY &&
                                worldY - tileSize < playerWorldY + playerScreenY) {
                            Tile currentTile = tile.get(tileNumber);
                            graphics2D.drawImage(currentTile.image, Math.round(screenX), Math.round(screenY), null);
                            if (Main.game.debugHitBoxes && currentTile.collision && layer.equals("foreground")) {
                                graphics2D.setColor(new Color(0.7f, 0, 0, 0.3f));
                                graphics2D.fillRect(Math.round(screenX), Math.round(screenY), tileSize, tileSize);
                            }
                        }
                    }));
        }

        if (Main.game.debugPathfinding) {
            graphics2D.setColor(new Color(0, 0, 0.7f, 0.3f));
            for (var pathNode : Main.game.pathFinder.pathList) {
                int worldX = pathNode.col * tileSize;
                int worldY = pathNode.row * tileSize;
                float screenX = worldX - playerWorldX + playerScreenX;
                float screenY = worldY - playerWorldY + playerScreenY;

                graphics2D.fillRect(Math.round(screenX), Math.round(screenY), tileSize, tileSize);
            }
        }
    }
}
