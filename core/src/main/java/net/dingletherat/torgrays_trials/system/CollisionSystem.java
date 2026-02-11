// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import java.util.Optional;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.component.*;
import net.dingletherat.torgrays_trials.rendering.Map;

public class CollisionSystem implements System {
    // Check if two entities are colliding
    public static boolean check2EntityCollision(Integer entityA, Integer entityB) {
        // Declare the necessary components as optionals (so we can check if they are present)
        Optional<CollisionComponent> componentA = EntityHandler.getComponent(entityA, CollisionComponent.class);
        Optional<CollisionComponent> componentB = EntityHandler.getComponent(entityB, CollisionComponent.class);
        Optional<PositionComponent> positionComponentA = EntityHandler.getComponent(entityA, PositionComponent.class);
        Optional<PositionComponent> positionComponentB = EntityHandler.getComponent(entityB, PositionComponent.class);

        // Check if the entities have the necessary components present
        if (componentA.isEmpty() || componentB.isEmpty()) return false;
        if (positionComponentA.isEmpty() || positionComponentB.isEmpty()) return false;


        float aLeft = positionComponentA.get().x;
        float aTop = positionComponentA.get().y;
        float aRight = positionComponentA.get().x + componentA.get().width;
        float aBottom = positionComponentA.get().y + componentA.get().height;

        float bLeft = positionComponentB.get().x;
        float bTop = positionComponentB.get().y;
        float bRight = positionComponentB.get().x + componentB.get().width;
        float bBottom = positionComponentB.get().y + componentB.get().height;

        return aLeft < bRight && aRight > bLeft && aTop < bBottom && aBottom > bTop;
    }

    public static boolean checkBlockCollision(Integer entity, boolean[][] collisionPoints, float x, float y) {
        // Declare the necessary components as optionals (so we can check if they are present)
        Optional<CollisionComponent> component = EntityHandler.getComponent(entity, CollisionComponent.class);
        Optional<PositionComponent> positionComponent = EntityHandler.getComponent(entity, PositionComponent.class);

        // Check if the entity has the necessary components present
        if (component.isEmpty() || positionComponent.isEmpty()) return false;

        int gridX = collisionPoints.length;      // number of columns in collision grid
        int gridY = collisionPoints[0].length;   // number of rows in collision grid
        float cellW = Main.tileSize / (float) gridX;
        float cellH = Main.tileSize / (float) gridY;

        for (int i = 0; i < gridX; i++) {
            for (int j = 0; j < gridY; j++) {
                if (collisionPoints[i][j]) {
                    float halfW = component.get().width / 2f;
                    float halfH = component.get().height / 2f;

                    float px = x + i * cellW + cellW / 2f;
                    float py = y + j * cellH + cellH / 2f;
                    if (Math.abs(px - positionComponent.get().x) <= halfW && Math.abs(py - positionComponent.get().y) <= halfH) return true;
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
        if (EntityHandler.getComponent(entity, PlayerComponent.class).isEmpty())
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
