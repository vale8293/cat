package org.acitech;

import org.acitech.entities.Entity;
import org.acitech.entities.Item;
import org.acitech.entities.Player;
import org.acitech.entities.enemies.Jordan;
import org.acitech.entities.enemies.Pepto;
import org.acitech.entities.enemies.Rico;
import org.acitech.inventory.ItemStack;
import org.acitech.inventory.ItemType;
import org.acitech.tilemap.Room;
import org.acitech.tilemap.Tile;
import org.acitech.utils.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    final int screenWidth = 800;
    final int screenHeight = 600;
    final int fps = 60;
    private boolean paused = false;
    private boolean unEscaped = false;
    private ArrayList<Entity> newEntities = new ArrayList<>();

    KeyHandler keys = new KeyHandler();
    Thread gameThread;

    public static ArrayList<Entity> entities = new ArrayList<>();
    public static Player player = new Player();
    public static Vector2d camera = new Vector2d(0, 0);
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

        // Create test enemies for no reason ¯\_(ツ)_/¯
        for (int i = 0; i < 5; i++) {
            addNewEntity(new Rico(Math.random() * screenWidth + 400, Math.random() * screenHeight));
        }

        // Test Pepto
        for (int i = 0; i < 5; i++) {
            addNewEntity(new Pepto(Math.random() * screenWidth + 400, Math.random() * screenHeight));
        }

        // Test Jordan
        for (int i = 0; i < 10; i++) {
            addNewEntity(new Jordan(Math.random() * screenWidth + 400, Math.random() * screenHeight));
        }

        // Test Fire Tome
        for (int i = 0; i < 10; i++) {
            addNewEntity(new Item(Math.random() * screenWidth + 400, Math.random() * screenHeight, new ItemStack(ItemType.FIRE_TOME_1)));
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

        if (!paused) {
            ArrayList<Entity> disposedEntities = new ArrayList<>();
            ArrayList<Item> pickupItems = new ArrayList<>();

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
                    if (itemEntity.isInPickupRange() && !itemEntity.isDisappearing()) {
                        pickupItems.add(itemEntity);
                    }
                }
            }

            // Tick the player
            player.tickEntity(delta);

            // Pick up items and make them disappear
            ArrayList<Item> itemsPickedUp = player.pickupItems(pickupItems);

            for (Item item : itemsPickedUp) {
                item.disappear();
            }

            // Loop through each disposed entity/item and remove them
            for (Entity entity : disposedEntities) {
                entities.remove(entity);
            }

            // Clear the list of mouse clicks
            KeyHandler.mouseClicks.clear();

            // Check for pausing
            if (!KeyHandler.escDown) {
                unEscaped = true;
            }

            if (unEscaped && KeyHandler.escDown) {
                paused = true;
                unEscaped = false;
            }
        }

        else {
            // Check for pausing
            if (!KeyHandler.escDown) {
                unEscaped = true;
            }

            

            if (unEscaped && KeyHandler.escDown) {
                paused = false;
                unEscaped = false;
            }
        }


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

        camera = new Vector2d(camera.getX() + offsetX, camera.getY() + offsetY);
//        camera = new Vector2d(player.position.getX() - this.getWidth() / 2d, player.position.getY() - this.getHeight() / 2d);
    }

    public Vector2d getCameraCenter() {
        return new Vector2d(
                player.position.getX() - player.width / 2d - camera.getX(),
                player.position.getY() - player.height / 2d - camera.getY()
        );
    }
}
