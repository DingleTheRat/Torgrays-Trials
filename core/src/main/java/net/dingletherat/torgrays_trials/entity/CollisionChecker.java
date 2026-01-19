// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.rendering.Map;
import net.dingletherat.torgrays_trials.rendering.TileManager;

public class CollisionChecker {
    // Check if two entities are colliding
    public static boolean check2EntityCollision(Entity entity1, Entity entity2) {
        float aLeft = entity1.x;
        float aTop = entity1.y;
        float aRight = entity1.x + entity1.width;
        float aBottom = entity1.y + entity1.height;

        float bLeft = entity2.x;
        float bTop = entity2.y;
        float bRight = entity2.x + entity2.width;
        float bBottom = entity2.y + entity2.height;

        return aLeft < bRight && aRight > bLeft && aTop < bBottom && aBottom > bTop;
    }

    public static boolean checkBlockCollision(Entity entity, boolean[][] collisionPoints, float x, float y) {
        int gridX = collisionPoints.length;      // number of columns in collision grid
        int gridY = collisionPoints[0].length;   // number of rows in collision grid
        float cellW = Main.tileSize / (float) gridX;
        float cellH = Main.tileSize / (float) gridY;

        for (int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++) {
                if (collisionPoints[i][j]) {
                    float halfW = entity.width / 2f;
                    float halfH = entity.height / 2f;

                    float px = x + i * cellW + cellW / 2f;
                    float py = y + j * cellH + cellH / 2f;
                    if (Math.abs(px - entity.x) <= halfW && Math.abs(py - entity.y) <= halfH) return true;
                }
            }
        }

        return false;
    }

    // Check if entity collides with any other entity or any tile on layer2
    public static boolean checkEntityColliding(Entity entity) {
        for (Entity other : Main.world.entities) {
            if (other != entity && other.collision) {
                if (check2EntityCollision(entity, other)) return true;
            }
        }

        // Check collision with player too (if it's not the player)
        if (!(entity instanceof Player))
            if (check2EntityCollision(entity, Main.world.player)) return true;

        Map map = TileManager.maps.get(Main.world.currentMap);
        if (map == null) return false;

        for (int row = 0; row < map.y(); row++) {
            for (int col = 0; col < map.x(); col++) {
                int tileNumber = map.foreground().get(new TileManager.Pair(col, row));
                var tile = TileManager.tileTypes.get(tileNumber);
                if (tile == null) continue;

                boolean[][] collisionPoints = tile.collision();
                int worldX = col * Main.tileSize - Main.tileSize / 2; // tile top-left X
                int worldY = row * Main.tileSize - Main.tileSize / 2; // tile top-left Y

                if (checkBlockCollision(entity, collisionPoints, worldX, worldY)) return true;
            }
        }

        return false;
    }
}

