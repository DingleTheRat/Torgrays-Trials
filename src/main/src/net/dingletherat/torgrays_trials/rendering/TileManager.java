package net.dingletherat.torgrays_trials.rendering;

import net.dingletherat.torgrays_trials.main.Main;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.IntStream;

public class TileManager {
	public static HashMap<Integer, Tile> tileTypes = new HashMap<>();
	public static HashMap<String, Map> maps = new HashMap<>();
	
	public static void setup() {
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
			tileTypes.put(i, new Tile());
			try {
				tileTypes.get(i).image = ImageIO.read(Objects.requireNonNull(TileManager.class
						.getResourceAsStream("/drawable/tile/" + imageName + ".png")));
			} catch (IllegalArgumentException e) {
				// If the image is not found, use the disabled image and print a warning
				System.err.println("\"" + imageName + "\" is not a valid path.");
				tileTypes.get(i).image = ImageIO.read(Objects.requireNonNull(
						TileManager.class.getResourceAsStream("/drawable/disabled.png")));
				Main.handleException(e);
			}
			tileTypes.get(i).image = scaleImage(tileTypes.get(i).image, Main.game.tileSize, Main.game.tileSize);
			tileTypes.get(i).collision = collision;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
		BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = scaledImage.createGraphics();
		graphics2D.drawImage(original, 0, 0, width, height, null);
		graphics2D.dispose();
		
		return scaledImage;
	}
	
	public static void draw(Graphics g) {
		float playerWorldX = 10;  // Main.game.player.worldX;
		float playerWorldY = 10;  // Main.game.player.worldY;
		int playerScreenX = 10;  // Main.game.player.screenX;
		int playerScreenY = 10;  // Main.game.player.screenY;
		int tileSize = Main.game.tileSize;
		if (!maps.containsKey(Main.game.currentMap)) {
			Main.LOGGER.error("Map '{}' not found", Main.game.currentMap);
			return;
		}
		Map map = maps.get(Main.game.currentMap);
		
		IntStream.range(0, Main.game.maxWorldRow).parallel().forEach(worldRow ->
			IntStream.range(0, Main.game.maxWorldCol).parallel().forEach(worldCol -> {
				int tileNumber = map.ground().get(new Pair(worldCol, worldRow));
				int worldX = worldCol * tileSize;
				int worldY = worldRow * tileSize;
				float screenX = worldX - playerWorldX + playerScreenX;
				float screenY = worldY - playerWorldY + playerScreenY;
				
				// Check if the tile is within the visible screen
				if (worldX + tileSize > playerWorldX - playerScreenX &&
						worldX - tileSize < playerWorldX + playerScreenX &&
						worldY + tileSize > playerWorldY - playerScreenY &&
						worldY - tileSize < playerWorldY + playerScreenY) {
					Tile currentTile = tileTypes.get(tileNumber);
					g.drawImage(currentTile.image, Math.round(screenX), Math.round(screenY), null);
				}
			}
		));
	}
	
	record Pair(int x, int y) {}
}