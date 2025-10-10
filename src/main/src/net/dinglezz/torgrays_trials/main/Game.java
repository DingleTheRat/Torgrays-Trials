package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.*;
import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.entity.Player;
import net.dinglezz.torgrays_trials.entity.monster.Monster;
import net.dinglezz.torgrays_trials.environment.EnvironmentManager;
import net.dinglezz.torgrays_trials.pathfinding.Pathfinder;
import net.dinglezz.torgrays_trials.tile.MapHandler;
import net.dinglezz.torgrays_trials.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class Game extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // Word Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // Full Screen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D graphics2D;
    
    // Settings
    public boolean fullScreen;
    public boolean BRendering;
    public boolean pathFinding;

    // Debug
    public boolean debug = false;
    public boolean debugPathfinding = false;
    public boolean debugHitBoxes = false;

    // FPS
    public int FPS = 60;
    public long drawStart;

    // System
    public UI ui;
    public Pathfinder pathFinder;
    public InputHandler inputHandler;
    public EnvironmentManager environmentManager;
    Thread gameThread;

    // Entities
    public Player player;
    public HashMap<String, ArrayList<Entity>> objects = new HashMap<>();
    public HashMap<String, ArrayList<Item>> items = new HashMap<>();
    public HashMap<String, ArrayList<Mob>> npcs = new HashMap<>();
    public HashMap<String, ArrayList<Monster>> monsters = new HashMap<>();
    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Particle> particleList = new ArrayList<>();

    public States.GameStates gameState = States.GameStates.TITLE;
    public States.ExceptionStates exceptionState = States.ExceptionStates.ONLY_IGNORABLE;
    String exceptionStackTrace = "";
    public int saveSlot = 0;
    public String currentMap = "Main Island";
    public String difficulty;

    public Game() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public void setupGame() {
        // Setup classes
        setupExceptionHandling();
        TileManager.setup();
        MapHandler.loadMaps();
        LootTableHandler.loadLootTables();
        environmentManager.setup();

        // Set Assets
        AssetSetter.setAssets(true);

        // Add listener
        this.addKeyListener(inputHandler);

        Sound.playMusic("Tech Geek");
        gameState = States.GameStates.TITLE;
        player.setDefaultPosition();

        // Load DataManager
        if (fullScreen) setFullScreen();
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D) tempScreen.getGraphics();
    }
    private void setupExceptionHandling() {
        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            if (exceptionState != States.ExceptionStates.NOTHING) {
                // Console stuff (If anyone actually sees this)
                System.err.println("Torgray's Trials has encountered an error!");
                System.err.println("This is likely not your fault, please create an issue on GitHub with the error below.");
                System.err.println("---------------------------------------------------------------------------------------");
                exception.printStackTrace();
                exceptionStackTrace = "Error Message: '" + exception.getMessage() + "' Stack Trace: " + Arrays.toString(exception.getStackTrace());

                if (exceptionState != States.ExceptionStates.INSTANT_QUIT) {
                    // Display the error message
                    startGameThread();
                    gameState = States.GameStates.EXCEPTION;
                    ui.commandNumber = 0;
                } else {
                    // Welp, guess we're quitting
                    System.exit(1);
                }
            } else {
                // If the state is nothing, just forget the exception ever happened and restarted the game thread
                startGameThread();
            }
        });
    }
    public void init() {
        ui = new UI(this);
        pathFinder = new Pathfinder(this);
        inputHandler = new InputHandler();
        environmentManager = new EnvironmentManager(this);
        player = new Player();
    }
    public void adjustDifficulty() {
        if (difficulty.equals("Easy")) {
            System.out.println("Imagine Picking Easy");

            // Modified Darkness State Stuff
            environmentManager.lighting.nightLength = 18000;
            environmentManager.lighting.gloomLength = 9000;

            environmentManager.lighting.gloomChance = 35;
            environmentManager.lighting.lightGloomChance = 50;
            environmentManager.lighting.darkGloomChance = 15;
        } else if (difficulty.equals("Medium")) {
            System.out.println("Kinda a mid game mode lol");
            // No modified stats since Medium is the default
        } else if (difficulty.equals("Hard")) {
            System.out.println("You really think you are \"hardcore\"?");

            // Modified State Stuff
            Main.game.environmentManager.lighting.nightLength = 7200;
            Main.game.environmentManager.lighting.gloomLength = 144000;

            Main.game.environmentManager.lighting.gloomChance = 35;
            Main.game.environmentManager.lighting.lightGloomChance = 10;
            Main.game.environmentManager.lighting.darkGloomChance = 55;
        }
    }
    public void respawn() {
        player.heal(player.maxHealth);
        player.invincible = false;
        currentMap = "Main Island";
        player.setDefaultPosition();
        environmentManager.lightUpdated = true;
    }
    void restart(boolean deleteSave) {
        // Delete save (if needed)
        if (deleteSave && saveSlot != 0) {
            // Delete the save file
            String userHome = System.getProperty("user.home");
            File directory = new File(userHome, ".torgray");
            File saveFile = new File(directory, "torgrays-trials-save-" + saveSlot + ".dat");
            if (saveFile.exists()) {
                if (!saveFile.delete()) {
                    System.err.println("Failed to delete save file: " + saveFile.getAbsolutePath());
                }
            }
        }

        // Set default stuff
        player.setDefaultValues();
        player.setItems();
        currentMap = "Main Island";
        player.setDefaultPosition();
        player.effects.clear();
        saveSlot = 0;

        // Set Assets
        objects.clear();
        items.clear();
        npcs.clear();
        monsters.clear();
        AssetSetter.setAssets(true);

        // Darkness reset
        environmentManager.lightUpdated = true;
        environmentManager.lighting.darknessCounter = 0;
        environmentManager.lighting.darknessState = States.DarknessStates.NIGHT;
        environmentManager.lighting.nextGloom = environmentManager.lighting.chooseNextGloom();
    }
    public void setFullScreen() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        graphicsDevice.setFullScreenWindow(Main.window);

        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        final double drawInterval = 1_000_000_000.0 / FPS;
        long lastTime = System.nanoTime();
        long timer = 0;
        double delta = 0;
        long tick = 0;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            delta += elapsedTime / drawInterval;
            timer += elapsedTime;

            while (delta >= 1) {
                long start = System.nanoTime();
                update();
                if (BRendering && gameState != States.GameStates.TITLE) {
                    drawToTempScreen();
                    drawToScreen();
                } else {
                    repaint();
                }
                delta--;
                tick++;
                
                if (debug && tick % 60 == 0) {
                    FPS = Math.round(1_000_000_000F / (System.nanoTime() - start));
                }
            }

            if (timer >= 1_000_000_000) {
                timer = 0;
            }
        }
    }
    public void update() {
        if (gameState == States.GameStates.PLAY) {
            // Player
            player.update();

            // Items
            ArrayList<Item> currentItems = new ArrayList<>(items.getOrDefault(currentMap, new ArrayList<>()));
            currentItems.stream()
                    .filter(Objects::nonNull)
                    .forEach(Entity::update);

            // Objects
            ArrayList<Entity> currentObjects = new ArrayList<>(objects.getOrDefault(currentMap, new ArrayList<>()));
            currentObjects.stream()
                    .filter(Objects::nonNull)
                    .forEach(Entity::update);

            // NPCs
            ArrayList<Mob> currentNPCs = new ArrayList<>(npcs.getOrDefault(currentMap, new ArrayList<>()));
            currentNPCs.stream()
                    .filter(Objects::nonNull)
                    .forEach(Entity::update);

            // Monsters
            ArrayList<Monster> monsters = this.monsters.getOrDefault(currentMap, new ArrayList<>());
            for (int i = 0; i < monsters.size(); i++) {
                Monster monster = monsters.get(i);
                if (monster != null) {
                    if (monster.alive && !monster.dying) {
                        monster.update();
                    } else if (!monster.alive) {
                        monster.checkDrop();
                        monsters.set(i, null);

                        // Respawn if all monsters are dead
                        if (monsters.stream().allMatch(Objects::isNull)) {
                            AssetSetter.setMonsters(false);
                        }
                    }
                }
            }

            // Particles
            particleList.stream()
                    .filter(particle -> particle != null && particle.exists)
                    .forEach(Particle::update);

            environmentManager.update();
        }
    }

    public void draw(Graphics2D graphics2D) {
        // Debug
        if (debug) drawStart = System.nanoTime();

        // Title Screen
        if (gameState == States.GameStates.TITLE) ui.draw(graphics2D);
        else {
            // Draw :)
            TileManager.draw(graphics2D);

            // Add entities to the list
            if (gameState != States.GameStates.GAME_END) entityList.add(player);

            entityList.addAll(npcs.getOrDefault(currentMap, new ArrayList<>()));
            entityList.addAll(items.getOrDefault(currentMap, new ArrayList<>()));
            entityList.addAll(objects.getOrDefault(currentMap, new ArrayList<>()));
            entityList.addAll(monsters.getOrDefault(currentMap, new ArrayList<>()));

            // Sort and draw entities
            entityList.stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(e -> e.worldY))
                    .forEach(entity -> entity.draw(graphics2D));

            // Empty Entity List
            entityList.clear();

            // Filter out null particles before drawing
            particleList.stream()
                    .filter(Objects::nonNull)
                    .filter(particle -> particle.exists)
                    .forEach(particle -> particle.draw(graphics2D));

            // More  drawing :D
            environmentManager.draw(graphics2D);
            ui.draw(graphics2D);
        }
    }
    public void drawToTempScreen() {
        draw(graphics2D);
    }

    public void drawToScreen() {
        Graphics graphics = getGraphics();
        graphics.drawImage(tempScreen,0,0,screenWidth2, screenHeight2, null);
        graphics.dispose();
    }
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        draw(graphics2D);
        graphics2D.dispose();
    }
 }
