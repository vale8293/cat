package org.acitech;

import javax.swing.*;
import java.util.HashMap;

public class Main {

    private static HashMap<String, Long> profiles = new HashMap<>();
    private static ResourceLoader resources;
    private static GamePanel gamePanel;

    public static void main(String[] args) {
        startProfile("Startup");

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

        finishProfile("Startup");
    }

    public static ResourceLoader getResources() {
        return resources;
    }

    public static GamePanel getGamePanel() {
        return gamePanel;
    }

    public static void startProfile(String name) {
        profiles.put(name.toLowerCase(), System.nanoTime());
    }

    public static void finishProfile(String name) {
        Long time = profiles.getOrDefault(name.toLowerCase(), null);

        if (time == null) {
            System.out.println(name + " took ?ms");
            return;
        }

        long timing = System.nanoTime() - time;
        System.out.println(name + " took " + timing / 1000000f + "ms");
    }
}