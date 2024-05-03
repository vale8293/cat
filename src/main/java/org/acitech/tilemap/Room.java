package org.acitech.tilemap;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.utils.Caboodle;
import org.acitech.utils.Vector2d;
import org.spongepowered.noise.module.source.Simplex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Room {

    private final Tile[][] tilemap;
    private final Connector[][][] ctmmap;
    private Vector2d cloudOffset;
    private double cloudAngle;
    private Random cloudRng;
    private final int maxWidth;
    private final int maxHeight;
    private final Color skyColor;
    private final Random seedRng;
    private final Simplex tuftSimplex;
    private final Simplex terrainSimplex;

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

        // Generate the map
        generate();
    }

    private void generate() {
        Vector2d centerMap = new Vector2d(this.maxWidth / 2d, this.maxHeight / 2d);

        for (int x = 0; x < this.maxWidth; x++) {
            for (int y = 0; y < this.maxHeight; y++) {
                if (centerMap.distance(new Vector2d(x, y)) < this.maxWidth / 2d) {
                    double noise = this.terrainSimplex.get((double) x / 10, (double) y / 10, 0);

                    if (noise > 1) {
                        setTile(x, y, Tile.dirt);
                    } else {
                        setTile(x, y, Tile.grass);
                    }
                }
            }
        }

        setTile(0, 0, Tile.grass);

        setTile(3, 0, Tile.grass);
        setTile(2, 0, Tile.grass);

        setTile(3, 3, Tile.grass);
        setTile(2, 2, Tile.grass);

        setTile(0, 3, Tile.grass);
        setTile(0, 2, Tile.grass);
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

        // Create a collection of tile id's mapped to secondary tile-maps
//        HashMap<String, Connector[][][]> connCache = new HashMap<>();

        // Initialize the collection of tile-maps for each connectable tile type
//        for (Tile tile : Tile.getConnectedTiles()) {
//            connCache.put(tile.getId(), new Connector[maxWidth][maxHeight][]);
//        }

        // Loop over the room's tilemap
        for (int x = widthN; x < widthP; x++) {
            for (int y = heightN; y < heightP; y++) {
                Tile tile = tilemap[x][y]; // Get the tile at the iterated coordinate

                // If the tile is defined, draw it
                if (tile != null) {
                    BufferedImage image = tile.getFullTexture();
                    ctx.drawImage(image, x * Tile.tileSize - (int) GamePanel.camera.getX(), y * Tile.tileSize - (int) GamePanel.camera.getY(), Tile.tileSize, Tile.tileSize, Main.getGamePanel());

                    if (tile.getId().equals(Tile.grass.getId())) {
                        double tuftValue = this.tuftSimplex.get(x * 10, y * 10, 0);

                        if (tuftValue > 0.15) {
                            BufferedImage imageTufts = Main.getResources().getTexture("environment/grass_tufts/" + ((int) (tuftValue * 10e7) % 8) + ":0");
                            ctx.drawImage(imageTufts, x * Tile.tileSize - (int) GamePanel.camera.getX(), y * Tile.tileSize - (int) GamePanel.camera.getY(), Tile.tileSize, Tile.tileSize, Main.getGamePanel());
                        }
                    }

                    if (tile.getId().equals(Tile.dirt.getId())) {
                        double tuftValue = this.tuftSimplex.get(x, y, 0);

                        if (tuftValue > 0.15) {
                            BufferedImage imageTufts = Main.getResources().getTexture("environment/pebbles/" + ((int) (tuftValue * 10e7) % 4) + ":0");
                            ctx.drawImage(imageTufts, x * Tile.tileSize - (int) GamePanel.camera.getX(), y * Tile.tileSize - (int) GamePanel.camera.getY(), Tile.tileSize, Tile.tileSize, Main.getGamePanel());
                        }
                    }
                }

                // Loop through each connectable tile type
//                for (String connTileId : connCache.keySet()) {
//                    if (tile != null && tile.getId().equals(connTileId)) continue; // Skip iteration if tile is the same type as the iterated connectable tile type
//
//                    ArrayList<Connector> connectorPossibilities = new ArrayList<>(List.of(Connector.values()));
//
//                    if (!checkTileType(connTileId, x, y + 1)) {
//                        connectorPossibilities.remove(Connector.TOP_SIDE);
//                    }
//                    if (!checkTileType(connTileId, x, y - 1)) {
//                        connectorPossibilities.remove(Connector.BOTTOM_SIDE);
//                    }
//                    if (!checkTileType(connTileId, x - 1, y)) {
//                        connectorPossibilities.remove(Connector.RIGHT_SIDE);
//                    }
//                    if (!checkTileType(connTileId, x + 1, y)) {
//                        connectorPossibilities.remove(Connector.LEFT_SIDE);
//                    }
//
//                    if (!checkTileType(connTileId, x + 1, y - 1)) {
//                        connectorPossibilities.remove(Connector.DL_EDGE);
//                    }
//                    if (!checkTileType(connTileId, x + 1, y + 1)) {
//                        connectorPossibilities.remove(Connector.UL_EDGE);
//                    }
//                    if (!checkTileType(connTileId, x - 1, y - 1)) {
//                        connectorPossibilities.remove(Connector.DR_EDGE);
//                    }
//                    if (!checkTileType(connTileId, x - 1, y + 1)) {
//                        connectorPossibilities.remove(Connector.UR_EDGE);
//                    }
//
//                    // If there is only one connector possibility left then append it to the connector map
//                    if (connectorPossibilities.size() > 0) {
//                        Connector[][][] connMap = connCache.get(connTileId);
//
//                        // Convert the arraylist of connectors to a multidimensional array
//                        Connector[] connectors = new Connector[connectorPossibilities.size()];
//                        int i = 0;
//                        for (Connector con : connectorPossibilities) {
//                            connectors[i] = con;
//                            i++;
//                        }
//                        connMap[x][y] = connectors;
//
//                        // Set the connector map
//                        connCache.put(connTileId, connMap);
//                    }
//                }
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

                    ctx.drawImage(image, (int) position.getX() - (int) GamePanel.camera.getX(), (int) position.getY() - (int) GamePanel.camera.getY(), Tile.tileSize, Tile.tileSize, Main.getGamePanel());
                }
            }
        }
    }

    /**
     * Checks if a specific tile type is at specific coordinates
     */
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
        if (connectorPossibilities.size() > 0) {
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
        return maxWidth;
    }

    public int getHeight() {
        return maxHeight;
    }
}
