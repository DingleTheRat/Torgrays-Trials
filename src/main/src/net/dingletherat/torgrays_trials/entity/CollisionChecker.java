// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.rendering.Map;
import net.dingletherat.torgrays_trials.rendering.TileManager;

public class CollisionChecker {
	// Check if two entities are colliding
	public static boolean check2EntityCollision(Entity a, Entity b) {
		// treat a.x/a.y and b.x/b.y as centers
		float halfW = (a.width + b.width) / 2f;
		float halfH = (a.height + b.height) / 2f;
		return Math.abs(a.x - b.x) <= halfW && Math.abs(a.y - b.y) <= halfH;
	}
	
	// Check if an entity is colliding with a block defined by collisionPoints at world position (x, y)
	public static boolean checkBlockCollision(Entity e, boolean[][] collisionPoints, float x, float y) {
		// tile positions are from the top-left corner; use a tile center for broad-phase
		float tileHalfW = Main.game.tileSize / 2f;
		float tileCenterX = x + tileHalfW;
		float tileCenterY = y + tileHalfW;
		
		float xDiff = Math.abs(e.x - tileCenterX);
		float yDiff = Math.abs(e.y - tileCenterY);
		
		// use half-extents for the entity in the broad-phase test
		if (xDiff <= e.width / 2f + tileHalfW && yDiff <= e.height /2f + tileHalfW) {
			int gridX = collisionPoints.length;
			int gridY = collisionPoints[0].length;
			float cellW = Main.game.tileSize / (float) gridX;
			float cellH = Main.game.tileSize / (float) gridY;
			for (int i = 0; i < gridX; i++) {
				for (int j = 0; j < gridY; j++) {
					if (collisionPoints[i][j]) {
						float halfW = e.width / 2f;
						float halfH = e.height / 2f;
						// compute a world-space center of the subcell (tile top-left + cell offset)
						float px = x + i * cellW + cellW / 2f;
						float py = y + j * cellH + cellH / 2f;
						if (Math.abs(px - e.x) <= halfW && Math.abs(py - e.y) <= halfH) return true;
					}
				}
			}
		}
		return false;
	}
	
	// Check if an entity is colliding with any other entity or block
	public static boolean checkEntityColliding(Entity e) {
		for (Entity other : Main.game.entities) {
			if (other != e && other.collision) {
				if (check2EntityCollision(e, other)) {
					return true;
				}
			}
		}
		Map map = TileManager.maps.get(Main.game.currentMap);
		for (int row = 0; row < map.y(); row++) {
			for (int col = 0; col < map.x(); col++) {
				int tileNumber = map.foreground().get(new TileManager.Pair(col, row));
				boolean[][] collisionPoints = TileManager.tileTypes.get(tileNumber).collision();
				int worldX = col * Main.game.tileSize - Main.game.tileSize / 2;
				int worldY = row * Main.game.tileSize - Main.game.tileSize / 2;
				if (checkBlockCollision(e, collisionPoints, worldX, worldY)) return true;
			}
		}
		return false;
	}
}
