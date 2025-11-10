package net.dingletherat.torgrays_trials.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

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
        window.getContentPane().setBackground(Color.BLACK);
        window.setResizable(false);

        game = new Game();
        game.setup();
        window.add(game);
        window.pack();
        
        game.gameThread.start();
        game.gameThread.setUncaughtExceptionHandler((thread, throwable) -> handleException((Exception) throwable));
        Main.LOGGER.info("Game thread started");

        window.setVisible(true);
        Main.LOGGER.info("Window enabled");
    }

    public static void handleException(Exception exception) {
        LOGGER.error("Torgray's Trials has encountered an error!");
        LOGGER.error("This is likely not your fault, please create an issue on GitHub with the error below.");
        LOGGER.error("---------------------------------------------------------------------------------------", exception);
        running = false;
        System.exit(1);
    }
}
