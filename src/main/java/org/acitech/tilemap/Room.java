package org.acitech.tilemap;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.Enemy;
import org.acitech.entities.Entity;
import org.acitech.entities.Item;
import org.acitech.utils.Caboodle;
import org.acitech.utils.Vector2d;
import org.spongepowered.noise.module.source.Simplex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

abstract public class Room {

    private final Tile[][] tilemap;
    private final Connector[][][] ctmmap;
    private final Vector2d cloudOffset;
    private double cloudAngle;
    private final Random cloudRng;
    protected final int maxWidth;
    protected final int maxHeight;
    private final Color skyColor;
    private final Random seedRng;
    private final Simplex tuftSimplex;
    protected final Simplex terrainSimplex;
    private final HashSet<Entity> newEntities = new HashSet<>();
    private final HashSet<Entity> entities = new HashSet<>();

    public Room(int maxWidth, int maxHeight, int seed) {
        this.tilemap = new Tile[maxWidth][maxHeight];
        this.ctmmap = new Connector[maxWidth][maxHeight][];
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.skyColor = new Color(0x413552);
        this.cloudOffset = new Vector2d();
        this.cloudAngle = 0;

        // Create random noise instances
        this.seedRng = new Random(seed);
        this.cloudRng = new Random(seedRng.nextInt());
        this.tuftSimplex = new Simplex();
        this.tuftSimplex.setSeed(seedRng.nextInt());
        this.terrainSimplex = new Simplex();
        this.terrainSimplex.setSeed(seedRng.nextInt());

        // Generate the tilemap
        generateTilemap();

        // Generate the entities
        generateEntities();
    }

    protected abstract void generateTilemap();

    protected abstract void generateEntities();

    public void tick(double delta) {
        ArrayList<Entity> disposedEntities = new ArrayList<>();
        ArrayList<Item> pickupItems = new ArrayList<>();

        // Add newly created entities
        flushNewEntities();

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

        // Pick up items and make them disappear
        ArrayList<Item> itemsPickedUp = GamePanel.getPlayer().pickupItems(pickupItems);

        for (Item item : itemsPickedUp) {
            item.disappear();
        }

        // Loop through each disposed entity/item and remove them
        for (Entity entity : disposedEntities) {
            entities.remove(entity);
        }
    }

    public void addNewEntity(Entity entity) {
        this.newEntities.add(entity);
    }

