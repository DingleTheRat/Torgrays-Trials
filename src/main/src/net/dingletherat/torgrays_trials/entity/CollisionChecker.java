// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.rendering.Map;
import net.dingletherat.torgrays_trials.rendering.TileManager;

public class CollisionChecker {
	public static boolean check2EntityCollision(Entity a, Entity b) {
		return a.x < b.x + b.width &&
				a.x + a.width > b.x &&
				a.y < b.y + b.height &&
				a.y + a.height > b.y;
	}
	
	public static boolean checkBlockCollision(Entity e, boolean[][] collisionPoints, float x, float y) {
		float xDiff = Math.abs(e.x - x);
		float yDiff = Math.abs(e.y - y);
		if (xDiff < e.width + 16 && yDiff < e.height + 16) {
			for (int i = 0; i < collisionPoints.length; i++) {
				for (int j = 0; j < collisionPoints[i].length; j++) {
					if (collisionPoints[i][j]) {
						float pointX = e.x + (i * (e.width / collisionPoints.length));
						float pointY = e.y + (j * (e.height / collisionPoints[i].length));
						if (pointX - x < e.width && pointX - x > 0 &&
							pointY - y < e.height && pointY - y > 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean checkEntityColliding(Entity e) {
		for (Entity other : Main.game.entities) {
			if (other != e) {
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
				int worldX = col * Main.game.tileSize;
				int worldY = row * Main.game.tileSize;
				if (checkBlockCollision(e, collisionPoints, worldX, worldY)) {
					return true;
				}
			}
		}
		return false;
	}
}
