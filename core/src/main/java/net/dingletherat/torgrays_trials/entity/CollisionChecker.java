// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.rendering.Map;
import net.dingletherat.torgrays_trials.rendering.TileManager;

public class CollisionChecker {
    // Check if two entities are colliding
    public static boolean check2EntityCollision(Entity a, Entity b) {
        float aLeft = a.x;
        float aTop = a.y;
        float aRight = a.x + a.width;
        float aBottom = a.y + a.height;

        float bLeft = b.x;
        float bTop = b.y;
        float bRight = b.x + b.width;
        float bBottom = b.y + b.height;

        return aLeft < bRight && aRight > bLeft && aTop < bBottom && aBottom > bTop;
    }

    public static boolean checkBlockCollision(Entity e, boolean[][] collisionPoints, float x, float y) {
        int gridX = collisionPoints.length;      // number of columns in collision grid
        int gridY = collisionPoints[0].length;   // number of rows in collision grid
        float cellW = Game.tileSize / (float) gridX;
        float cellH = Game.tileSize / (float) gridY;

        for (int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++) {
                if (collisionPoints[i][j]) {
                    float halfW = e.width / 2f;
                    float halfH = e.height / 2f;

                    float px = x + i * cellW + cellW / 2f;
                    float py = y + j * cellH + cellH / 2f;
                    if (Math.abs(px - e.x) <= halfW && Math.abs(py - e.y) <= halfH) return true;
                }
            }
        }

        return false;
    }

    // Check if entity collides with any other entity or any tile on layer2
    public static boolean checkEntityColliding(Entity e) {
        for (Entity other : Main.game.entities) {
            if (other != e && other.collision) {
                if (check2EntityCollision(e, other)) return true;
            }
        }

        Map map = TileManager.maps.get(Main.game.currentMap);
        if (map == null) return false;

        for (int row = 0; row < map.y(); row++) {
            for (int col = 0; col < map.x(); col++) {
                int tileNumber = map.foreground().get(new TileManager.Pair(col, row));
                var tile = TileManager.tileTypes.get(tileNumber);
                if (tile == null) continue;

                boolean[][] collisionPoints = tile.collision();
                int worldX = col * Game.tileSize - Game.tileSize / 2; // tile top-left X
                int worldY = row * Game.tileSize - Game.tileSize / 2; // tile top-left Y

                if (checkBlockCollision(e, collisionPoints, worldX, worldY)) return true;
            }
        }

        return false;
    }
}

