package net.dinglezz.torgrays_trials.entity.npc;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.event.KeyEvent;
import java.util.Random;

public class GateKeeper extends Mob {
    public GateKeeper(TilePoint tilePoint) {
        super("Gate Keeper", tilePoint);
        direction = "down";
        speed = 1;

        resizeSolidArea(8, 16, 32, 32, 5);

        setImages();
        setDialogue();
    }

    public void setImages() {
        up1 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_up_1");
        up2 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_up_2");
        up3 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_up_3");

        down1 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_down_1");
        down2 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_down_2");
        down3 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_down_3");

        left1 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_left_1");
        left2 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_left_2");
        left3 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_left_3");

        right1 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_right_1");
        right2 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_right_2");
        right3 = registerEntitySprite("entity/npc/gatekeeper/gatekeeper_right_3");
    }
    public void setDialogue() {
        dialogues.addFirst("Hey there partner!");
        dialogues.add(1, "This place is quite dark isn't it?");
        dialogues.add(2, "I heard that it's some sort of curse that \nwas placed upon this village");
        dialogues.add(3, "Who did it though?");
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
                else if ( random <= 75) direction = "left";
                else direction = "right";

                actionLockCounter = 0;
            }
        }
    }
    @Override
    public void speak(boolean facePlayer) {
        super.speak(true);

        // Pathfinding
        if (Main.game.pathFinding) onPath = true;
    }

    // Interact Prompt
    @Override
    public <T extends Entity> void onHit(T entity) {
        Main.game.ui.uiState = States.UIStates.INTERACT;
    }
    @Override
    public <T extends Entity> void onLeave(T entity) {
        Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
    }

    // Functionality
    @Override
    public <T extends Entity> void whileHit(T entity) {
        if (Main.game.inputHandler.uiKeyStates.get(States.UIStates.INTERACT).get(KeyEvent.VK_E)) {
            speak(true);
        }
    }
}
