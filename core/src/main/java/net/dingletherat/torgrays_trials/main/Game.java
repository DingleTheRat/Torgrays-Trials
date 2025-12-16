// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.entity.Entity;
import net.dingletherat.torgrays_trials.entity.Player;
import net.dingletherat.torgrays_trials.entity.npc.GateKeeper;
import net.dingletherat.torgrays_trials.main.States.GameStates;
import net.dingletherat.torgrays_trials.rendering.Darkness;
import net.dingletherat.torgrays_trials.rendering.MapHandler;
import net.dingletherat.torgrays_trials.rendering.TileManager;
import net.dingletherat.torgrays_trials.rendering.UI;

import java.util.ArrayList;

import com.badlogic.gdx.math.Matrix4;

public class Game {
    // Entities
    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<Entity> entitiesDrawn = new ArrayList<>();
    public Player player = new Player();

    // Map
    public String currentMap = "Main Island";

    // Other
    Darkness darkness = new Darkness();
    long currentSong;

    public Game() {
        loadPlay();
    }

    public void loadPlay() {
        // Load maps and tiles
        TileManager.loadTiles();
        MapHandler.loadMaps();

        // Set the state to play, so mobs and stuff could be updated and drawn. As well as the uiState for the, well, UI
        Main.gameState = GameStates.PLAY;
        UI.uiState = "Play";

        // TODO: Whenever there is an inventory system, make this only work with items
        darkness.addLightSource(player);

        // TODO: Make an AssetSetter
        entities.add(new GateKeeper(Main.tileSize * 21, Main.tileSize * 23));

        // Change the music to the "playing music"
        Sounds.stopMusic("Tech Geek", Main.titleMusic);
        currentSong = Sounds.playMusic("Umbral Force");
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
        entitiesDrawn.addAll(entities);

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
        entities.forEach(Entity::update);
    }
}
