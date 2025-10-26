// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class InputHandler {
    // Keyboard
    public static class Keyboard implements KeyListener {
        public HashMap<Integer, Boolean> keyMap = new HashMap<>(){{
            // Movement
            put(KeyEvent.VK_W, false);
            put(KeyEvent.VK_A, false);
            put(KeyEvent.VK_S, false);
            put(KeyEvent.VK_D, false);
            put(KeyEvent.VK_UP, false);
            put(KeyEvent.VK_LEFT, false);
            put(KeyEvent.VK_DOWN, false);
            put(KeyEvent.VK_RIGHT, false);
            // Actions
            put(KeyEvent.VK_ENTER, false);
            put(KeyEvent.VK_SPACE, false);
            put(KeyEvent.VK_ESCAPE, false);
        }};

        public Keyboard() {
            Main.LOGGER.info("Loaded keyboard input handler");
        }

        public void cancelInputs() {
            keyMap.replaceAll((_, _) -> false);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println(e.getKeyChar());
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            keyMap.put(code, true);
            System.out.println(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyMap.put(e.getKeyCode(), false);
        }
    }

    // Mouse
    public static class Mouse implements MouseListener {
        public Mouse() {
            Main.LOGGER.info("Loaded mouse input handler");
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Main.LOGGER.debug("Mouse clicked");
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
