package org.acitech;

import javax.swing.*;
import java.io.IOException;

public class Main {

    private static ResourceLoader resources;
    private static GamePanel gamePanel;

    public static void main(String[] args) {
        resources = new ResourceLoader();
        resources.load();

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("cat");

        gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }

    public static ResourceLoader getResources() {
        return resources;
    }

    public static GamePanel getGamePanel() {
        return gamePanel;
    }
}