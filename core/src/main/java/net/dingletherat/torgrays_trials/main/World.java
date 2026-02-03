// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.*;
import net.dingletherat.torgrays_trials.rendering.UI;
import net.dingletherat.torgrays_trials.system.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.math.Matrix4;

public class World {
    // ECS
    private Map<Integer, List<Component>> entities = new HashMap<>();
    private Integer player;
    public List<System> updateSystems = new ArrayList<>();
    public List<System> drawSystems = new ArrayList<>();
    private final ArrayList<Integer> VACANT_IDENTIFIERS = new ArrayList<>();
    private int nextIdentifier = 0;

    // Map and camera
    public String currentMap = "Main Island";
    public float cameraX;
    public float cameraY;

    // Other
    public long currentSong;

    // ECS Methods
    public int newEntity(Map<Class<? extends Component>, List<Object>> componentTemplate) {
        // Set the return variable to the nextIdentifier. However, if there is a vacant identifier from a removed entity, use that.
        int identifier = nextIdentifier;
        if (!VACANT_IDENTIFIERS.isEmpty()) identifier = VACANT_IDENTIFIERS.remove(0);
        else nextIdentifier++; // Since we used up the nextIdentifier, increase it for the next entity

        // Loop through all the componentClasses and construct them. Then, add them to the components list.
        List<Component> components = new ArrayList<>();
        for (Class<? extends Component> componentClass : componentTemplate.keySet()) {
            List<Object> args = componentTemplate.get(componentClass);

            // Enter try and catch zone in case constructing fails
            try {
                // Convert arguments to constructor parameter types
                Class<?>[] parameterTypes = args.stream()
                        .map(Object::getClass)
                        .toArray(Class<?>[]::new);

                // Create the component instance and add it to the components list
                Component component = componentClass.getConstructor(parameterTypes).newInstance(args.toArray());
                components.add(component);

                // If the component is a playerComponent, check if there already is one. If not, make the entity the player. If there is, warn.
                if (component instanceof PlayerComponent) {
                    if (player == null) player = identifier;
                    else {
                        Main.LOGGER.warn("Entity template {} has a PlayerComponent when player has already been declated!");
                        Main.LOGGER.warn("The entity will be created, but the component will be removed");
                        components.remove(component);
                    }
                }
            } catch (NoSuchMethodException exception) {
                Main.LOGGER.error(
                        "Failed to create entity: Component '{}' in Template '{}' has invalid args!",
                        componentClass.getSimpleName(), componentTemplate.get(NameComponent.class)
                );
                Main.LOGGER.error("The args are for this constructor that doesn't exist: {}", exception.getMessage());
            } catch (Exception exception) {
                Main.handleException(exception);
            }
        }

        // Add the new entity
        entities.put(identifier, components);

        // Return the identifier
        return identifier;
    }
    public void removeEntity(int identifier) {
        VACANT_IDENTIFIERS.add(identifier);
        entities.remove(identifier);
    }
    public List<Component> getComponents(int identifier) {
        return entities.containsKey(identifier) ? List.of() : entities.get(identifier);
    }
    public int getPlayer() {
        return player == null ? -1 : player;
    }
    public List<Integer> queryAll(Class<? extends Component>... components) {
        List<Integer> result = new ArrayList<>();

        for (int entity : entities.keySet()) {
            boolean match = true;

            for (Class<? extends Component> componentClass : components) {
                boolean found = false;

                for (Component component : entities.get(entity)) {
                    if (componentClass.isInstance(component)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    match = false;
                    break;
                }
            }

            if (match) result.add(entity);
        }

        return result;
    }
    public List<Integer> queryAny(Class<? extends Component>... components) {
        List<Integer> result = new ArrayList<>();
        for (int entity : entities.keySet()) {
            boolean match = false;

            for (Class<? extends Component> componentClass : components) {
                for (Component component : entities.get(entity)) {
                    if (componentClass.isInstance(component)) {
                        match = true;
                        break;
                    }
                }
            }

            if (match) result.add(entity);
        }

        return result;
    }
    public <T extends Component> Optional<T> getEntityComponent(int identifier, Class<T> type) {
        List<Component> entity = entities.get(identifier);

        for (Component element : entity) {
            if (type.isInstance(element)) {
                return Optional.of((T) element);
            }
        }
        return Optional.empty();
    }
    public <T extends Component> List<T> getEntityComponents(int identifier, Class<T> type) {
        List<T> result = new ArrayList<>();
        List<Component> entity = entities.get(identifier);

        for (Component component : entity) {
            if (type.isInstance(component)) {
                result.add(type.cast(component));
            }
        }
        return result;
    }
    public <T extends Component> boolean entityHasComponent(int identifier, Class<T> type) {
        List<Component> entity = entities.get(identifier);
        for (Component element : entity) {
            if (type.isInstance(element)) {
                return true;
            }
        }
        return false;
    }

    public void draw() {
        // Flip the Y axis because most stuff are drawn upside down
        Matrix4 original = new Matrix4(Main.batch.getProjectionMatrix());
        Main.batch.getProjectionMatrix().setToOrtho(0, Main.screenWidth, Main.screenHeight, 0, 0, 1);

        for (System system : drawSystems) system.draw();

        // Unflip the Y axis and draw the UI. UI isn't drawn upside-down, which is why we flip it back
        Main.batch.setProjectionMatrix(original);
        UI.stage.draw();
    }
    public void update(float deltaTime) {
        // Update UI
        UI.update();

        // Move the camera to the player's x and y (if there is a PositionComponent)
        getEntityComponent(getPlayer(), PositionComponent.class).ifPresent(component -> {
            cameraX = component.x;
            cameraY = component.y;
        });

        for (System system : updateSystems) system.update(deltaTime);
    }
}
