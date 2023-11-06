package org.acitech;

import org.acitech.entities.enemies.Rico;
import org.acitech.entities.Entity;
import org.acitech.entities.Player;
import org.acitech.tilemap.Room;
import org.acitech.tilemap.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePanel extends JPanel implements Runnable {

    final int screenWidth = 800;
    final int screenHeight = 600;
    final int fps = 60;
    private ArrayList<Entity> newEntities = new ArrayList<Entity>();

    KeyHandler keys = new KeyHandler();
    Thread gameThread;

    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    public static Player player = new Player();
    public static UI ui = new UI();
    public static HashMap<String, Room> rooms = new HashMap<>();
    public static String currentRoom = null;

    public GamePanel() {
        // Configure the JPanel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setMinimumSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Register keyboard and mouse listeners
        this.addKeyListener(keys);
        this.addMouseListener(keys);
    }

    public void startGameThread() {
        // Create a room
        currentRoom = "default";
        Room room = new Room(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (Math.random() > 0.5) continue;

                room.setTile(x, y, Tile.grass);
            }
        }
        rooms.put(currentRoom, room);

        // Create 10 enemies for no reason ¯\_(ツ)_/¯
//        for (int i = 0; i < 10; i++) {
//            addNewEntity(new Enemy(Math.random() * screenWidth, Math.random() * screenHeight));
//        }

        // Create 1 Rico for reason ¯\_(ツ)_/¯
        for (int i = 0; i < 10; i++) {
            addNewEntity(new Rico(Math.random() * screenWidth, Math.random() * screenHeight));
        }

        // Create 10 items for no reason ¯\_(ツ)_/¯
//        for (int i = 0; i < 10; i++) {
//            addNewEntity(new Item(Math.random() * screenWidth, Math.random() * screenHeight));
//        }

        // Create 10 Water for no reason ¯\_(ツ)_/¯
//        for (int i = 0; i < 10; i++) {
//            addNewEntity(new Water(Math.random() * screenWidth, Math.random() * screenHeight));
//        }

        // Create and start the game loop thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void addNewEntity(Entity entity) {
        newEntities.add(entity);
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
        ArrayList<Entity> disposedEntities = new ArrayList<Entity>();

        // Add newly created entities
        entities.addAll(newEntities);
        newEntities.clear();

        // Loop through each entity and tick them
        for (Entity entity : entities) {
            entity.tickEntity(delta);

            // Check if entity is disposed and add them to a list
            if (entity.isDisposed()) {
                disposedEntities.add(entity);
            }
        }

        // Tick the player
        player.tickEntity(delta);

        // Loop through each disposed entity and remove them
        for (Entity entity : disposedEntities) {
            entities.remove(entity);
        }

        // Clear the list of mouse clicks
        KeyHandler.mouseClicks.clear();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D ctx = (Graphics2D) g;

        // Draw the current room
        rooms.get(currentRoom).draw(ctx);

        // Loop through each entity in a cloned list of entities and draw them
        for (Entity entity : new ArrayList<>(entities)) {
            entity.draw(ctx);
        }

        // Draw the player & ui
        player.draw(ctx);
        ui.draw(ctx);

        ctx.dispose();
    }
}
