// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.*;

public class DarknessSystem implements System{
    public float ambientDarkness = 0.92f;  // How dark it is without lights (0.0 = no darkness, 1.0 = complete darkness)
    Texture radialLightTexture = createRadialLight(128);

    public static Texture createRadialLight(int radius) {
        int size = radius * 2;

        // Create a Pixmap with RGBA8888 format
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);

        // Draw radial gradient
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float dx = x - radius;
                float dy = y - radius;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                float alpha = MathUtils.clamp(1f - (distance / radius), 0f, 1f);

                // Set pixel color: white with alpha
                pixmap.setColor(1f, 1f, 1f, alpha);
                pixmap.drawPixel(x, y);
            }
        }

        // Create texture from pixmap
        Texture texture = new Texture(pixmap);
        pixmap.dispose(); // Dispose Pixmap, texture keeps the data

        return texture;
    }

    @Override
    public void draw() {
        // Save the current batch state and end it
        Main.batch.flush();

        // 1. Create FBO for darkness
        FrameBuffer darknessFbo = new FrameBuffer(Pixmap.Format.RGBA8888, Main.screenWidth, Main.screenHeight, false);
        darknessFbo.begin();

        // Clear with black at full opacity
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Start a batch for drawing lights
        Main.batch.begin();

        // Use blending that allows lights to "cut holes" in the darkness
        Main.batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (Integer lightSource : Main.world.queryAny(LightComponent.class)) {
            LightComponent component = Main.world.getEntityComponent(lightSource, LightComponent.class).get();


            int radius = Math.round(component.lightRadius);
            float intensity = component.lightIntensity;

            // Flicker the light (if needed)
            if (component.flicker) {
                if (Main.random.nextFloat() > 0.5)
                    intensity = 0.8f * ((Main.random.nextFloat() - 0.5f) / 5f + 1);
            }

            // Set the intensity accordingly
            intensity = Math.max(0f, Math.min(1f, intensity));

            // Get the entity's position. As long as it has a PositionComponent, otherwise, just leave it at 0.
            float x = 0;
            float y = 0;
            if (Main.world.entityHasComponent(lightSource, PositionComponent.class)) {
                PositionComponent positionComponent = Main.world.getEntityComponent(lightSource, PositionComponent.class).get();
                x = positionComponent.x;
                y = positionComponent.y;
            }

            // Get the light's position on the screen
            x = x - Main.world.cameraX + Main.screenWidth / 2f + 24 - radius;
            y = y - Main.world.cameraY + Main.screenHeight / 2f + 24 - radius;

            Main.batch.setColor(1f, 1f, 1f, intensity);
            Main.batch.draw(radialLightTexture, x, y, radius * 2, radius * 2);
        }

        Main.batch.end();
        darknessFbo.end();

        // 2. Draw the darkness overlay to the main scene
        Main.batch.begin();

        // Use normal alpha blending but control overall darkness with color alpha
        Main.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Main.batch.setColor(1f, 1f, 1f, ambientDarkness);

        TextureRegion darknessRegion = new TextureRegion(darknessFbo.getColorBufferTexture());
        Main.batch.draw(darknessRegion, 0, 0);

        // Reset to normal settings
        Main.batch.setColor(1f, 1f, 1f, 1f);
        Main.batch.end();

        darknessFbo.dispose();
    }

    @Override
    public void update(float deltaTime) { }
}
