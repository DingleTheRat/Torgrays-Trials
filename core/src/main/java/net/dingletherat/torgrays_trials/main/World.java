// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.*;
import net.dingletherat.torgrays_trials.rendering.MapHandler;
import net.dingletherat.torgrays_trials.rendering.UI;
import net.dingletherat.torgrays_trials.system.System;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.math.Matrix4;

public class World {
    // ECS
    private Map<String, Map<Integer, List<Component>>> entityStorage = new HashMap<>();
    private Map<Integer, List<Component>> entities = new HashMap<>();
    private Integer player;
    public List<System> updateSystems = new ArrayList<>();
    public List<System> drawSystems = new ArrayList<>();
    private final ArrayList<Integer> VACANT_IDENTIFIERS = new ArrayList<>();
    private int nextIdentifier = 0;

    // Map and camera
    private String currentMap = "Disabled";
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
                // Convert arguments (JSONArray) to constructor parameter types (ArrayList of Classes)
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
    public void addComponent(int identifier, Component component) {
        entities.get(identifier).add(component);
    }
    public void removeComponent(int identifier, Component component) {
        entities.get(identifier).remove(component);
    }
    public Map<Integer, List<Component>> getEntities() {
        return entities;
    }
    public List<Component> getComponents(int identifier) {
        return entities.containsKey(identifier) ? List.of() : entities.get(identifier);
    }
    public int getPlayer() {
        return player == null ? -1 : player;
    }

    // Maps
    public void setMap(String mapName) {
        // Store or update the old map's entites in entityStorage and clear the entities list
        entityStorage.put(currentMap, entities);
        entities.clear();

        // Set entities list to the map's entites
        // If there's already entity data stored in entityStorage, use that
        if (entityStorage.containsKey(mapName))
            entities = entityStorage.get(mapName);
        else {
            // Otherwise, it's time to json out the entites
            // First, check for necessary stuff
            if (!MapHandler.mapFiles.containsKey(mapName)) {
                Main.LOGGER.warn("Unnable to retrive corresponding map file to {}!", mapName);
                return;
            }
            JSONObject mapFile = MapHandler.mapFiles.get(mapName);

            if (!mapFile.has("entities") && !(mapFile.get("entities") instanceof JSONArray)) {
                Main.LOGGER.warn("Map {} does not contain 'entities' field or 'entities' field is not a JSONArray!", mapName);
                return;
            }
            JSONArray entitiesArray = mapFile.getJSONArray("entities");

            // Turn entites JSONArray into a list, so I don't have to use horrible "for i" loops
            List<JSONObject> entities = new ArrayList<>(IntStream.range(0, entitiesArray.length())
                .mapToObj(entitiesArray::getJSONObject)
                .toList());

            for (JSONObject entityData : entities) {
                // Check for important stuff (again)
                if (!entityData.has("name") && !(entityData.get("name") instanceof String)) {
                    Main.LOGGER.warn("Entry in 'entites' array in map {} does not contain 'name' field or 'name' field is not a String!", mapName);
                    return;
                }
                if (!EntityHandler.TEMPLATES.containsKey(entityData.get("name"))) {
                    Main.LOGGER.warn("{} is not an existing entity template!", entityData.get("name"));
                    return;
                }

                // Add in the entity
                Integer entity = newEntity(EntityHandler.TEMPLATES.get(entityData.get("name")));

                // If the entityData has position components and the entity has a PositionComponent, change the data in the component
                if (entityData.has("col") && entityData.has("row") &&
                        entityData.get("col") instanceof Integer && entityData.get("row") instanceof Integer) {
                    EntityHandler.getEntityComponent(entity, PositionComponent.class).ifPresent(component -> {
                        component.x = Main.tileSize * entityData.getInt("col");
                        component.y = Main.tileSize * entityData.getInt("row");
                    });
                } else if (entityData.has("x") && entityData.has("y") &&
                        entityData.get("x") instanceof Integer && entityData.get("y") instanceof Integer) {
                    EntityHandler.getEntityComponent(entity, PositionComponent.class).ifPresent(component -> {
                        component.x = entityData.getInt("x");
                        component.y = entityData.getInt("y");
                    });
                }
            }
        }

        // Finally, do the deed
        currentMap = mapName;
    }
    public String getMap() {
        return currentMap;
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
        EntityHandler.getEntityComponent(getPlayer(), PositionComponent.class).ifPresent(component -> {
            cameraX = component.x;
            cameraY = component.y;
        });

        for (System system : updateSystems) system.update(deltaTime);
    }
}
