// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.entity.Entity;
import net.dingletherat.torgrays_trials.entity.EntityHandler;
import net.dingletherat.torgrays_trials.entity.Player;
import net.dingletherat.torgrays_trials.entity.component.Component;
import net.dingletherat.torgrays_trials.entity.npc.GateKeeper;
import net.dingletherat.torgrays_trials.main.States.GameStates;
import net.dingletherat.torgrays_trials.rendering.Darkness;
import net.dingletherat.torgrays_trials.rendering.MapHandler;
import net.dingletherat.torgrays_trials.rendering.TileManager;
import net.dingletherat.torgrays_trials.rendering.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Matrix4;

public class World {
    // Entities
    @Deprecated
    public ArrayList<Entity> oldEntities = new ArrayList<>();
    private Map<Integer, List<Component>> entities = new HashMap<>();
    private final ArrayList<Integer> VACANT_IDENTIFIERS = new ArrayList<>();
    private int nextIdentifier = 0;
    public ArrayList<Entity> entitiesDrawn = new ArrayList<>();
    public Player player = new Player();

    // Map
    public String currentMap = "Main Island";

    // Other
    Darkness darkness = new Darkness();
    long currentSong;

    public World() {
        loadWorld();
    }

    public void loadWorld() {
        // Load maps and tiles
        TileManager.loadTiles();
        MapHandler.loadMaps();

        // Set the state to play, so mobs and stuff could be updated and drawn. As well as the uiState for the, well, UI
        Main.gameState = GameStates.PLAY;
        UI.uiState = "Play";

        // TODO: Whenever there is an inventory system, make this only work with items
        darkness.addLightSource(player);

        // TODO: Make an AssetSetter
        oldEntities.add(new GateKeeper(Main.tileSize * 21, Main.tileSize * 23));

        int chest = newEntity(EntityHandler.TEMPLATES.get("Chest"));
        Main.LOGGER.debug("{}", entities);
        removeEntity(chest);
        newEntity(EntityHandler.TEMPLATES.get("Chest"));
        Main.LOGGER.debug("{}", entities);

        // Change the music to the "playing music"
        Sounds.stopMusic("Tech Geek", Main.titleMusic);
        currentSong = Sounds.playMusic("Umbral Force");
    }

    // ECS Methods
    public int newEntity(Map<Class<? extends Component>, List<Object>> componentTemplates) {
        // Set the return variable to the nextIdentifier. However, if there is a vacant identifier from a removed entity, use that.
        int identifier = nextIdentifier;
        if (!VACANT_IDENTIFIERS.isEmpty()) identifier = VACANT_IDENTIFIERS.remove(0);
        else nextIdentifier++; // Since we used up the nextIdentifier, increase it for the next entity

        // Loop through all the componentClasses and construct them. Then, add them to the components list.
        List<Component> components = new ArrayList<>();
        for (Class<? extends Component> componentClass : componentTemplates.keySet()) {
            List<Object> args = componentTemplates.get(componentClass);

            // Enter try and catch zone in case constructing fails
            try {
                // Convert arguments to constructor parameter types
                Class<?>[] parameterTypes = args.stream()
                        .map(Object::getClass)
                        .toArray(Class<?>[]::new);

                // Create the component instance and add it to the components list
                Component component = componentClass.getConstructor(parameterTypes).newInstance(args.toArray());
                components.add(component);
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

    public void draw() {
        // Flip the Y axis because most stuff are drawn upside down
        Matrix4 original = new Matrix4(Main.batch.getProjectionMatrix());
        Main.batch.getProjectionMatrix().setToOrtho(0, Main.screenWidth, Main.screenHeight, 0, 0, 1);

        TileManager.draw(); // TEMPORARY! will relace this with better code later

        /*
         * For entities, they will be drawn slightly differently than everything else.
         * Entities will be added to an ArrayList called entities drawn.
         * All entities in that ArrayList will later be sorted depending on their y position.
         * This allows entities to be behind something, but also in front, depending on their y position.
         */
        // Add in all entities
        entitiesDrawn.add(player);
        entitiesDrawn.addAll(oldEntities);

        // Sort the entities by y position
        entitiesDrawn.sort((entity, entity2) -> Float.compare(entity.y, entity2.y));

        // Draw the entities and clear the Arraylist (for the next frame)
        Main.batch.begin();
        entitiesDrawn.forEach(Entity::draw);
        entitiesDrawn.clear();
        Main.batch.end();

        darkness.draw();

        // Unflip the Y axis and draw the UI. UI isn't drawn upside-down, which is why we flip it back
        Main.batch.setProjectionMatrix(original);
        UI.stage.draw();
    }
    public void update() {
        // Update UI
        UI.update();

        // Update player and entites
        player.update();
        oldEntities.forEach(Entity::update);
    }
}
