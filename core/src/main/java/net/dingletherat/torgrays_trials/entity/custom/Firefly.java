// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.custom;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.entity.Entity;

public class Firefly extends Entity {
    float nextX = 0f;
    float nextY = 0f;

    public Firefly(float spawnX, float spawnY) {
        super("Firefly", spawnX, spawnY);

        collision = false;
        properties.put("light_radius", (float) Main.random.nextInt(15) + 1);
        properties.put("light_intensity", 0.3f);
    }

    @Override
    public void update() {
        // Random flicker
        if (Main.random.nextFloat() > 0.5f) {
            float intensity = 0.3f * ((Main.random.nextFloat() - 0.5f) / 5f + 1);
            properties.put("light_intensity", intensity);
        }

        // Random movement
        if (Main.random.nextFloat() > 0.9f) {
            nextX += (Main.random.nextFloat() - 0.5f) * 2;
            nextY += (Main.random.nextFloat() - 0.5f) * 2;
            if (nextX > 2) nextX = 2;
            if (nextX < -2) nextX = -2;
            if (nextY > 2) nextY = 2;
            if (nextY < -2) nextY = -2;
        }
        x += nextX;
        y += nextY;
    }
}
