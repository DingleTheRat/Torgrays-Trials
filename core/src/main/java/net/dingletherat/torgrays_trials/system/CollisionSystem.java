// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.component.*;
import net.dingletherat.torgrays_trials.rendering.Map;

public class CollisionSystem implements System {
    // Check if two entities are colliding
    public static boolean check2EntityCollision(Integer entityA, Integer entityB) {
        // Make sure the entites have the necessary components
        if (!EntityHandler.entityHasComponent(entityA, PositionComponent.class) ||
                !EntityHandler.entityHasComponent(entityB, PositionComponent.class)) return false;
        if (!EntityHandler.entityHasComponent(entityA, CollisionComponent.class) ||
                !EntityHandler.entityHasComponent(entityB, CollisionComponent.class)) return false;

        // Declare components
        CollisionComponent componentA = EntityHandler.getEntityComponent(entityA, CollisionComponent.class).get();
        CollisionComponent componentB = EntityHandler.getEntityComponent(entityB, CollisionComponent.class).get();
        PositionComponent positionComponentA = EntityHandler.getEntityComponent(entityA, PositionComponent.class).get();
        PositionComponent positionComponentB = EntityHandler.getEntityComponent(entityB, PositionComponent.class).get();

        float aLeft = positionComponentA.x;
        float aTop = positionComponentA.y;
        float aRight = positionComponentA.x + componentA.width;
        float aBottom = positionComponentA.y + componentA.height;

        float bLeft = positionComponentB.x;
        float bTop = positionComponentB.y;
        float bRight = positionComponentB.x + componentB.width;
        float bBottom = positionComponentB.y + componentB.height;

        return aLeft < bRight && aRight > bLeft && aTop < bBottom && aBottom > bTop;
    }

    public static boolean checkBlockCollision(Integer entity, boolean[][] collisionPoints, float x, float y) {
        // Make sure the entites have the necessary components
        if (!EntityHandler.entityHasComponent(entity, PositionComponent.class)) return false;
        if (!EntityHandler.entityHasComponent(entity, CollisionComponent.class)) return false;

        // Declare the components
        CollisionComponent component = EntityHandler.getEntityComponent(entity, CollisionComponent.class).get();
        PositionComponent positionComponent = EntityHandler.getEntityComponent(entity, PositionComponent.class).get();

        int gridX = collisionPoints.length;      // number of columns in collision grid
        int gridY = collisionPoints[0].length;   // number of rows in collision grid
        float cellW = Main.tileSize / (float) gridX;
        float cellH = Main.tileSize / (float) gridY;

        for (int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++) {
                if (collisionPoints[i][j]) {
                    float halfW = component.width / 2f;
                    float halfH = component.height / 2f;

                    float px = x + i * cellW + cellW / 2f;
                    float py = y + j * cellH + cellH / 2f;
                    if (Math.abs(px - positionComponent.x) <= halfW && Math.abs(py - positionComponent.y) <= halfH) return true;
                }
            }
        }

        return false;
    }

    // Check if entity collides with any other entity or any tile on layer2
    public static boolean checkEntityColliding(Integer entity) {
        for (Integer other : EntityHandler.queryAll(CollisionComponent.class, PositionComponent.class)) {
            if (other != entity)
                if (check2EntityCollision(entity, other)) return true;
        }

        // Check collision with player too (if it's not the player)
        if (!EntityHandler.entityHasComponent(entity, PlayerComponent.class))
            if (check2EntityCollision(entity, Main.world.getPlayer())) return true;

        Map map = TileSystem.maps.get(Main.world.getMap());
        if (map == null) return false;

        for (int row = 0; row < map.y(); row++) {
            for (int col = 0; col < map.x(); col++) {
                int tileNumber = map.foreground().get(new TileSystem.Pair(col, row));
                var tile = TileSystem.tileTypes.get(tileNumber);
                if (tile == null) continue;

                boolean[][] collisionPoints = tile.collision();
                int worldX = col * Main.tileSize - Main.tileSize / 2; // tile top-left X
                int worldY = row * Main.tileSize - Main.tileSize / 2; // tile top-left Y

                if (checkBlockCollision(entity, collisionPoints, worldX, worldY)) return true;
            }
        }

        return false;
    }

    @Override
    public void draw() { }

    @Override
    public void update(float deltaTime) { }
}
