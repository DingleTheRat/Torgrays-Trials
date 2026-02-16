// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import java.util.List;
import java.util.Optional;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.PlayerComponent;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.component.area.AreaComponent;
import net.dingletherat.torgrays_trials.component.area.CollisionComponent;
import net.dingletherat.torgrays_trials.rendering.Map;
import net.dingletherat.torgrays_trials.system.TileSystem;

public class AreaChecker {
    // Check if two entities are colliding
    public static boolean checkAreasIntersecting(AreaComponent componentA, PositionComponent positionComponentA, AreaComponent componentB, PositionComponent positionComponentB) {
        float aLeft = positionComponentA.x + componentA.offsetX;
        float aTop = positionComponentA.y + componentA.offsetY;
        float aRight = positionComponentA.x + componentA.offsetX + componentA.width;
        float aBottom = positionComponentA.y + componentA.offsetY + componentA.height;

        float bLeft = positionComponentB.x + componentB.offsetX;
        float bTop = positionComponentB.y + componentB.offsetY;
        float bRight = positionComponentB.x + componentB.offsetX + componentB.width;
        float bBottom = positionComponentB.y + componentB.offsetY + componentB.height;

        return aLeft < bRight && aRight > bLeft && aTop < bBottom && aBottom > bTop;
    }

    public static boolean check2EntityIntersecting(Integer entity, Integer other, Class<? extends AreaComponent> componentClass, boolean matchClass) {
        // Get the positionComponent, making sure there is one. If not, then just ignore this entity like doesn't exist :D
        PositionComponent positionComponent = EntityHandler.getComponent(entity, PositionComponent.class).orElse(null);
        if (positionComponent == null) return false;

        // Loop through all the areaComponents in the entity to preform checks for each
        for (AreaComponent areaComponent : EntityHandler.getComponents(entity, componentClass)) {

            // Do the same position check for the other entity
            PositionComponent otherPosition = EntityHandler.getComponent(other, PositionComponent.class).orElse(null);
            if (otherPosition == null) continue;

            // Finally, loop through the other entity's areas and see if one is intersecting with the current one
            List<? extends AreaComponent> otherAreas = matchClass ?
                EntityHandler.getComponents(other, componentClass) : EntityHandler.getComponents(other, AreaComponent.class);
            for (AreaComponent otherArea : otherAreas) {
                if (checkAreasIntersecting(areaComponent, positionComponent, otherArea, otherPosition)) return true;
            }
        }

        return false;
    }

    /**
     * Checks if the entity is on screen or not.
     * <p>
     * The entity that is being checked must have a {@link PositionComponent}, or else it will return true.
     * If the entity doesn't have any area components, the calculation will just use its position.
     * However, if it does, it will loop through each one and see if it's on the screen, while considering its width and height. If one is, it will return true.
     * <p>
     * @param entity The entity that is being checked.
     * @return True if the entity is on screen (or doesn't have and {@link PositionComponent}), false if not.
     **/
    public static boolean checkVisibility(Integer entity) {
        // Declare the PositionComponent and check if it exists (if not, return true)
        PositionComponent positionComponent = EntityHandler.getComponent(entity, PositionComponent.class).orElse(null);
        if (positionComponent == null) return true;

        List<AreaComponent> areaComponents = EntityHandler.getComponents(entity, AreaComponent.class);

        // Check if the entity's position (and maybe area components) are in the camera. If it is, or one areaComponent is, return true
        // If there's no areaComponents, simply use the position in the calculation
        if (areaComponents.isEmpty()) {
            if (positionComponent.x > Main.world.cameraX - Main.screenWidth / 2f &&
                positionComponent.x < Main.world.cameraX + Main.screenWidth / 2f &&
                positionComponent.y > Main.world.cameraY - Main.screenHeight / 2f &&
                positionComponent.y < Main.world.cameraY + Main.screenHeight / 2f) return true;
            return false;
        }

        // Otherwise, loop through each areaComponent and see if they are in the camera, taking the width, height, ect.. into consideration
        for (AreaComponent areaComponent : areaComponents) {
            if (positionComponent.x + areaComponent.offsetX + areaComponent.width > Main.world.cameraX - Main.screenWidth / 2f &&
                positionComponent.x + areaComponent.offsetX < Main.world.cameraX + Main.screenWidth / 2f &&
                positionComponent.y + areaComponent.offsetY + areaComponent.height > Main.world.cameraY - Main.screenHeight / 2f &&
                positionComponent.y + areaComponent.offsetY < Main.world.cameraY + Main.screenHeight / 2f) return true;
        }
        return false;
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
        for (Integer other : EntityHandler.queryAll(CollisionComponent.class, PositionComponent.class))
            if (other != entity)
                if (check2EntityIntersecting(entity, other, CollisionComponent.class, true)) return true;

        // Check collision with player too (if it's not the player)
        if (EntityHandler.getComponent(entity, PlayerComponent.class).isEmpty())
            if (check2EntityIntersecting(entity, Main.world.getPlayer(), CollisionComponent.class, true)) return true;

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

}
