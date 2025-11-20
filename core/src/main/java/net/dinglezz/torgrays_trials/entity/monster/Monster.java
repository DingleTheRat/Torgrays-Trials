package net.dinglezz.torgrays_trials.entity.monster;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.entity.Player;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.io.Serializable;

public abstract class Monster extends Mob implements Serializable {
    public int attack;

    public Monster(String name, TilePoint tilePoint) {
        super(name, tilePoint);

        // Change the hit sound
        hitSound = "Hit Monster";
        attack = 1;
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;

        // Pathfinding
        if (Main.game.pathFinding) {
            onPath = true;
        } else {
            // If not, then change the direction
            switch (Main.game.player.direction) {
                case "up" -> direction = "down";
                case "down" -> direction = "up";
                case "left" -> direction = "right";
                case "right" -> direction = "left";
            }
        }
    }

    @Override
    public <T extends Entity> void whileHit(T entity) {
        // If the entity is a player, then attack
        if (entity instanceof Player player) {
            if (dying) return;
            player.damage(attack);
        }
    }
}