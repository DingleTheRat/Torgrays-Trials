// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.Game;

import java.util.HashMap;

public class TileManager {
    public static HashMap<Integer, Tile> tileTypes = new HashMap<>();
    public static HashMap<String, Map> maps = new HashMap<>();

    public static void loadTiles() {
        registerTile(0, "nothing", false);

        // Grass
        registerTile(10, "grass/grass_1", false);
        registerTile(11, "grass/grass_2", false);

        // Water
        registerTile(12, "water/water", false);
        registerTile(13, "water/white_line_water", false);
        registerTile(14, "water/water_corner_1", "00000/01111/01111/01111/01111");
        registerTile(19, "water/water_corner_2", "01111/01111/01111/01111/00000");
        registerTile(16, "water/water_corner_3", "00000/11110/11110/11110/11110");
        registerTile(21, "water/water_corner_4", "11110/11110/11110/11110/00000");
        registerTile(20, "water/water_edge_1", "11111/11111/11111/11111/00000");
        registerTile(18, "water/water_edge_2", "11110/11110/11110/11110/11110");
        registerTile(15, "water/water_edge_3", "00000/11111/11111/11111/11111");
        registerTile(17, "water/water_edge_4", "01111/01111/01111/01111/01111");
        registerTile(22, "water/water_outer_corner_1", "11111/11111/11111/11111/11110");
        registerTile(23, "water/water_outer_corner_2", "11111/11111/11111/11111/01111");
        registerTile(24, "water/water_outer_corner_3", "11110/11111/11111/11111/11111");
        registerTile(25, "water/water_outer_corner_4", "01111/11111/11111/11111/11111");

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
        registerTile(41, "tree/tree", "01110/11111/11111/11111/01110");

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
     * @param collision Determines which parts of the tile are solid.
     * Each side contains 4 colliders, put 1 to enable it, 0 to disable it.
     * You need to input the values for all sides that go left, right, top, and bottom. A slash must divide each side.
     * An example would be {@code 11111/11111/11111/11111/11111} for a solid tile.
     **/
    public static void registerTile(int i, String imageName, String collision) {
        // Create a new tile and add it to the tileTypes HashMap, set its collision, and load the image
        boolean[][] collisionArray = new boolean[5][5];
        for (int row = 0; row < collision.split("/").length; row++) {
            for (int col = 0; col < collision.split("/")[row].length(); col++) {
                collisionArray[col][row] = collision.split("/")[row].charAt(col) == '1';
            }
        }
        // Load the image and scale it to the tileSize
        Image image = Image.loadImage("tile/" + imageName);
        image.scaleImage(Game.tileSize, Game.tileSize);

        // Register the tile
        Tile tile = new Tile(image, collisionArray);
        tileTypes.put(i, tile);
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
    public static void registerTile(int i, String imageName, boolean collision) {
        registerTile(i, imageName, collision ? "11111/11111/11111/11111/11111" : "00000/00000/00000/00000/00000");
    }

    public static void draw() {
        if (!maps.containsKey(Main.game.currentMap)) {
            Main.LOGGER.error("Map '{}' not found", Main.game.currentMap);
            return;
        }
        Map map = maps.get(Main.game.currentMap);

        Main.batch.begin();

        // Draw the ground
        drawLayer(map, map.ground());
        drawLayer(map, map.foreground());

        Main.batch.end();
    }

    private static void drawLayer(Map map, HashMap<Pair, Integer> layer) {
        float camX = Main.game.player.cameraX;
        float camY = Main.game.player.cameraY;
        int tileSize = Game.tileSize;
        for (int worldRow = 0; worldRow < map.y(); worldRow++) {
            for (int worldCol = 0; worldCol < map.x(); worldCol++) {
                int tileNumber = layer.get(new Pair(worldCol, worldRow));
                int worldX = worldCol * tileSize;
                int worldY = worldRow * tileSize;
                float screenX = worldX - camX + Game.screenWidth / 2f;
                float screenY = worldY - camY + Game.screenHeight / 2f;

                // Check if the tile is within the visible screen
                if (worldX + tileSize > camX - Game.screenWidth / 2f &&
                    worldX - tileSize < camX + Game.screenWidth / 2f &&
                    worldY + tileSize > camY - Game.screenHeight / 2f &&
                    worldY - tileSize < camY + Game.screenHeight / 2f) {
                    Tile currentTile = TileManager.tileTypes.get(tileNumber);
                    Main.batch.draw(currentTile.image().getTexture(), Math.round(screenX), Math.round(screenY));
                }
            }
        }
    }

    public record Pair(int x, int y) {
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
