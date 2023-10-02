package org.acitech;

import org.acitech.entities.Enemy;
import org.acitech.entities.Entity;
import org.acitech.entities.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    final int screenWidth = 800;
    final int screenHeight = 600;
    final int fps = 60;

    KeyHandler keys = new KeyHandler();
    Thread gameThread;

    ArrayList<Entity> entities = new ArrayList<Entity>();
    public static Player player = new Player();

    public GamePanel() {
        // Configure the JPanel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Register keyboard and mouse listeners
        this.addKeyListener(keys);
        this.addMouseListener(keys);
    }

    public void startGameThread() {
        // Create and start the game loop thread
        gameThread = new Thread(this);
        gameThread.start();

        // Create 1,000 enemies for no reason ¯\_(ツ)_/¯
        for (int i = 0; i < 1_000; i++) {
            entities.add(new Enemy(Math.random() * screenWidth, Math.random() * screenHeight));
        }
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

    public void update(double delta) {
        // Loop through each entity and tick them
        for (Entity entity : entities) {
            entity.tickEntity(delta);
        }

        // Tick the player
        player.tickEntity(delta);

        // Clear the list of mouse clicks
        KeyHandler.mouseClicks.clear();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D ctx = (Graphics2D) g;

        // Loop through each entity and draw them
        for (Entity entity : entities) {
            entity.draw(ctx);
        }

        // Draw the player
        player.draw(ctx);

        ctx.dispose();
    }
}
