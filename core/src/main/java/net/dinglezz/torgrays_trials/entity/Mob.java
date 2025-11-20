package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.effect.Effect;
import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.main.CollisionChecker;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Mob extends Entity implements Serializable {
    // Sprites
    public Image up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public Image attackUp, attackDown, attackLeft, attackRight;
    protected int spriteCounter = 0;
    protected int spriteNumber = 1;
    protected int spriteSpeed = 10;
    public String direction = "down";

    // Collision
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);

    // Attributes
    public int defaultSpeed;
    public int speed;
    public int coins;

    // Health
    private int health;
    public int maxHealth;
    public boolean alive = true;
    public boolean dying = false;
    public boolean healthBar = false;

    // Dialogue
    public ArrayList <String> dialogues = new ArrayList<>();
    int dialogueIndex = 0;

    // Inventory stuff
    protected final ArrayList<Item> inventory = new ArrayList<>();
    public final int maxInventorySize = 25;
    public Item currentWeapon;
    public Item currentShield;
    public Item currentLight;

    // State
    public boolean attacking = false;
    public boolean onPath = false;
    public boolean knockBack = false;

    // Counters
    protected int actionLockCounter = 0;;
    protected int dyingCounter = 0;
    protected int healthBarCounter = 0;
    protected int knockBackCounter = 0;

    // Invincibility
    public boolean invincible = false;
    protected int invincibilityCounter = 0;
    protected int invincibilityTime = 40;

    // Other
    public ArrayList<Effect> effects = new ArrayList<>();
    public String hitSound = "Receive Damage";
    public String lootTable;

    public Mob(String name, TilePoint tilePoint) {
        super(name, tilePoint);

        // Make sprites, well, nothing
        down1 = registerEntitySprite("disabled");
        down2 = registerEntitySprite("disabled");
        down3 = registerEntitySprite("disabled");
        up1 = registerEntitySprite("disabled");
        up2 = registerEntitySprite("disabled");
        up3 = registerEntitySprite("disabled");
        left1 = registerEntitySprite("disabled");
        left2 = registerEntitySprite("disabled");
        left3 = registerEntitySprite("disabled");
        right1 = registerEntitySprite("disabled");
        right2 = registerEntitySprite("disabled");
        right3 = registerEntitySprite("disabled");

        attackUp = registerEntitySprite("disabled");
        attackDown = registerEntitySprite("disabled");
        attackLeft = registerEntitySprite("disabled");
        collision = true;
    }

    @Override
    public void checkCollisions() {
        super.checkCollisions();

        // Check tile collision
        CollisionChecker.checkTile(this);
    }

    @Override
    public void update() {
        super.update();

        if (onScreen) {
            if (knockBack) {
                checkCollisions();

                if (colliding || knockBackCounter == 10) {
                    knockBackCounter = 0;
                    knockBack = false;
                    speed = defaultSpeed;
                } else {
                    switch (Main.game.player.direction) {
                        case "up" -> worldY -= speed;
                        case "down" -> worldY += speed;
                        case "left" -> worldX -= speed;
                        case "right" -> worldX += speed;
                    }
                    knockBackCounter++;
                }
            } else {
                setAction();
                checkCollisions();

                // If colliding is false, move the entity
                if (!colliding) {
                    switch (direction) {
                        case "up" -> worldY -= speed;
                        case "down" -> worldY += speed;
                        case "left" -> worldX -= speed;
                        case "right" -> worldX += speed;
                    }
                }
            }

            spriteCounter++;
            if (spriteCounter > spriteSpeed) {
                if (spriteNumber == 1) spriteNumber = 2;
                else if (spriteNumber == 2) spriteNumber = 3;
                else if (spriteNumber == 3) spriteNumber = 1;
                spriteCounter = 0;
            }

            if (invincible) {
                invincibilityCounter--;
                if (invincibilityCounter <= 0) {
                    invincible = false;
                    invincibilityCounter = 0;
                }
            }
        }

        // Update effects
        effects.stream().toList().forEach(Effect::update);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int screenX = worldX - Main.game.player.worldX + Main.game.player.screenX;
        int screenY = worldY - Main.game.player.worldY + Main.game.player.screenY;

        if (onScreen) {
            // Set sprite based on direction and sprite number
            switch (direction) {
                case "up" -> {
                    if (spriteNumber == 1) currentImage = up1;
                    else if (spriteNumber == 2) currentImage = up2;
                    else if (spriteNumber == 3) currentImage = up3;
                }
                case "down" -> {
                    if (spriteNumber == 1) currentImage = down1;
                    else if (spriteNumber == 2) currentImage = down2;
                    else if (spriteNumber == 3) currentImage = down3;
                }
                case "left" -> {
                    if (spriteNumber == 1) currentImage = left1;
                    else if (spriteNumber == 2) currentImage = left2;
                    else if (spriteNumber == 3) currentImage = left3;
                }
                case "right" -> {
                    if (spriteNumber == 1) currentImage = right1;
                    else if (spriteNumber == 2) currentImage = right2;
                    else if (spriteNumber == 3) currentImage = right3;
                }
            }

            // Health Bar
            if (healthBar) {
                double oneScale = (double) Main.game.tileSize / maxHealth;
                double hpBarValue = oneScale * health;

                graphics2D.setColor(Color.black);
                graphics2D.fillRect(screenX - 2, screenY - 17, Main.game.tileSize + 4, 14);
                graphics2D.setColor(Color.white);
                graphics2D.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                healthBarCounter++;
                if (healthBarCounter > 100) {
                    healthBarCounter = 0;
                    healthBar = false;
                }
            }

            // Change alpha if the mob is in an invincible and enable health bar
            if (invincible) {
                healthBar = true;
                healthBarCounter = 0;
                changeAlpha(graphics2D, 0.4f);
            }

            // Do dying animation (if needed)
            if (dying) dyingAnimation(graphics2D, 5);

            // Call the super draw method to draw the entity and hitboxes
            super.draw(graphics2D);
        }
    }

    /**
     * Retrieves the current health value of the mob.
     *
     * @return the current health of the mob.
     */
    // Health
    public int getHealth() {return health;}
    /**
     * Applies damage to the mob.
     * The method ensures that the mob's health cannot drop below zero and handles
     * death-related state changes if the mob's health reaches zero. Additionally,
     * it triggers sound effects, visual particles, and reaction behaviors upon
     * taking damage unless the mob is invincible or already dying.
     *
     * @param damage  the amount of damage to be applied to the mob.
     */
    public void damage(int damage) {
        // If the mob is invincible, do not apply damage
        if (!invincible && !dying) {
            // Enable invincibility
            invincible = true;
            invincibilityCounter = invincibilityTime;

            // Ensure that the damage is not negative
            if (damage < 0) damage = 0;

            // Make sure the damage is not negative and kill it if it is
            health -= damage;
            if (health <= 0) {
                health = 0;
                dying = true;
                dyingCounter = 0;
            }

            // Sound
            Sound.playSFX(hitSound);

            // Other Methods
            generateParticles(this, this);
            damageReaction();
        }
    }
    /**
     * Restores the health of the mob by a specified amount.
     * Ensures that the resulting health value does not exceed the maximum health allowed.
     * If the given health value is negative, the mob's health will not be reduced below zero.
     *
     * @param health the amount of health to be restored to the mob.
     */
    public void heal(int health) {
        // Make sure that health is not negative
        if (health < 0) this.health = 0;

        this.health += health;

        // Ensure this.health does not exceed maxHealth
        if (this.health > maxHealth) {
            this.health = maxHealth;
        }
    }
    /**
     * Manages the dying animation of the mob by gradually changing its alpha (transparency)
     * in a sequence and ultimately marking it as not alive when the animation completes.
     *
     * @param graphics2D the Graphics2D objects used to render the alpha changes for the animation.
     * @param i the interval value that determines the duration of each phase of the animation sequence.
     */
    public void dyingAnimation(Graphics2D graphics2D, int i) {
        dyingCounter++;

        if (dyingCounter <= i) changeAlpha(graphics2D, 0f);
        else if (dyingCounter <= i * 2) changeAlpha(graphics2D, 1f);
        else if (dyingCounter <= i * 3) changeAlpha(graphics2D, 0f);
        else if (dyingCounter <= i * 4) changeAlpha(graphics2D, 1f);
        else if (dyingCounter <= i * 5) changeAlpha(graphics2D, 0f);
        else if (dyingCounter <= i * 6) changeAlpha(graphics2D, 1f);
        else if (dyingCounter <= i * 7) changeAlpha(graphics2D, 0f);
        else if (dyingCounter <= i * 8) changeAlpha(graphics2D, 1f);
        else alive = false;
    }

    // Effects
    public boolean hasEffect(String effectName) {
        // Check if the mob has the effect
        for (Effect effect : effects) {
            if (effect.name.equals(effectName)) {
                // If it does, return true
                return true;
            }
        }
        // If not, return false
        return false;
    }
    public void addEffect(Effect effect) {
        // Check if effect already exists
        for (Effect existingEffect : effects) {
            if (existingEffect.name.equals(effect.name)) {
                existingEffect.time = (existingEffect.time + effect.time);
                return;
            }
        }

        // If not, add the effect
        effects.add(effect);
    }
    public Effect getEffect(String effectName) {
        // Check if the mob has the effect
        for (Effect effect : effects) {
            if (effect.name.equals(effectName)) {
                // If it does, return it
                return effect;
            }
        }
        // If not, return null
        return null;
    }

    // Inventory
    public ArrayList<Item> getInventory() {
        return inventory;
    }
    public void clearInventory() {
        inventory.clear();
    }
    public void removeItem(int index) {
        if (inventory.get(index).amount == 1) inventory.remove(index);
        else inventory.get(index).amount--;
    }
    public void removeItem(Item item) {
        if (item.amount == 1) inventory.remove(item);
        else item.amount--;
    }
    public boolean giveItem(Item item) {
        boolean canObtain = false;

        // Check if maxStack
        if (!(item.amount >= item.maxStack) && !Objects.equals(item.name, "Coins")) {
            int index = searchInInventory(item.name);

            if (index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            }
            else { // New items, so check vacancy
                if (inventory.size() != maxInventorySize) {
                    inventory.add(item);
                    canObtain = true;
                }
            }
        } else if (Objects.equals(item.name, "Coins")) {
            coins += item.amount;
            canObtain = true;
        } else { // Not maxStack, so check vacancy
            if (inventory.size() != maxInventorySize) {
                inventory.add(item);
                canObtain = true;
            }
        }
        return canObtain;
    }
    public int searchInInventory(String itemName) {
        for (Item item : inventory) {
            if (item.name.equals(itemName)) return inventory.indexOf(item);
        }
        return 999; // Not found
    }
    // Actions and Reactions
    public void setAction() {}
    public void damageReaction() {}
    public void checkDrop() {}
    public void speak(boolean facePlayer) {
        // Make sure it shows the dialogue
        Main.game.ui.uiState = States.UIStates.DIALOGUE;

        // If the dialogues finish, restart and exit
        if (dialogueIndex >= dialogues.size()) {
            dialogueIndex = 0;
            Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
        }

        // Update the dialogue and increase the index
        Main.game.ui.setCurrentDialogue(dialogues.get(dialogueIndex));
        dialogueIndex++;

        // Face player (if needed)
        if (facePlayer) {
            switch (Main.game.player.direction) {
                case "up" -> direction = "down";
                case "down" -> direction = "up";
                case "left" -> direction = "right";
                case "right" -> direction = "left";
            }
        }
    }

    // Other Stuff
    public void dropItem(Item droppedItem) {
        // Make sure that there is a list of objects in the current map
        if (Main.game.items.get(Main.game.currentMap) != null) {
            // Add the key and place it in the map
            Main.game.items.get(Main.game.currentMap).add(droppedItem);
            droppedItem.worldX = worldX;
            droppedItem.worldY = worldY;
        }
    }

    public void searchPath(int goalCol, int goalRow, boolean endSearch) {
        int startCol = (worldX + solidArea.x) / Main.game.tileSize;
        int startRow = (worldY + solidArea.y) / Main.game.tileSize;
        Main.game.pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (Main.game.pathFinder.search()) {
            // Next worldX & worldY
            int nextX = Main.game.pathFinder.pathList.getFirst().col * Main.game.tileSize;
            int nextY = Main.game.pathFinder.pathList.getFirst().row * Main.game.tileSize;

            // Entity's solidArea position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + Main.game.tileSize) direction = "up";
            else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + Main.game.tileSize) direction = "down";
            else if (enTopY >= nextY && enBottomY < nextY + Main.game.tileSize) {
                // Left or r=Right
                if (enLeftX > nextX) direction = "left";
                if (enLeftX < nextX) direction = "right";
            }
            else if (enTopY > nextY && enLeftX > nextX) {
                // Up or Left
                direction = "up";
                checkCollisions();
                if (colliding) direction = "left";
            }
            else if (enTopY > nextY && enLeftX < nextX) {
                // Up or Right
                direction = "up";
                checkCollisions();
                if (colliding) direction = "right";
            }
            else if (enTopY < nextY && enLeftX > nextX) {
                // Down or Left
                direction = "down";
                checkCollisions();
                if (colliding) direction = "left";
            }
            else if (enTopY < nextY && enLeftX < nextX) {
                // Down or Right
                direction = "down";
                checkCollisions();
                if (colliding) direction = "right";
            }

            if (endSearch) {
                int nextCol = Main.game.pathFinder.pathList.getFirst().col;
                int nextRow = Main.game.pathFinder.pathList.getFirst().row;
                if (nextCol == goalCol && nextRow == goalRow) {
                    onPath = false;
                }
            }
        }
    }
}