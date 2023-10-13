package org.acitech;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class KeyHandler implements KeyListener, MouseListener {

    public static boolean wDown = false;
    public static boolean aDown = false;
    public static boolean sDown = false;
    public static boolean dDown = false;
    public static boolean zDown = false;
    public static boolean xDown = false;
    public static boolean mousePressed = false;
    public static ArrayList<Click> mouseClicks = new ArrayList<>();

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
            case KeyEvent.VK_Z -> {
                zDown = true;
            }
            case KeyEvent.VK_X -> {
                xDown = true;
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
            case KeyEvent.VK_Z -> {
                zDown = false;
            }
            case KeyEvent.VK_X -> {
                xDown = false;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        mouseClicks.add(new Click(e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public class Click {
        private final int x;
        private final int y;
        private final int button;

        Click(MouseEvent event) {
            x = event.getX();
            y = event.getY();
            button = event.getButton();
        }

        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public int getButton() {
            return button;
        }
    }
}
