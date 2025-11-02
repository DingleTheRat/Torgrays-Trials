// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class InputHandler implements KeyListener {
    // Keyboard
    public HashMap<Integer, Boolean> keyMap = new HashMap<>() {{
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

    public InputHandler() {
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
