// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import java.awt.Graphics;

import net.dingletherat.torgrays_trials.main.Main;
import net.dingletherat.torgrays_trials.rendering.Image;

/** Extend this class to create an Entity.
 * Think of an Entity like a simple object in a map that is rendered separately from tiles.
 * This "object" can be modified however you want, and you can make it do whatever you want.
 * You can change its position, make it have different states, ect...
 * There are many extensions of this class such as mobs that, well, extend the possibilities of entities.
 * Mobs, for instance, are living beings that can walk around and talk.
 **/
public class Entity {
    /// The image that is drawn at the entity's location to represent the entity.
    public Image currentImage = Image.loadImage("disabled");
    
    // Positions
    public float x;
    public float y;

    // Updating
    /// Can the entity update while not being on the screen? If it's true, the {@code} onScreen} field will always be set to true inside the main update loop of the entity.
    public boolean updateOffScreen = false;
    /// Pretty self-explanatory. It's used to increase performance by not loading the entity while it's off-screen.
    public boolean onScreen = false;


    // Other
    public String name;

    public Entity(String name, float spawnX, float spawnY) {
        // Can you guess what this does? (impossible)
        this.name = name;
        x = spawnX;
        y = spawnY;
    }

    /** Called in the main draw method to draw the entity.
     * The entity must be in a certain Hashmap in Main.game (depending on their type) to be drawn.
     * For instance, a normal entity would have to be in the {@code} entities} array list to draw.
     * Remove the entity from the array list if you stopped using it to stop drawing it.
     * <p>
     * For performance, everything is behind an if statement with {@code} onScreen} to only draw when the entity is on screen.
     * Use {@code} updateOffScreen} if you want to disable this.
     * <p>
     * @param graphics What graphics will the entity be drawn with?
     * I don't want to explain what graphics are, look at the Java class if you want to know - it has good documentation.
     **/
    public void draw(Graphics graphics) {
        if (onScreen) {
            System.out.println(Math.round(x + Main.game.screenWidth / 2f));
            System.out.println(Math.round(y + Main.game.screenWidth / 2f));
            graphics.drawImage(currentImage.getImage(), Math.round(x - Main.game.player.camX + Main.game.screenWidth / 2f),
                    Math.round(y - Main.game.player.camY + Main.game.screenHeight / 2f), null);
        }
    }

    /** Called in the main update loop to update the entity.
     * The entity must be in a certain Hashmap in Main.game (depending on their type) to be updated.
     * For instance, a normal entity would have to be in the {@code} entities} array list to update.
     * Remove the entity from the array list if you stopped using it to stop updating it.
     * <p>
     * For performance, everything is behind an if statement with {@code} onScreen} to only update when the entity is on screen.
     * Use {@code} updateOffScreen} if you want to disable this.
     **/
    public void update() {
        // Check if the entity is on the screen using the player's camera position
	    onScreen = x + Main.game.tileSize > Main.game.player.camX + Main.game.screenWidth / 2f &&
			    x - Main.game.tileSize < Main.game.player.camX + Main.game.screenWidth / 2f &&
			    y + Main.game.tileSize > Main.game.player.camY + Main.game.screenHeight / 2f &&
			    y - Main.game.tileSize < Main.game.player.camY + Main.game.screenHeight / 2f || updateOffScreen;
    }
}
