package org.acitech.inputs;

import org.acitech.utils.Broadcast;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Controls implements KeyListener, MouseListener {

    /*
     *  Keys and clicks maps
     */

    private static final HashMap<Integer, Boolean> keysPressed = new HashMap<>();
    private static final ArrayList<Click> mouseClicks = new ArrayList<>();
    private static boolean mousePressed = false;

    /*
     *  Keybindings
     */

    public final static int upKey = KeyEvent.VK_W;
    public final static int leftKey = KeyEvent.VK_A;
    public final static int downKey = KeyEvent.VK_S;
    public final static int rightKey = KeyEvent.VK_D;
    public final static int leftElemKey = KeyEvent.VK_SHIFT;
    public final static int rightElemKey = KeyEvent.VK_SPACE;
    public final static int pauseKey = KeyEvent.VK_ESCAPE;
    public final static int selectKey = KeyEvent.VK_ENTER;
    public final static int $1stSlotKey = KeyEvent.VK_1;
    public final static int $2ndSlotKey = KeyEvent.VK_2;
    public final static int $3rdSlotKey = KeyEvent.VK_3;
    public final static int $4thSlotKey = KeyEvent.VK_4;
    public final static int $5thSlotKey = KeyEvent.VK_5;
    public final static int $6thSlotKey = KeyEvent.VK_6;
    public final static int $7thSlotKey = KeyEvent.VK_7;
    public final static int $8thSlotKey = KeyEvent.VK_8;
    public final static int $9thSlotKey = KeyEvent.VK_9;
    public final static int $10thSlotKey = KeyEvent.VK_0;
    public final static int zKey = KeyEvent.VK_Z;
    public final static int xKey = KeyEvent.VK_X;
    public final static int useKey = KeyEvent.VK_E;
    public final static int cKey = KeyEvent.VK_C;
    public final static int vKey = KeyEvent.VK_V;

    /*
     *  Keyboard and mouse getters and functions
     */

    public static boolean isKeyPressed(int key) {
        return keysPressed.getOrDefault(key, false);
    }
    public static boolean isMouseDown() {
        return mousePressed;
    }
    public static boolean isMouseUp() {
        return !mousePressed;
    }
    public static ArrayList<Click> getMouseClicks() {
        return new ArrayList<>(mouseClicks);
    }
    public static boolean hasMouseClicks() {
        return !mouseClicks.isEmpty();
    }
    public static void flushClicks() {
        mouseClicks.clear();
    }

    /*
     * Keyboard and mouse listeners
     */

    @Override
    public void keyTyped(KeyEvent event) {}

    @Override
    public void keyPressed(KeyEvent event) {
        keysPressed.put(event.getKeyCode(), true);

        Broadcast.emit("keyDown");
        Broadcast.emit("keyUpdate");
    }

    @Override
    public void keyReleased(KeyEvent event) {
        keysPressed.put(event.getKeyCode(), false);

        Broadcast.emit("keyUp");
        Broadcast.emit("keyUpdate");
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        mouseClicks.add(new Click(e));

        Broadcast.emit("mouseDown");
        Broadcast.emit("mouseUpdate");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePressed = false;

        Broadcast.emit("mouseUp");
        Broadcast.emit("mouseUpdate");
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}
