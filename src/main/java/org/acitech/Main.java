package org.acitech;

import javax.swing.*;

public class Main {

    private static ResourceLoader resources;
    private static GamePanel gamePanel;

    public static void main(String[] args) {
        // Load the resource loader
        resources = new ResourceLoader();
        boolean resourcesLoaded = resources.load();

        if (!resourcesLoaded) {
            System.out.println("Failed to load some resources");
            return;
        }

        // Create a JFrame window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("cat");

        // Initialize a JPanel for the game
        gamePanel = new GamePanel();
        window.add(gamePanel);

        // Configure and update the window
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Start the game loop thread
        gamePanel.startGameThread();
    }

    public static ResourceLoader getResources() {
        return resources;
    }

    public static GamePanel getGamePanel() {
        return gamePanel;
    }
}