package org.acitech;

import org.acitech.entities.Player;
import org.acitech.inputs.Controls;
import org.acitech.inventory.ItemStack;
import org.acitech.inventory.ItemType;
import org.acitech.tilemap.Map;
import org.acitech.tilemap.Tile;
import org.acitech.utils.Broadcast;
import org.acitech.utils.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
    private final int fps = 60;
    private boolean paused = false;
    private boolean unEscaped = false;
    private Map map;
    private Thread gameThread;
    private static Player player;
    private static Vector2d camera = new Vector2d(0, 0);
    private static Vector2d upperBounds = new Vector2d(0, 0);
    private static Vector2d lowerBounds = new Vector2d(0, 0);

    public GamePanel() {
        // Configure the JPanel
        int initWidth = 800;
        int initHeight = 600;

        this.setPreferredSize(new Dimension(initWidth, initHeight));
        this.setMinimumSize(new Dimension(initWidth, initHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Register keyboard and mouse listeners
        Controls keys = new Controls();

        this.addKeyListener(keys);
        this.addMouseListener(keys);

        Broadcast.registerClass(UI.class);

        initGame();
    }

    private void initGame() {
        // Create a map
        map = new Map(new Random().nextInt());
        map.generateRooms(3);

        // Position the player
        player = new Player(map.getCurrentRoom());
        player.position.set(Tile.tileSize * 7.0d, Tile.tileSize * 7.0d);

        // Give spell tomes
        player.spellInv.addItem(new ItemStack(ItemType.FIRE_TOME_1));
        player.spellInv.addItem(new ItemStack(ItemType.AQUA_TOME_1));

        // Give test potions
        player.defaultInv.addItem(new ItemStack(ItemType.ATTACK_POTION));
        player.defaultInv.addItem(new ItemStack(ItemType.MANA_POTION));
        player.defaultInv.addItem(new ItemStack(ItemType.HEALTH_POTION));
        player.defaultInv.addItem(new ItemStack(ItemType.SPEED_POTION));

        // Create and start the game loop thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000d / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        // Game loop
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update(delta);
                repaint();
                delta--;
            }
        }
    }

    private void update(double delta) {
        if (!paused) {
            map.getCurrentRoom().tick(delta);

            // Clear the list of mouse clicks
            Controls.flushClicks();

            // Check for pausing
            if (!Controls.isKeyPressed(Controls.pauseKey)) {
                unEscaped = true;
            }

            if (unEscaped && Controls.isKeyPressed(Controls.pauseKey)) {
                paused = true;
                unEscaped = false;
            }
        }
        else {
            // Check for pausing
            if (!Controls.isKeyPressed(Controls.pauseKey)) {
                unEscaped = true;
            }

            if (unEscaped && Controls.isKeyPressed(Controls.pauseKey)) {
                paused = false;
                unEscaped = false;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D ctx = (Graphics2D) g;

        // Draw the current room
        map.getCurrentRoom().draw(ctx);

        // Draw the player & ui
        UI.draw(ctx);

        // Update the camera position
        updateCamera();

        if (paused) {
            UI.drawPauseMenu(ctx);
        }

        ctx.dispose();
    }

    private void updateCamera() {
        double followSpeed = 0.15;
        double offsetX = (player.position.getX() + Tile.tileSize / 2d - this.getWidth() / 2d - camera.getX()) * followSpeed;
        double offsetY = (player.position.getY() + Tile.tileSize / 2d - this.getHeight() / 2d - camera.getY()) * followSpeed;

        camera = new Vector2d(camera.getX() + offsetX, camera.getY() + offsetY);
//        camera = new Vector2d(player.position.getX() - this.getWidth() / 2d, player.position.getY() - this.getHeight() / 2d);

        // Update the upper and lower bounds
        upperBounds.set(
            camera.getX() + Main.getGamePanel().getWidth(),
            camera.getY() + Main.getGamePanel().getHeight()
        );
        lowerBounds.set(
            upperBounds.getX() - Main.getGamePanel().getWidth() - 1,
            upperBounds.getY() - Main.getGamePanel().getHeight() - 1
        );
    }

    public static Vector2d getCamera() {
        return camera;
    }

    public Vector2d getCameraCenter() {
        return new Vector2d(
                player.position.getX() - player.width / 2d - camera.getX(),
                player.position.getY() - player.height / 2d - camera.getY()
        );
    }

    public Vector2d getUpperFrameBounds() {
        return upperBounds;
    }

    public Vector2d getLowerFrameBounds() {
        return lowerBounds;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public static Player getPlayer() {
        return player;
    }
}
