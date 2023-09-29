package org.acitech;

import org.acitech.entities.Enemy;
import org.acitech.entities.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    final int screenWidth = 800;
    final int screenHeight = 600;
    public static final int fps = 60;

    KeyHandler keys = new KeyHandler();
    Thread gameThread;

    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public static Player player = new Player();

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keys);
        this.addMouseListener(keys);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();

        // Initialize enemies
        for (int i = 0; i < 10_000; i++) {
            enemies.add(new Enemy(Math.random() * screenWidth, Math.random() * screenHeight));
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
        for (Enemy enemy : enemies) {
            enemy.tickEntity(delta);
        }

        player.tickEntity(delta);

        KeyHandler.mouseClicks.clear();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D ctx = (Graphics2D) g;

        for (Enemy enemy : enemies) {
            enemy.draw(ctx);
        }

        player.draw(ctx);

        ctx.dispose();
    }
}
