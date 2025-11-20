package net.dinglezz.torgrays_trials.main;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {
    public static JFrame window;
    public static Game game;

    public static void main(String[] args) throws IOException, InterruptedException {
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Torgray's Trials");

        game = new Game();
        game.init();
        window.add(game);

        DataManager.loadConfig();
        if (game.fullScreen) {
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setUndecorated(true);
        }
        window.pack();

        window.setLocationRelativeTo(null);

        game.setupGame();
        window.setVisible(true);
        game.startGameThread();

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (game.gameState != States.GameStates.TITLE) {
                    game.ui.commandNumber = 1;

                    game.ui.yesAction = () -> {
                        game.gameState = States.GameStates.TITLE;
                        game.ui.uiState = States.UIStates.JUST_DEFAULT;
                        game.ui.subUIState = "Main Title";
                        Sound.music.stop();
                        Sound.playMusic("Tech Geek");
                        game.restart(false);
                    };

                    game.ui.noAction = () -> {
                        game.gameState = States.GameStates.PLAY;
                        game.ui.uiState = States.UIStates.JUST_DEFAULT;
                    };

                    game.gameState = States.GameStates.PAUSE;
                    game.ui.subUIState = "Confirm";
                    game.ui.uiState = States.UIStates.PAUSE;
                    game.ui.setCurrentDialogue("Ending the game without saving \nmay result in you loosing progress. \nPLEASE SAVE BEFORE CONTINUING!!!!!!");
                } else {
                    System.exit(0);
                }
            }
        });

    }
}
