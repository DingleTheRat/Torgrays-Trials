package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.tile.TileManager;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.util.ArrayList;
import java.util.HashMap;

public class CollisionChecker {
    public static final HashMap<String, Boolean> firstHitMap = new HashMap<>();
    public static final HashMap<String, Boolean> wasInMap = new HashMap<>();

    public static void checkTile(Mob mob) {
        if (TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, 0, 0)) == null) return;

        // World coordinates of the mob's solid area
        int mobLeftWorldX = mob.worldX + mob.solidArea.x;
        int mobRightWorldX = mob.worldX + mob.solidArea.x + mob.solidArea.width;
        int mobTopWorldY = mob.worldY + mob.solidArea.y;
        int mobBottomWorldY = mob.worldY + mob.solidArea.y + mob.solidArea.height;

        // Tile coordinates of the mob's solid area'
        int mobLeftCol = mobLeftWorldX / Main.game.tileSize;
        int mobRightCol = mobRightWorldX / Main.game.tileSize;
        int mobTopRow = mobTopWorldY / Main.game.tileSize;
        int mobBottomRow = mobBottomWorldY / Main.game.tileSize;

        int tileNumber1, tileNumber2;

        if (mob.direction.contains("up")) {
            mobTopRow = (mobTopWorldY - mob.speed) / Main.game.tileSize;
            tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
            tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobTopRow));
            if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) mob.colliding = true;
        }
        if (mob.direction.contains("down")) {
            mobBottomRow = (mobBottomWorldY + mob.speed) / Main.game.tileSize;
            tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobBottomRow));
            tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
            if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) mob.colliding = true;
        }
        if (mob.direction.contains("left")) {
            mobLeftCol = (mobLeftWorldX - mob.speed) / Main.game.tileSize;
            tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
            tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobBottomRow));
            if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) mob.colliding = true;
        }
        if (mob.direction.contains("right")) {
            mobRightCol = (mobRightWorldX + mob.speed) / Main.game.tileSize;
            tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
            tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
            if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) mob.colliding = true;
        }
    }

    public static <T extends Entity, V extends Entity> V checkEntity(T entity, ArrayList<V> targets) {
        V target = checkEntityCollision(entity, targets);
        checkEntityHit(entity, targets);
        return target;
    }

    /**
     * Checks if a given entity collides with any entity in a list of target entities.
     * If a collision is detected, the colliding target entity is returned, if not, then null is returned.
     *
     * @param <T> the type of the source entity being checked, which extends Entity
     * @param <V> the type of the target entities in the list, which extends Entity
     * @param entity the source entity whose collision is being checked
     * @param targets the list of target entities to check for collisions
     * @return the first target entity that the source entity collides with, or null if no collision is detected
     */
    public static <T extends Entity, V extends Entity> V checkEntityCollision(T entity, ArrayList<V> targets) {
        // Define the return variable, if the target is not found, it will return null
        V target = null;

        // If the target list is null or empty, return
        if (entity == null || targets == null || targets.isEmpty()) return target;

        // Save entity's original solid area positions
        int solidAreaDefaultX = entity.solidArea.x;
        int solidAreaDefaultY = entity.solidArea.y;

        // Move the solid area to the entity's current position
        entity.solidArea.x = entity.worldX + solidAreaDefaultX;
        entity.solidArea.y = entity.worldY + solidAreaDefaultY;

        // Adjust the solid area based on the entity's direction (If it's a Mob)
        if (entity instanceof Mob mob) {
            switch (mob.direction) {
                case "up" -> mob.solidArea.y -= mob.speed;
                case "down" -> mob.solidArea.y += mob.speed;
                case "left" -> mob.solidArea.x -= mob.speed;
                case "right" -> mob.solidArea.x += mob.speed;
            }
        }

        for (V checkedEntity : targets) {
            // Make sure the checked entity is not null and different from the entity
            if (checkedEntity == null || checkedEntity == entity) continue;

            // Save the checked entity's original solid area positions
            int checkedSolidAreaDefaultX = checkedEntity.solidArea.x;
            int checkedSolidAreaDefaultY = checkedEntity.solidArea.y;

            // Move the checked entity's solid area to its current position
            checkedEntity.solidArea.x = checkedEntity.worldX + checkedSolidAreaDefaultX;
            checkedEntity.solidArea.y = checkedEntity.worldY + checkedSolidAreaDefaultY;

            // Check if the solid areas intersect
            if (entity.solidArea.intersects(checkedEntity.solidArea)) {
                // If so, make them collide >:)
                if (checkedEntity.collision && entity.collision) {
                    entity.colliding = true;
                    checkedEntity.colliding = true;
                    target = checkedEntity;
                }
            }

            // Restore the solid area back to its default positions
            checkedEntity.solidArea.x = checkedSolidAreaDefaultX;
            checkedEntity.solidArea.y = checkedSolidAreaDefaultY;
        }

        // Restore the entity's solid area back to its default positions
        entity.solidArea.x = solidAreaDefaultX;
        entity.solidArea.y = solidAreaDefaultY;

        return target;
    }

    /**
     * Checks if a given entity's hit area intersects with the solid areas of a list of target entities.
     * If an intersection occurs, it triggers the appropriate hit-related actions for the entity.
     *
     * @param <T> the type of the source entity whose hit area is being checked, which extends Entity
     * @param <V> the type of the target entities in the list, which extends Entity
     * @param entity the source entity whose hit area is being checked
     * @param targets the list of target entities to check for intersections with the source entity's hit area
     */
    public static <T extends Entity, V extends Entity> void checkEntityHit(T entity, ArrayList<V> targets) {
        // If the target list is null or empty, return
        if (entity == null || targets == null || targets.isEmpty()) return;

        // Save entity's original hit area positions
        int hitAreaDefaultX = entity.hitArea.x;
        int hitAreaDefaultY = entity.hitArea.y;

        // Move the hit area to the entity's current position
        entity.hitArea.x = entity.worldX + hitAreaDefaultX;
        entity.hitArea.y = entity.worldY + hitAreaDefaultY;

        for (V checkedEntity : targets) {
            // Make sure the checked entity is not null and different from the entity
            if (checkedEntity == null || checkedEntity == entity) continue;

            // Save the checked entity's original solid area positions
            int checkedSolidAreaDefaultX = checkedEntity.solidArea.x;
            int checkedSolidAreaDefaultY = checkedEntity.solidArea.y;

            // Move the checked entity's solid area to its current position
            checkedEntity.solidArea.x = checkedEntity.worldX + checkedSolidAreaDefaultX;
            checkedEntity.solidArea.y = checkedEntity.worldY + checkedSolidAreaDefaultY;

            // Initialize hit state tracking for this entity-target pair if not present
            String key = entity.hashCode() + "-" + checkedEntity.hashCode();
            firstHitMap.putIfAbsent(key, true);
            wasInMap.putIfAbsent(key, false);

            // Check if the hit area intersects with the checkedEntity's solid area
            if (entity.hitArea.intersects(checkedEntity.solidArea)) {
                if (firstHitMap.get(key)) {
                    // Initial hit
                    entity.onHit(checkedEntity);
                    firstHitMap.put(key, false);
                    wasInMap.put(key, true);
                }
                // Continuous hit
                entity.whileHit(checkedEntity);
            } else if (wasInMap.get(key)) {
                // No longer intersecting
                entity.onLeave(checkedEntity);
                firstHitMap.put(key, true);
                wasInMap.put(key, false);
            }

            // Restore the checked entity's solid area position
            checkedEntity.solidArea.x = checkedSolidAreaDefaultX;
            checkedEntity.solidArea.y = checkedSolidAreaDefaultY;
        }

        // Restore the entity's hit area position
        entity.hitArea.x = hitAreaDefaultX;
        entity.hitArea.y = hitAreaDefaultY;
    }
    public static <T extends Entity> T getDetected(Mob user, ArrayList<T> targets, String targetName) {
        T detected = null;

        // Check surrounding objects
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction) {
            case "up" -> nextWorldY = user.getTopY() - 10;
            case "down" -> nextWorldY = user.getBottomY() + 10;
            case "left" -> nextWorldX = user.getLeftX() - 10;
            case "right" -> nextWorldY = user.getRightX() + 10;
        }
        int col = nextWorldX / Main.game.tileSize;
        int row = nextWorldY / Main.game.tileSize;

        for (T target : targets) {
            if (target != null) {
                if (target.getCol() == col && target.getRow() == row && target.name.equals(targetName)) {
                    detected = target;
                    break;
                }
            }
        }
        return detected;
    }
}