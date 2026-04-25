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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.math.Matrix4;

public class World {
    // ECS
    private Map<String, Map<Integer, List<Component>>> entityStorage = new HashMap<>();
    private Map<Integer, List<Component>> entities = new HashMap<>();
    public Map<Integer, List<Map.Entry<Class<? extends Component>, List<Object>>>> entityTemplates = new HashMap<>();
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
    public boolean skipDraw = false;
    public boolean skipUpdate = false;

    // ECS Methods
    public List<Component> generateComponents(List<Map.Entry<Class<? extends Component>, List<Object>>> componentClasses) {
        // Loop through all the componentClasses and construct them. Then, add them to the components list.
        List<Component> components = new ArrayList<>();
        for (Map.Entry<Class<? extends Component>, List<Object>> entry : componentClasses) {
            Class<? extends Component> componentClass = entry.getKey();
            List<Object> args = entry.getValue();

            // Enter try and catch zone in case constructing fails
            try {
                // Convert arguments (JSONArray) to constructor parameter types (ArrayList of Classes)
                Class<?>[] parameterTypes = args.stream()
                        .map(Object::getClass)
                        .toArray(Class<?>[]::new);

                // Create the component instance and add it to the components list
                Component component = componentClass.getConstructor(parameterTypes).newInstance(args.toArray());

                // BUT BEFORE, if the component type is SINGLE, get rid of any other existing component that is the same as it
                if (component.getType() == Component.ComponentType.SINGLE) {
                    boolean hadDuplicate = components.stream().anyMatch(existing -> existing.getClass().equals(componentClass));
                    components.removeIf(existing -> existing.getClass().equals(componentClass));

                    // Warn if that's the case ofc
                    if (hadDuplicate) Main.LOGGER.warn("Component {} is already present, despite the type being SINGLE! Old component was removed.", componentClass.getSimpleName());
                }

                components.add(component);
            } catch (NoSuchMethodException exception) {
                Main.LOGGER.error("Failed to generate components: Component '{}' has invalid args!", componentClass.getSimpleName());
                Main.LOGGER.error("The args are for this constructor that doesn't exist: {}", exception.getMessage());
            } catch (Exception exception) {
                Main.handleException(exception);
            }
        }
        return components;
    }
    public void updateEntity(int identifier, List<Map.Entry<Class<? extends Component>, List<Object>>> componentClasses) {
        // Generate the new components
        List<Component> newComponents = generateComponents(componentClasses);

        // If any of the new components are SINGLE, remove the existing ones of the same class so no components get replaced
        newComponents.stream().filter(component -> component.getType() == Component.ComponentType.SINGLE)
            .forEach(component -> entities.get(identifier).removeIf(existing -> existing.getClass().equals(component.getClass())));

        // Now add in the new components to the entity
        entities.get(identifier).addAll(newComponents);
        entityTemplates.get(identifier).addAll(componentClasses);
    }
    public int newEntity(List<Map.Entry<Class<? extends Component>, List<Object>>> componentTemplate) {

        // Set the return variable to the nextIdentifier. However, if there is a vacant identifier from a removed entity, use that.
        int identifier = nextIdentifier;
        if (!VACANT_IDENTIFIERS.isEmpty()) identifier = VACANT_IDENTIFIERS.remove(0);
        else nextIdentifier++; // Since we used up the nextIdentifier, increase it for the next entity

        // If there's a playerComponent, check if there already is one. If not, make the entity the player. If there is, warn.
        if (componentTemplate.stream().anyMatch(entry -> entry.getKey().equals(PlayerComponent.class))) {
            if (player == null) player = identifier;
            else {
                Main.LOGGER.warn("Entity template {} has a PlayerComponent when player has already been declared!");
                Main.LOGGER.warn("The entity will be created, but the component will be removed");
                componentTemplate = componentTemplate.stream().filter(entry -> !entry.getKey().equals(PlayerComponent.class)).collect(Collectors.toList());
            }
        }

        List<Component> components = generateComponents(componentTemplate);

        // Add the new entity
        entities.put(identifier, components);
        entityTemplates.put(identifier, new ArrayList<>(componentTemplate));

        // Return the identifier
        return identifier;
    }
    public void removeEntity(int identifier) {
        // If the player is the one being removed, then set player to null so another one is possible to add
        if (entityTemplates.get(identifier).stream().anyMatch(entry -> entry.getKey().equals(PlayerComponent.class))) player = null;

        // Make its identifier vacant and remove it from everywhere
        VACANT_IDENTIFIERS.add(identifier);
        entities.remove(identifier);
        entityTemplates.remove(identifier);
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
        return !entities.containsKey(identifier) ? List.of() : entities.get(identifier);
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
                List<Map.Entry<Class<? extends Component>, List<Object>>> components = new ArrayList<>(EntityHandler.TEMPLATES.get(entityData.get("name")));

                // If the entity also has a JSONArray by the name of "components",
                // that means that the map file want to add on some more components to the entity on top of the template, so add the components to the components map
                if (entityData.has("components") && entityData.get("components") instanceof JSONArray)
                    components = EntityHandler.modifyComponentClasses(components, entityData.getJSONArray("components"), mapName + " map");

                // Add in the entity
                Integer identifier = newEntity(components);
                entityTemplates.put(identifier, EntityHandler.TEMPLATES.get(entityData.get("name")));
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
        // Move the camera to the player's x and y (if there is a PositionComponent)
        EntityHandler.getComponent(getPlayer(), PositionComponent.class).ifPresent(component -> {
            cameraX = component.x;
            cameraY = component.y;
        });

        for (System system : updateSystems) system.update(deltaTime);

        // Update UI
        UI.update();
    }
}
