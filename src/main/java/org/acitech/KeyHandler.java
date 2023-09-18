package org.acitech;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static boolean wDown = false;
    public static boolean aDown = false;
    public static boolean sDown = false;
    public static boolean dDown = false;

    @Override
    public void keyTyped(KeyEvent event) {}

    @Override
    public void keyPressed(KeyEvent event) {
        int code = event.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> {
                wDown = true;
            }
            case KeyEvent.VK_A -> {
                aDown = true;
            }
            case KeyEvent.VK_S -> {
                sDown = true;
            }
            case KeyEvent.VK_D -> {
                dDown = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        int code = event.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> {
                wDown = false;
            }
            case KeyEvent.VK_A -> {
                aDown = false;
            }
            case KeyEvent.VK_S -> {
                sDown = false;
            }
            case KeyEvent.VK_D -> {
                dDown = false;
            }
        }
    }
}
