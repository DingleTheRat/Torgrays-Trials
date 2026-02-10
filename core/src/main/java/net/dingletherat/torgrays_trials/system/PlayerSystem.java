//Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.MovementComponent;
import net.dingletherat.torgrays_trials.component.PlayerComponent;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.main.States;
import net.dingletherat.torgrays_trials.rendering.Map;

public class PlayerSystem implements System {
    public void update(float deltaTime) {
        for (Integer entity : EntityHandler.queryAll(PlayerComponent.class)) {
            // Clamp the camera to the map bounds
            Map map = TileSystem.maps.get(Main.world.getMap());
            int maxCameraX = map.x() * Main.tileSize - Main.screenWidth / 2;
            int maxCameraY = map.y() * Main.tileSize - Main.screenHeight / 2;
            if (Main.world.cameraX < Main.screenWidth / 2f) Main.world.cameraX = Main.screenWidth / 2f;
            if (Main.world.cameraY < Main.screenHeight / 2f) Main.world.cameraY = Main.screenHeight / 2f;
            if (Main.world.cameraX > maxCameraX) Main.world.cameraX = maxCameraX;
            if (Main.world.cameraY > maxCameraY) Main.world.cameraY = maxCameraY;

            EntityHandler.getEntityComponent(entity, MovementComponent.class).ifPresent(component -> {
                StringBuilder newDirection = new StringBuilder();

                /* Depending on the key pressed, append a newDirection with a direction.
                 * If the direction was appended more than once, append the direction with a space
                 this is to let the mob's update method know if the movement is diagonal */
                if (Gdx.input.isKeyPressed(Input.Keys.W)) newDirection.append("up");
                if (Gdx.input.isKeyPressed(Input.Keys.S)) newDirection.append(!newDirection.isEmpty() ? "" : "down");
                if (Gdx.input.isKeyPressed(Input.Keys.A)) newDirection.append(!newDirection.isEmpty() ? " left" : "left");
                if (Gdx.input.isKeyPressed(Input.Keys.D)) newDirection.append(!newDirection.isEmpty() ? " right" : "right");

                // If nothing was added to the StringBuilder, meaning the player isn't walking, change his state accordingly
                if (newDirection.isEmpty()) component.state = States.MovementStates.IDLE;
                else component.state = States.MovementStates.WALKING;

                // Set the direction to the final newDirection string and let the mod's update method do the rest
                component.direction = newDirection.toString().trim();
            });
        }
    }
    public void draw() { }
}