    /** Force adds new entities into the entities list */
    public void flushNewEntities() {
        entities.addAll(newEntities);
        newEntities.clear();
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public HashSet<Entity> getEntities() {
        return this.entities;
    }

    /** @return Whether the room is clear of enemies */
    public boolean isRoomClear() {
        for (Entity entity : this.entities) {
            if (entity instanceof Enemy) {
                return false;
            }
        }

        return true;
    }

    private void drawBackground(Graphics2D ctx) {
        ctx.setColor(this.skyColor);
        ctx.fillRect(0, 0, Main.getGamePanel().getWidth(), Main.getGamePanel().getHeight());

        BufferedImage image = Main.getResources().getTexture("environment/clouds");
        int size = Math.max(Main.getGamePanel().getWidth(), Main.getGamePanel().getHeight());
        int span = size / Tile.tileSize * Tile.tileSize / 4;

        cloudOffset.set(Caboodle.wrap(cloudOffset.getX(), 0, image.getWidth() - span), Caboodle.wrap(cloudOffset.getY(), 0, image.getHeight() - span));

        ctx.drawImage(image, 0, 0, size, size, (int) cloudOffset.getX(), (int) cloudOffset.getY(), (int) cloudOffset.getX() + span, (int) cloudOffset.getY() + span, Main.getGamePanel());

        double x = Math.cos(cloudAngle) * 0.5;
        double y = Math.sin(cloudAngle) * 0.5;

        cloudOffset.add(x, y);
        cloudAngle += cloudRng.nextDouble(-0.1, 0.1);
        cloudAngle = cloudAngle % (Math.PI * 2);
    }

    public void draw(Graphics2D ctx) {
        drawBackground(ctx);

        Vector2d upperBounds = Main.getGamePanel().getUpperFrameBounds();
        Vector2d lowerBounds = Main.getGamePanel().getLowerFrameBounds();

        int widthP = (int) Caboodle.clamp(Math.ceil(upperBounds.getX() / Tile.tileSize), 0, maxWidth);
        int widthN = (int) Caboodle.clamp(Math.floor(lowerBounds.getX() / Tile.tileSize), 0, maxWidth);
        int heightP = (int) Caboodle.clamp(Math.ceil(upperBounds.getY() / Tile.tileSize), 0, maxHeight);
        int heightN = (int) Caboodle.clamp(Math.floor(lowerBounds.getY() / Tile.tileSize), 0, maxHeight);

        // Loop over the room's tilemap
        for (int x = widthN; x < widthP; x++) {
            for (int y = heightN; y < heightP; y++) {
                Tile tile = tilemap[x][y]; // Get the tile at the iterated coordinate

                // If the tile is defined, draw it
                if (tile != null) {
                    BufferedImage image = tile.getFullTexture();
                    ctx.drawImage(image, x * Tile.tileSize - (int) GamePanel.getCamera().getX(), y * Tile.tileSize - (int) GamePanel.getCamera().getY(), Tile.tileSize, Tile.tileSize, Main.getGamePanel());

                    if (tile.getId().equals(Tile.grass.getId())) {
                        double tuftValue = this.tuftSimplex.get(x * 10, y * 10, 0);

                        if (tuftValue > 0.15) {
                            BufferedImage imageTufts = Main.getResources().getTexture("environment/grass_tufts/" + ((int) (tuftValue * 10e7) % 8) + ":0");
                            ctx.drawImage(imageTufts, x * Tile.tileSize - (int) GamePanel.getCamera().getX(), y * Tile.tileSize - (int) GamePanel.getCamera().getY(), Tile.tileSize, Tile.tileSize, Main.getGamePanel());
                        }
                    }

                    if (tile.getId().equals(Tile.dirt.getId())) {
                        double tuftValue = this.tuftSimplex.get(x, y, 0);

                        if (tuftValue > 0.15) {
                            BufferedImage imageTufts = Main.getResources().getTexture("environment/pebbles/" + ((int) (tuftValue * 10e7) % 4) + ":0");
                            ctx.drawImage(imageTufts, x * Tile.tileSize - (int) GamePanel.getCamera().getX(), y * Tile.tileSize - (int) GamePanel.getCamera().getY(), Tile.tileSize, Tile.tileSize, Main.getGamePanel());
                        }
                    }
                }
            }
        }

        for (int x = widthN; x < widthP; x++) {
            for (int y = heightN; y < heightP; y++) {
                Tile tile = tilemap[x][y]; // Get the tile at the iterated coordinate
                Connector[] connectors = ctmmap[x][y];

                if (connectors == null) continue;

                // Loop through every connector on the tile
                for (Connector connector : connectors) {
                    BufferedImage image = tile.getTexture(connector);
                    Vector2d position = connector.getOffset().add(x, y).multiply(Tile.tileSize);

                    ctx.drawImage(image, (int) position.getX() - (int) GamePanel.getCamera().getX(), (int) position.getY() - (int) GamePanel.getCamera().getY(), Tile.tileSize, Tile.tileSize, Main.getGamePanel());
                }
            }
        }

        // Loop through each entity in a cloned list of entities and draw them
        for (Entity entity : new ArrayList<>(entities)) {
            entity.draw(ctx);
        }
    }

    /** Checks if a specific tile type is at specific coordinates */
    private boolean matchConnector(String tileId, int x, int y) {
        Tile tile = getTile(x, y);

        return tile == null || !tile.getId().equals(tileId);
    }

    private void computeCtm(int x, int y) {
        Tile tile = getTile(x, y);

        if (tile == null) return;

        String tileId = tile.getId();
        ArrayList<Connector> connectorPossibilities = new ArrayList<>();

        if (matchConnector(tileId, x, y - 1)) {
            connectorPossibilities.add(Connector.TOP_SIDE);
        }
        if (matchConnector(tileId, x, y + 1)) {
            connectorPossibilities.add(Connector.BOTTOM_SIDE);
        }
        if (matchConnector(tileId, x + 1, y)) {
            connectorPossibilities.add(Connector.RIGHT_SIDE);
        }
        if (matchConnector(tileId, x - 1, y)) {
            connectorPossibilities.add(Connector.LEFT_SIDE);
        }

        if (matchConnector(tileId, x - 1, y + 1)) {
            connectorPossibilities.add(Connector.DL_EDGE);
        }
        if (matchConnector(tileId, x - 1, y - 1)) {
            connectorPossibilities.add(Connector.UL_EDGE);
        }
        if (matchConnector(tileId, x + 1, y + 1)) {
            connectorPossibilities.add(Connector.DR_EDGE);
        }
        if (matchConnector(tileId, x + 1, y - 1)) {
            connectorPossibilities.add(Connector.UR_EDGE);
        }

        // If there is only one connector possibility left then append it to the connector map
        if (!connectorPossibilities.isEmpty()) {
            Connector[] nodes = new Connector[connectorPossibilities.size()];

            for (int i = 0; i < connectorPossibilities.size(); i++) {
                nodes[i] = connectorPossibilities.get(i);
            }

            ctmmap[x][y] = nodes;
        }
    }

    public void setTile(int x, int y, Tile tile) {
        tilemap[x][y] = tile;

        // Calculate the nearby connected textures
        for (int cx = x - 2; cx < x + 2; cx++) { // TODO: figure out why a expansion of 2 works instead of just 1
            for (int cy = y - 2; cy < y + 2; cy++) {
                computeCtm(cx, cy);
            }
        }
    }

    public Tile getTile(int x, int y) {
        try { // TODO: do math instead of catch
            return tilemap[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public int getWidth() {
        return this.maxWidth;
    }

    public int getHeight() {
        return this.maxHeight;
    }

}
