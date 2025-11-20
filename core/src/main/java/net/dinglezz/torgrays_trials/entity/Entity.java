package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.main.CollisionChecker;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.UtilityTool;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Entity implements Serializable {
    // Images
    public Image image, image2, image3;
    public Image currentImage;

    // Collision
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle hitArea = new Rectangle(-1, -1, 50, 50);
    public boolean collision = false;
    public boolean colliding = false;

    // Position
    public int worldX, worldY;
    public TilePoint tilePoint;

    // Attributes
    public String name;
    public boolean updateOffScreen = false;
    public boolean onScreen = false;

    public Entity(String name, TilePoint tilePoint) {
        this.name = name;

        // Position
        if (tilePoint != null) {
            // Set tile point
            this.tilePoint = tilePoint;

            // Set world coordinates based on tile point
            worldX = tilePoint.col() * Main.game.tileSize;
            worldY = tilePoint.row() * Main.game.tileSize;
        }

        // Image
        image = registerEntitySprite("disabled");
        image2 = registerEntitySprite("disabled");
        image3 = registerEntitySprite("disabled");
    }

    // Hit methods
    public <T extends Entity> void onHit(T entity) {}
    public <T extends Entity> void whileHit(T entity) {}
    public <T extends Entity> void onLeave(T entity) {}

    // Particles (I need to rework this)
    public Color getParticleColor() {return null;}
    public int getParticleSize() {return 0;}
    public int getParticleSpeed() {return 0;}
    public int getParticleMaxHealth() {return 0;}
    public void generateParticles(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxHealth = generator.getParticleMaxHealth();

        Main.game.particleList.add(new Particle(target, color, size, speed, maxHealth, -2, -1));
        Main.game.particleList.add(new Particle(target, color, size, speed, maxHealth, 2, -1));
        Main.game.particleList.add(new Particle(target, color, size, speed, maxHealth, -2, 1));
        Main.game.particleList.add(new Particle(target, color, size, speed, maxHealth, 2, 1));
    }

    // Position
    public int getLeftX() {return worldX + solidArea.x;}
    public int getRightX() {return worldX + solidArea.x + solidArea.width;}
    public int getTopY() {return worldY + solidArea.y;}
    public int getBottomY() {return worldY + solidArea.y + solidArea.height;}
    public int getCol() {return (worldX + solidArea.x) / Main.game.tileSize;}
    public int getRow() {return (worldY + solidArea.y) / Main.game.tileSize;}

    // Sprite Registration
    public Image registerEntitySprite(String imagePath) {
        BufferedImage image;
        try {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/" + imagePath + ".png"));
            } catch (IllegalArgumentException | IOException exception) {
                System.err.println("Warning: \"" + imagePath + "\" is not a valid path.");
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/disabled.png"));
            }
            image = UtilityTool.scaleImage(image, Main.game.tileSize, Main.game.tileSize);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        // Serialize the image to a byte array
        return new Image(UtilityTool.serializeImage(image));
    }
    public Image registerEntitySprite(String imagePath, int width, int height) {
        BufferedImage image;
        try {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/" + imagePath + ".png"));
            } catch (IllegalArgumentException | IOException exception) {
                System.err.println("Warning: \"" + imagePath + "\" is not a valid path.");
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/disabled.png"));
            }
            image = UtilityTool.scaleImage(image, width, height);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        // Serialize the image to a byte array
        return new Image(UtilityTool.serializeImage(image));
    }

    // Collision
    public void checkCollisions() {
        colliding = false;

        // Check collision for all entities in the current map (Even the player)
        CollisionChecker.checkEntity(this, Main.game.objects.get(Main.game.currentMap));
        CollisionChecker.checkEntity(this, Main.game.npcs.get(Main.game.currentMap));
        CollisionChecker.checkEntity(this, Main.game.monsters.get(Main.game.currentMap));
        CollisionChecker.checkEntity(this, Main.game.items.get(Main.game.currentMap));
        CollisionChecker.checkEntity(this, new ArrayList<Player>() {{add(Main.game.player);}});
    }
    /**
     * Resizes the solid area and hit area of the entity based on specified dimensions and offset.
     *
     * @param worldX The x-coordinate of the solid area's position in the world.
     * @param worldY The y-coordinate of the solid area's position in the world.
     * @param width  The width of the solid area.
     * @param height The height of the solid area.
     * @param hitAreaOffset The offset used to adjust the hit area's boundaries relative to the solid area.
     */
    public void resizeSolidArea(int worldX, int worldY, int width, int height, int hitAreaOffset) {
        // Do what the method is supposed to do lol
        solidArea.x = worldX;
        solidArea.y = worldY;
        solidArea.width = width;
        solidArea.height = height;

        // Resize the hit area to be a tad bit bigger than the solid area
        hitArea.x = worldX - (hitAreaOffset / 2);
        hitArea.y = worldY - (hitAreaOffset / 2);
        hitArea.width = width + hitAreaOffset;
        hitArea.height = height + hitAreaOffset;
    }

    public void update() {
        if (worldX + Main.game.tileSize > Main.game.player.worldX - Main.game.player.screenX &&
                worldX - Main.game.tileSize < Main.game.player.worldX + Main.game.player.screenX &&
                worldY + Main.game.tileSize > Main.game.player.worldY - Main.game.player.screenY &&
                worldY - Main.game.tileSize < Main.game.player.worldY + Main.game.player.screenY || updateOffScreen){
            checkCollisions();
            onScreen = true;
        } else
            onScreen = false;
    }

    public void draw(Graphics2D graphics2D) {
        // Get screen X and Y
        int screenX = worldX - Main.game.player.worldX + Main.game.player.screenX;
        int screenY = worldY - Main.game.player.worldY + Main.game.player.screenY;

        if (onScreen) {
            // Draw the entity
            if (currentImage != null) graphics2D.drawImage(currentImage.getImage(), screenX, screenY, null);

            // Reset alpha to 1.0f
            changeAlpha(graphics2D, 1f);

            // Draw the solid area for debugging
            if (Main.game.debugHitBoxes) {
                // Hit area
                graphics2D.setColor(new Color(0.7f, 0.5f, 0, 0.5f));
                graphics2D.setStroke(new BasicStroke(3));
                graphics2D.drawRect(screenX + hitArea.x, screenY + hitArea.y, hitArea.width, hitArea.height);

                // Reset stroke
                graphics2D.setStroke(new BasicStroke(1));

                // Solid area
                    // Change the color depending on it's state
                    if (!collision) graphics2D.setColor(new Color(0, 0.7f, 0, 0.3f));
                    else if (colliding) graphics2D.setColor(new Color(0.7f, 0, 0, 0.5f));
                    else graphics2D.setColor(new Color(0.7f, 0, 0, 0.3f));
                graphics2D.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            }
        }
    }
    public void changeAlpha(Graphics2D graphics2D, float alphaValue) {
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }
}
