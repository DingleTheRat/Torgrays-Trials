// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.entity.component.*;
import net.dingletherat.torgrays_trials.main.World;

public class SpriteSystem implements System {
    @Override
    public void tick(World world) {
        Main.batch.begin();

        for (Integer entity : world.query(SpriteComponent.class, SpriteSheetComponent.class)) {
            // Get the entity's positon on the screen. If the entity has positionComponent, draw it at the positon. Otherwise, draw it at 0
            float screenX;
            float screenY;
            if (world.entityHasComponent(entity, PositionComponent.class)) {
                // Get PositionComponent
                PositionComponent positionComponent = world.getEntityComponent(entity, PositionComponent.class).get();

                screenX = positionComponent.x - Main.world.player.cameraX + Main.screenWidth / 2f;
                screenY = positionComponent.y - Main.world.player.cameraY + Main.screenHeight / 2f;
            } else {
                screenX = 0 - Main.world.player.cameraX + Main.screenWidth / 2f;
                screenY = 0 - Main.world.player.cameraY + Main.screenHeight / 2f;
            }

            // Draw depending what component the entity has
            // If the entity has a SpriteComponent, then just draw the sprite
            if (world.entityHasComponent(entity, SpriteComponent.class)) {
                SpriteComponent component = world.getEntityComponent(entity, SpriteComponent.class).get();
                Main.batch.draw(component.sprite.getTexture(), Math.round(screenX), Math.round(screenY));
            }
            // If it has a SpriteSheetComponent, draw only a part of the provided sprite image
            if (world.entityHasComponent(entity, SpriteSheetComponent.class)) {
                SpriteSheetComponent component = world.getEntityComponent(entity, SpriteSheetComponent.class).get();

                // Store what part of the sprite sheet to draw
                int imageX = Main.tileSize * component.column;
                int imageY = Main.tileSize * component.row;

                Main.batch.draw(component.spriteSheet.getTexture(),
                    // Image position in the world
                    Math.round(screenX), Math.round(screenY), Main.tileSize, Main.tileSize,
                    // Image position in the sprite sheet
                    imageX, component.spriteSheet.getTexture().getWidth() - imageY, Main.tileSize, Main.tileSize,
                    // Flip X and Y
                    false, false);
            }
        }
        Main.batch.end();
    }
}
