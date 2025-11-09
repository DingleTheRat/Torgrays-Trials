// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import net.dingletherat.torgrays_trials.main.Main;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;

public class TileManager {
    public static HashMap<Integer, Tile> tileTypes = new HashMap<>();
    public static HashMap<String, Map> maps = new HashMap<>();

    public static void loadTiles() {
        registerTile(0, "nothing", "00000/00000/00000/00000/00000");

        // Grass
        registerTile(10, "grass/grass_1", "00000/00000/00000/00000/00000");
        registerTile(11, "grass/grass_2", "00000/00000/00000/00000/00000");

        // Water
        registerTile(12, "water/water", "00000/00000/00000/00000/00000");
        registerTile(13, "water/white_line_water", "00000/00000/00000/00000/00000");
        registerTile(14, "water/water_corner_1", "11111/11111/11111/11111/11111");
        registerTile(15, "water/water_edge_3", "11111/11111/11111/11111/11111");
        registerTile(16, "water/water_corner_3", "11111/11111/11111/11111/11111");
        registerTile(17, "water/water_edge_4", "11111/11111/11111/11111/11111");
        registerTile(18, "water/water_edge_2", "11111/11111/11111/11111/11111");
        registerTile(19, "water/water_corner_2", "11111/11111/11111/11111/11111");
        registerTile(20, "water/water_edge_1", "11111/11111/11111/11111/11111");
        registerTile(21, "water/water_corner_4", "11111/11111/11111/11111/11111");
        registerTile(22, "water/water_outer_corner_1", "11111/11111/11111/11111/11111");
        registerTile(23, "water/water_outer_corner_2", "11111/11111/11111/11111/11111");
        registerTile(24, "water/water_outer_corner_3", "11111/11111/11111/11111/11111");
        registerTile(25, "water/water_outer_corner_4", "11111/11111/11111/11111/11111");

        // Path
        registerTile(26, "path/path", "00000/00000/00000/00000/00000");
        registerTile(27, "path/path_corner_1", "00000/00000/00000/00000/00000");
        registerTile(28, "path/path_edge_1", "00000/00000/00000/00000/00000");
        registerTile(29, "path/path_corner_2", "00000/00000/00000/00000/00000");
        registerTile(30, "path/path_edge_4", "00000/00000/00000/00000/00000");
        registerTile(31, "path/path_edge_2", "00000/00000/00000/00000/00000");
        registerTile(32, "path/path_corner_3", "00000/00000/00000/00000/00000");
        registerTile(33, "path/path_edge_3", "00000/00000/00000/00000/00000");
        registerTile(34, "path/path_corner_4", "00000/00000/00000/00000/00000");
        registerTile(35, "path/path_outer_corner_1", "00000/00000/00000/00000/00000");
        registerTile(36, "path/path_outer_corner_2", "00000/00000/00000/00000/00000");
        registerTile(37, "path/path_outer_corner_3", "00000/00000/00000/00000/00000");
        registerTile(38, "path/path_outer_corner_4", "00000/00000/00000/00000/00000");

        // Building Stuff
        registerTile(39, "floor", "00000/00000/00000/00000/00000");
        registerTile(40, "planks", "11111/11111/11111/11111/11111");

        // Tree
        registerTile(41, "tree/tree", "11111/11111/11111/11111/11111");

        // Event Tiles
        registerTile(42, "path/path_pit", "00000/00000/00000/00000/00000");
        registerTile(43, "grass/grass_pit", "00000/00000/00000/00000/00000");
        registerTile(44, "grass/grass_healing", "00000/00000/00000/00000/00000");
        registerTile(45, "coiner's_hut", "00000/00000/00000/00000/00000");

        // Dark Tiles
        registerTile(46, "tree/dark_tree", "11111/11111/11111/11111/11111");
        registerTile(47, "grass/dark_grass", "00000/00000/00000/00000/00000");

        registerTile(48, "tunnel_door", "00000/00000/00000/00000/00000");
    }

