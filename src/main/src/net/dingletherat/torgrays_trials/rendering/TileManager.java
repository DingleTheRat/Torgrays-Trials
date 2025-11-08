// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import net.dingletherat.torgrays_trials.main.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.IntStream;

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
        // Create a new tile and add it to the tileTypes HashMap, as well as store it in a field for later use.
        Tile tile = new Tile();
        tile.collision = collision;
        tileTypes.put(i, tile);

        /* Entering try and catch zone because this part involves ImageIO.
         The catch also has a NullPointerException because "requireNonNull" is used in the code*/
        try {
            // Get the imageStream that we will use for the tile. It's separately instantiated as it will be used for null checking
            InputStream imageStream = TileManager.class.getResourceAsStream("/drawable/tile/" + imageName + ".png");

            /* Make sure the imageStream is a member of "/drawable/tile/" and is a png. If not, use the disabled imageStream and warn.
             * The way we find out that is by checking if the imageStream is null. If it is, it's likely not a valid member.
            However, if the imageName was just "", don't warn as it may be a result of an error */
            if (imageName.isEmpty()) {
                tile.image = ImageIO.read(Objects.requireNonNull(
                        TileManager.class.getResourceAsStream("/drawable/disabled.png")));
                return;
            }
            if (imageStream == null) {
                Main.LOGGER.warn(imageName + " is not a valid member of \"/drawable/tiles\". ");
                tile.image = ImageIO.read(Objects.requireNonNull(
                        TileManager.class.getResourceAsStream("/drawable/disabled.png")));
                return;
            }

            // If all checks have passed, then set the tile's image to a scaled version of the imageStream
            tile.image = Images.scaleImage(ImageIO.read(imageStream), Main.game.tileSize, Main.game.tileSize);
        } catch (IOException | NullPointerException exception) {
            Main.handleException(exception);
        }
    }

    public static void draw(Graphics graphics) {
        float playerX = Main.game.player.x;
        float playerY = Main.game.player.y;
        float playerScreenX = Main.game.player.screenX;
        float playerScreenY = Main.game.player.screenY;
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
                    float screenX = worldX - playerX + playerScreenX;
                    float screenY = worldY - playerY + playerScreenY;

                    // Check if the tile is within the visible screen
                    if (worldX + tileSize > playerX - playerScreenX &&
                            worldX - tileSize < playerX + playerScreenX &&
                            worldY + tileSize > playerY - playerScreenY &&
                            worldY - tileSize < playerY + playerScreenY) {
                        Tile currentTile = tileTypes.get(tileNumber);
                        graphics.drawImage(currentTile.image, Math.round(screenX), Math.round(screenY), null);
                    }
                })
        );

        // Draw the foreground
        IntStream.range(0, map.x()).parallel().forEach(worldRow ->
                IntStream.range(0, map.y()).parallel().forEach(worldCol -> {
                    int tileNumber = map.foreground().get(new Pair(worldCol, worldRow));
                    int worldX = worldCol * tileSize;
                    int worldY = worldRow * tileSize;
                    float screenX = worldX - playerX + playerScreenX;
                    float screenY = worldY - playerY + playerScreenY;

                    // Check if the tile is within the visible screen
                    if (worldX + tileSize > playerX - playerScreenX &&
                            worldX - tileSize < playerX + playerScreenX &&
                            worldY + tileSize > playerY - playerScreenY &&
                            worldY - tileSize < playerY + playerScreenY) {
                        Tile currentTile = tileTypes.get(tileNumber);
                        graphics.drawImage(currentTile.image, Math.round(screenX), Math.round(screenY), null);
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
