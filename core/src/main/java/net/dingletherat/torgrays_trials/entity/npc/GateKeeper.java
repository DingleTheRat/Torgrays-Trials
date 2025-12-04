// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.npc;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.entity.Mob;
import net.dingletherat.torgrays_trials.main.Game;
import net.dingletherat.torgrays_trials.rendering.Image;

public class GateKeeper extends Mob {
    public GateKeeper(float spawnX, float spawnY) {
        super("GateKeeper", spawnX, spawnY, 12f, 16f);

        // Set the sprites
        spriteSheet = Image.loadImage("entity/npc/gatekeeper_sheet");
        spriteSheet.scaleImage(Game.tileSize * 3, Game.tileSize * 4);
        currentImage = spriteSheet;

        // Set some properties
        drawEyes = true;
        animationSpeed = 13;
    }
}
