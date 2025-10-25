package net.dingletherat.torgrays_trials.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Main {
    public static JFrame window;
    public static Game game;
    public static boolean running = true;
    public static final Logger LOGGER = LoggerFactory.getLogger("Torgray's Trials");

    public static void main(String[] args) {
        LOGGER.info("Program started");

        window = new JFrame();
        window.setTitle("Torgray's Trials");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        game = new Game();
        game.setup();
        window.add(game);
        window.pack();
        window.setVisible(true);

        game.gameThread.start();
        Main.LOGGER.info("Game thread started");
    }
    
    public static void handleException(InterruptedException e) {
        e.printStackTrace();
        running = false;
        System.exit(1);
    }
}
