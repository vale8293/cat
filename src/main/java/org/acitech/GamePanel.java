package org.acitech;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    final int screenWidth = 800;
    final int screenHeight = 600;
    final int fps = 60;

    KeyHandler keys = new KeyHandler();
    Thread gameThread;

    int squareX = 0;
    int squareY = 0;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keys);
        this.setFocusable(true);
    }

    public void startGameThread() {
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

    public void update(double delta) {
        if (keys.wDown) {
            squareY -= 5;
        }
        if (keys.aDown) {
            squareX -= 5;
        }
        if (keys.sDown) {
            squareY += 5;
        }
        if (keys.dDown) {
            squareX += 5;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D ctx = (Graphics2D) g;

        ctx.setColor(Color.white);
        ctx.fillRect(squareX, squareY, 32 * 2, 32 * 2);

        ctx.dispose();
    }
}