    /**
     * Registers a new tile instance into the {@code tileTypes} HashMap with its own index and image.
     * 
     * @param i The index of the tile. Used mainly for two things: getting the specific tile from the {@code tileTypes} HashMap,
     * or is used in a map file to load in the tile of the specified index.
     * <p>
     * @param imageName The name of the image that will be used for the tile.
     * The image is obtained from {@code /drawable/tiles} and must end with {@code .png}.
     * In the case that something goes wrong, the {@code disabled.png} image will be used for the tile.
     * <p>
     * @param collision Determines if you can go through the tile, or not.
     */
    public static void registerTile(int i, String imageName, String collision) {
        // Create a new tile and add it to the tileTypes HashMap, set it's collision, and load the image
        boolean[][] collisionArray = new boolean[5][5];
        for (int row = 0; row < collision.split("/").length; row++) {
            for (int col = 0; col < collision.split("/")[row].length(); col++) {
                collisionArray[row][col] = collision.split("/")[row].charAt(col) == '1';
            }
        }
        System.out.println(Arrays.toString(Arrays.stream(collisionArray).toArray()));
        Tile tile = new Tile(Image.loadImage("tile/" + imageName), collisionArray);
        tileTypes.put(i, tile);
    }

    public static void draw(Graphics graphics) {
        float camX = Main.game.player.camX;
        float camY = Main.game.player.camY;
        int tileSize = Main.game.tileSize;

        if (!maps.containsKey(Main.game.currentMap)) {
            Main.LOGGER.error("Map '{}' not found", Main.game.currentMap);
            return;
        }
        Map map = maps.get(Main.game.currentMap);

        // Draw the ground
        IntStream.range(0, map.x()).parallel().forEach(worldRow ->
                IntStream.range(0, map.y()).parallel().forEach(worldCol -> {
                    int tileNumber = map.ground().get(new Pair(worldCol, worldRow));
                    int worldX = worldCol * tileSize;
                    int worldY = worldRow * tileSize;
                    float screenX = worldX - camX + Main.game.screenWidth / 2f;
                    float screenY = worldY - camY + Main.game.screenHeight / 2f;

                    // Check if the tile is within the visible screen
                    if (worldX + tileSize > camX - Main.game.screenWidth / 2f &&
                            worldX - tileSize < camX + Main.game.screenWidth / 2f &&
                            worldY + tileSize > camY - Main.game.screenHeight / 2f &&
                            worldY - tileSize < camY + Main.game.screenHeight / 2f) {
                        Tile currentTile = tileTypes.get(tileNumber);
                        graphics.drawImage(currentTile.image().getImage(), Math.round(screenX), Math.round(screenY), null);
                    }
                })
        );

        // Draw the foreground
        IntStream.range(0, map.x()).parallel().forEach(worldRow ->
                IntStream.range(0, map.y()).parallel().forEach(worldCol -> {
                    int tileNumber = map.foreground().get(new Pair(worldCol, worldRow));
                    int worldX = worldCol * tileSize;
                    int worldY = worldRow * tileSize;
                    float screenX = worldX - camX + Main.game.screenWidth / 2f;
                    float screenY = worldY - camY + Main.game.screenHeight / 2f;

                    // Check if the tile is within the visible screen
                    if (worldX + tileSize > camX - Main.game.screenWidth / 2f &&
                            worldX - tileSize < camX + Main.game.screenWidth / 2f &&
                            worldY + tileSize > camY - Main.game.screenHeight / 2f &&
                            worldY - tileSize < camY + Main.game.screenHeight / 2f) {
                        Tile currentTile = tileTypes.get(tileNumber);
                        graphics.drawImage(currentTile.image().getImage(), Math.round(screenX), Math.round(screenY), null);
                    }
                })
        );
    }

    record Pair(int x, int y) {
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
