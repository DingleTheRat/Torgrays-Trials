package net.dinglezz.torgrays_trials.entity.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Player;
import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.entity.LootTableHandler;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class Dark_Chest extends Entity implements Serializable {
    String lootTable;
    boolean opened = false;

    public Dark_Chest(TilePoint tilePoint, String lootTable) {
        super("Dark Chest", tilePoint);
        this.lootTable = lootTable;

        setDefaultValues();
    }

    public void setDefaultValues() {
        name = "Dark Chest";
        image = registerEntitySprite("entity/object/dark_chest/closed");
        image2 = registerEntitySprite("entity/object/dark_chest/opened");
        currentImage = image;

        // Collision
        collision = true;
        hitArea.y = 48;
        hitArea.height = 20;
    }

    // Interact Prompts
    @Override
    public <T extends Entity> void onHit(T entity) {
        if (!opened && entity instanceof Player) Main.game.ui.uiState = States.UIStates.INTERACT;
    }
    @Override
    public <T extends Entity> void onLeave(T entity) {
        if (!opened && entity instanceof Player) Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
    }

    // Functionality
    @Override
    public <T extends Entity> void whileHit(T entity) {
        if (entity instanceof Player player && !opened) {
            if (Main.game.inputHandler.uiKeyStates.get(States.UIStates.INTERACT).get(KeyEvent.VK_E)) {
                // Play sound :D
                Sound.playSFX("Unlock");

                // Get Ready for dialogue
                Main.game.ui.uiState = States.UIStates.DIALOGUE;
                StringBuilder stringBuilder = new StringBuilder();

                if (lootTable.isEmpty()) {
                    stringBuilder.append("This chest is empty :(");
                } else {
                    ArrayList<Item> loot = LootTableHandler.generateLoot(LootTableHandler.lootTables.get(lootTable));
                    if (loot.isEmpty()) stringBuilder.append("This chest is empty :(");
                    else stringBuilder.append("Hmmm what's in this dark old chest?");

                    for (Item reward : loot) {
                        if (player.giveItem(reward))
                            stringBuilder.append("\n+").append(reward.amount).append(" ").append(reward.name);
                        else stringBuilder.append("\nI can't carry all this loot :(");
                    }
                }

                Main.game.ui.setCurrentDialogue(stringBuilder.toString());
                currentImage = image2;
                opened = true;
            }
        }
    }
}
