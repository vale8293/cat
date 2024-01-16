package org.acitech;

import org.acitech.entities.Entity;
import org.acitech.entities.Item;
import org.acitech.entities.Player;
import org.acitech.entities.enemies.Pepto;
import org.acitech.entities.enemies.Rico;
import org.acitech.tilemap.Room;
import org.acitech.tilemap.Tile;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    final int screenWidth = 800;
    final int screenHeight = 600;
    final int fps = 60;
    private ArrayList<Entity> newEntities = new ArrayList<Entity>();

    KeyHandler keys = new KeyHandler();
    Thread gameThread;

    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    public static Player player = new Player();
    public static Vector2D camera = new Vector2D(0, 0);
    public static UI ui = new UI();
    public static HashMap<String, Room> rooms = new HashMap<>();
    public static String currentRoom = "default";

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
        Room room = new Room(40, 40, new Random().nextInt());
        rooms.put(currentRoom, room);

        for (int x = 0; x < room.getWidth(); x++) {
            for (int y = 0; y < room.getHeight(); y++) {
                double noise = room.getSimplex().get((double) x / 10, (double) y / 10, 0);
                if (noise > 1) {
                    room.setTile(x, y, Tile.dirt);
                } else {
                    room.setTile(x, y, Tile.grass);
                }
            }
        }

        // Create 10 enemies for no reason ¯\_(ツ)_/¯
//        for (int i = 0; i < 10; i++) {
//            addNewEntity(new Enemy(Math.random() * screenWidth, Math.random() * screenHeight));
//        }

        // Create test enemies for no reason ¯\_(ツ)_/¯
        for (int i = 0; i < 100; i++) {
            addNewEntity(new Rico(Math.random() * screenWidth + 400, Math.random() * screenHeight));
        }

        // Create test enemies for no reason ¯\_(ツ)_/¯
        for (int i = 0; i < 100; i++) {
            addNewEntity(new Pepto(Math.random() * screenWidth + 400, Math.random() * screenHeight));
        }

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
        ArrayList<Item> pickupItems = new ArrayList<Item>();

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

            // Check if the entity is an item and is getting picked up
            if (entity instanceof Item itemEntity) {
                if (itemEntity.isGettingPickedUp()) {
                    pickupItems.add(itemEntity);
                }
            }
        }

        // Tick the player
        player.tickEntity(delta);

        // Pick up items
        ArrayList<Item> disposedItems = player.pickupItems(pickupItems);

        // Loop through each disposed entity/item and remove them
        for (Entity entity : disposedEntities) {
            entities.remove(entity);
        }
        for (Item item : disposedItems) {
            entities.remove(item);
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

        // Update the camera position
        updateCamera();

        ctx.dispose();
    }

    private void updateCamera() {
        double followSpeed = 0.2;
        double offsetX = (player.position.getX() + Tile.tileSize / 2d - this.getWidth() / 2d - camera.getX()) * followSpeed;
        double offsetY = (player.position.getY() + Tile.tileSize / 2d - this.getHeight() / 2d - camera.getY()) * followSpeed;

        camera = new Vector2D(camera.getX() + offsetX, camera.getY() + offsetY);
    }

    public Vector2D getCameraCenter() {
        return new Vector2D(
                player.position.getX() - player.width / 2d - camera.getX(),
                player.position.getY() - player.height / 2d - camera.getY()
        );
    }
}
