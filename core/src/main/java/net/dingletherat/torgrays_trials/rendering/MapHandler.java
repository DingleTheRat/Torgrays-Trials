// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.dingletherat.torgrays_trials.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MapHandler {
	public static final HashMap<String, JSONObject> mapFiles = new HashMap<>();
	public static ArrayList<String> maps = new ArrayList<>();

	public static void loadMaps() {
		String[] mapFiles = getResourceFileNames("values/maps");
		for (String mapFile : mapFiles) {
			if (mapFile.endsWith(".json")) {
				String mapName = mapFile.substring(0, mapFile.lastIndexOf(".json"));
				loadMap(mapName);
			} else if (!mapFile.contains(".")) { // Check if it's a directory
				String[] subFiles = getResourceFileNames("values/maps/" + mapFile);
				for (String subFile : subFiles) {
					if (subFile.endsWith(".json")) {
						String mapName = subFile.substring(0, subFile.lastIndexOf(".json"));
						loadMap(mapFile + "/" + mapName);
					}
				}
			}
		}
		Main.LOGGER.info("Loaded {} map files", maps.toArray().length);
	}

	public static void loadMap(String fileName) {
		// Get the file and check if it exists
		JSONObject file = getJsonObject("values/maps/" + fileName + ".json");
		if (file == null) {
			Main.LOGGER.error("Couldn't find values/maps/{}.json", fileName);
			file = getJsonObject("maps/disabled.json");
			if (file == null) {
				Main.LOGGER.error("Couldn't find /values/maps/disabled.json");
				return;
			}
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
			Main.LOGGER.error("Failed to find essential map data in {}.json. Using default map.", fileName);
			file = getJsonObject("/maps/disabled.json");
			if (file == null) {
				Main.LOGGER.error("Couldn't find /values/maps/disabled.json after choosing default map.");
				return;
			}
			name = file.getString("name");
			map = file.getJSONObject("map");
			ground = map.getJSONArray("ground");
		}

		JSONArray foreground = null;
		if (map.has("foreground")) {
			foreground = map.getJSONArray("foreground");
		}

		int mapHeight = ground.length();
		String[] numbers = ground.getString(0).split(" ");
		int mapWidth = numbers.length;
		Map mapClass = new Map(new HashMap<>(), new HashMap<>(), mapWidth, mapHeight);

		// Load the ground from the ground variable
		int row = 0;
		while (row < mapHeight) {
			numbers = ground.getString(row).split(" ");
			int col = 0;
			while (col < mapWidth) {
				int number = Integer.parseInt(numbers[col]);
				mapClass.ground().put(new TileManager.Pair(col, row), number);

				// If the tile number is not registered, register it as a disabled tile
				if (TileManager.tileTypes.get(number) == null) {
					Main.LOGGER.warn("Index {} is not a valid tile in {} map ground.", number, name);
					TileManager.registerTile(number, "", "00000/00000/00000/00000/00000");
				}
				col++;
			}
			row++;
		}

		// Load the foreground from the ground variable
		if (foreground != null) {
			row = 0;
			while (row < mapHeight) {
				numbers = foreground.getString(row).split(" ");
				int col = 0;
				while (col < mapWidth) {
					int number = Integer.parseInt(numbers[col]);
					mapClass.foreground().put(new TileManager.Pair(col, row), number);

					// If the tile number is not registered, register it as a disabled tile
					if (TileManager.tileTypes.get(number) == null) {
						Main.LOGGER.error("Index {} is not a valid tile in {} map foreground.", number, name);
						TileManager.registerTile(number, "", "00000|00000|00000|00000|00000");
					}
					col++;
				}
				row++;
			}
		}


		// Add to some useful HashMap and Array List
		TileManager.maps.put(name, mapClass);
		mapFiles.put(name, file);
		maps.add(name);
	}

	public static String[] getResourceFileNames(String directoryPath) {
        // This is the LibGDX way of getting files
        FileHandle dir = Gdx.files.internal(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            Main.LOGGER.warn("Invalid directory: {}", directoryPath);
            return new String[0];
        }

        return Arrays.stream(dir.list())
            .map(FileHandle::name)
            .toArray(String[]::new);
	}

    public static JSONObject getJsonObject(String path) {
        try {
            FileHandle file = Gdx.files.internal(path);

            if (!file.exists()) {
                Main.LOGGER.error("Warning: \"{}\" is not a valid path.", path);
                return null;
            }

            String content = file.readString("UTF-8");
            return new JSONObject(content);

        } catch (Exception e) {
            Main.handleException(e);
            return new JSONObject();
        }
    }
}
