package net.dinglezz.torgrays_trials.entity.monster;

import net.dinglezz.torgrays_trials.entity.LootTableHandler;
import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Dracore extends Monster {
    public Dracore(TilePoint tilePoint, String lootTable) {
        super("Dracore", tilePoint);
        this.lootTable = lootTable;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxHealth = 5;
        heal(maxHealth);
        attack = 3;

        if (Main.game.environmentManager.lighting != null) {
            if (Main.game.environmentManager.lighting.darknessState == States.DarknessStates.GLOOM ||
                    Main.game.environmentManager.lighting.darknessState == States.DarknessStates.LIGHT_GLOOM ||
                    Main.game.environmentManager.lighting.darknessState == States.DarknessStates.DARK_GLOOM) {
                attack += 2;
                maxHealth = maxHealth * 5;
                heal(maxHealth);
            }
        }

        getImage();

        resizeSolidArea(-1, -1, 46, 46, 2);
    }
    public void getImage() {
        up1 = registerEntitySprite("entity/monster/dracore/dracore_1");
        up2 = registerEntitySprite("entity/monster/dracore/dracore_2");
        up3 = registerEntitySprite("entity/monster/dracore/dracore_3");

        down1 = registerEntitySprite("entity/monster/dracore/dracore_1");
        down2 = registerEntitySprite("entity/monster/dracore/dracore_2");
        down3 = registerEntitySprite("entity/monster/dracore/dracore_3");

        left1 = registerEntitySprite("entity/monster/dracore/dracore_1");
        left2 = registerEntitySprite("entity/monster/dracore/dracore_2");
        left3 = registerEntitySprite("entity/monster/dracore/dracore_3");

        right1 = registerEntitySprite("entity/monster/dracore/dracore_1");
        right2 = registerEntitySprite("entity/monster/dracore/dracore_2");
        right3 = registerEntitySprite("entity/monster/dracore/dracore_3");
    }

    @Override
    public void update() {
        super.update();

        // Pathfinding
        if (Main.game.pathFinding) {
            int xDistance = Math.abs(worldX - Main.game.player.worldX);
            int yDistance = Math.abs(worldY - Main.game.player.worldY);
            int tileDistance = (xDistance + yDistance) / Main.game.tileSize;

            if (!onPath && tileDistance < 5) {
                int random = new Random().nextInt(2);
                if (random == 1) {
                    onPath = true;
                }
            } else if (tileDistance > 20) {
                onPath = false;
            }
        }
    }

    @Override
    public void setAction() {
        if (onPath) {
            int goalCol = (Main.game.player.worldX + Main.game.player.solidArea.x) / Main.game.tileSize;
            int goalRow = (Main.game.player.worldY + Main.game.player.solidArea.y) / Main.game.tileSize;

            searchPath(goalCol, goalRow, false);
        } else {
            actionLockCounter++;
            if (actionLockCounter == 120) {
                int random = new Random().nextInt(100);

                if (random <= 25) direction = "up";
                else if (random <= 50) direction = "down";
                else if (random <= 75) direction = "left";
                else direction = "right";
                actionLockCounter = 0;
            }
        }
    }

    @Override
    public void checkDrop() {
        ArrayList<Item> loot = LootTableHandler.generateLoot(LootTableHandler.lootTables.get(lootTable));

        if (!loot.isEmpty() && loot.getFirst() != null) {
            dropItem(loot.getFirst());
        }
    }

    // Particles
    @Override public Color getParticleColor() {return new Color(63, 6, 5);}
    @Override public int getParticleSize() {return 6;} // 6 pixels
    @Override public int getParticleSpeed() {return 1;}
    @Override public int getParticleMaxHealth() {return 20;}
}
