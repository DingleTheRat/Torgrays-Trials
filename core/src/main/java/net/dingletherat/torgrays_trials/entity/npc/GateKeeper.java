// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.npc;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.entity.Mob;
import net.dingletherat.torgrays_trials.rendering.Image;

public class GateKeeper extends Mob {
    public GateKeeper(float spawnX, float spawnY) {
        super("GateKeeper", spawnX, spawnY, 12f, 16f);

        // Set the sprites
        spriteSheet = Image.loadImage("entity/npc/gatekeeper_sheet");
        spriteSheet.scaleImage(Main.tileSize * 3, Main.tileSize * 4);
        currentImage = spriteSheet;

        // Set some properties
        properties.put("draw_eyes", true);
        properties.put("wander_speed", 60);
        animationSpeed = 13;
    }
}
