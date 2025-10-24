package net.dingletherat.torgrays_trials.main;

import javax.swing.*;

public class Main {
    public static JFrame window;
    public static Game game;
    public static boolean running = true;

    public static void main(String[] args) {
        window = new JFrame();
        window.setTitle("Torgray's Trials");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        game = new Game();
        game.setup();
        window.add(game);
        window.pack();
        window.setVisible(true);
        game.gameThread.start();
    }
    
    public static void handleException(InterruptedException e) {
        e.printStackTrace();
        running = false;
        System.exit(1);
    }
}
