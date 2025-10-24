package net.dinglezz.torgrays_trials.entity.npc;

import java.awt.event.KeyEvent;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.entity.item.Gate_Key;
import net.dinglezz.torgrays_trials.entity.item.World_Map;
import net.dinglezz.torgrays_trials.entity.item.soup.Torgray_Soup;
import net.dinglezz.torgrays_trials.entity.item.weapon.Stick;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Coiner extends Mob {
    public Coiner(TilePoint tilePoint) {
        super("Coiner", tilePoint);
        direction = "down";
        speed = 1;
        spriteSpeed = 80;

        resizeSolidArea(8, 21, 32, 28, 2);

        getImage();
        setDialogue();
        setItem();
    }

    public void getImage() {
        down1 = registerEntitySprite("entity/npc/coiner/coiner_1");
        down2 = registerEntitySprite("entity/npc/coiner/coiner_2");
        down3 = registerEntitySprite("entity/npc/coiner/coiner_3");
    }
    public void setDialogue() {
        dialogues.addFirst("Hey there partner, I'm Coiner! \nWanna buy or sell something? :D");
    }
    public void setItem() {
        giveItem(new Torgray_Soup(null));
        giveItem(new Gate_Key(null));
        giveItem(new Stick(null));
        giveItem(new World_Map(null));
    }
    @Override
    public void speak(boolean facePlayer) {
        super.speak(false);
        Main.game.ui.uiState = States.UIStates.TRADE;
        Main.game.ui.subUIState = "Select";
        Main.game.ui.commandNumber = 0;
        Main.game.ui.npc = this;
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
}
