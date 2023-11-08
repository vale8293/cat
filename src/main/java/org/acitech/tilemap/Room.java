package org.acitech.tilemap;

import org.acitech.Main;
import org.spongepowered.noise.module.source.Simplex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Room {

    private final Tile[][] tilemap;
    private final int width;
    private final int height;
    private final Simplex simplex;

    private final int tileSize = 64;

    public Room(int width, int height, int seed) {
        this.tilemap = new Tile[width][height];
        this.width = width;
        this.height = height;
        this.simplex = new Simplex();
        this.simplex.setSeed(seed);
    }

    public void draw(Graphics2D ctx) {
        // Create a collection of tile id's mapped to secondary tile-maps
        HashMap<String, Connector[][][]> connCache = new HashMap<>();

        // Initialize the collection of tile-maps for each connectable tile type
        for (Tile tile : Tile.getConnectedTiles()) {
            connCache.put(tile.getId(), new Connector[width][height][]);
        }

        Random tuftsRng = new Random(this.getSimplex().seed() * 87832L);

        // Loop over the room's tilemap
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = tilemap[x][y]; // Get the tile at the iterated coordinate

                // If the tile is defined, draw it
                if (tile != null) {
                    BufferedImage image = tile.getFullTexture();
                    ctx.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize, Main.getGamePanel());

                    if (tile.getId().equals(Tile.grass.getId())) {
                        if (this.getSimplex().get((double) x * 10, (double) y * 10, 200) > 0.8) {
                            BufferedImage imageTufts = Main.getResources().getTexture("environment/grass_tufts/" + tuftsRng.nextInt(8) + ":0");
                            ctx.drawImage(imageTufts, x * tileSize, y * tileSize, tileSize, tileSize, Main.getGamePanel());
                        }
                    }
                }

                // Loop through each connectable tile type
                for (String connTileId : connCache.keySet()) {
                    if (tile != null && tile.getId().equals(connTileId)) continue; // Skip iteration if tile is the same type as the iterated connectable tile type

                    ArrayList<Connector> connectorPossibilities = new ArrayList<>(List.of(Connector.values()));

                    if (!checkTileType(connTileId, x, y + 1)) {
                        connectorPossibilities.remove(Connector.TOP_SIDE);
                    }
                    if (!checkTileType(connTileId, x, y - 1)) {
                        connectorPossibilities.remove(Connector.BOTTOM_SIDE);
                    }
                    if (!checkTileType(connTileId, x - 1, y)) {
                        connectorPossibilities.remove(Connector.RIGHT_SIDE);
                    }
                    if (!checkTileType(connTileId, x + 1, y)) {
                        connectorPossibilities.remove(Connector.LEFT_SIDE);
                    }

                    if (!checkTileType(connTileId, x + 1, y - 1)) {
                        connectorPossibilities.remove(Connector.DL_EDGE);
                    }
                    if (!checkTileType(connTileId, x + 1, y + 1)) {
                        connectorPossibilities.remove(Connector.UL_EDGE);
                    }
                    if (!checkTileType(connTileId, x - 1, y - 1)) {
                        connectorPossibilities.remove(Connector.DR_EDGE);
                    }
                    if (!checkTileType(connTileId, x - 1, y + 1)) {
                        connectorPossibilities.remove(Connector.UR_EDGE);
                    }

                    // If there is only one connector possibility left then append it to the connector map
                    if (connectorPossibilities.size() > 0) {
                        Connector[][][] connMap = connCache.get(connTileId);

                        // Convert the arraylist of connectors to a multidimensional array
                        Connector[] connectors = new Connector[connectorPossibilities.size()];
                        int i = 0;
                        for (Connector con : connectorPossibilities) {
                            connectors[i] = con;
                            i++;
                        }
                        connMap[x][y] = connectors;

                        // Set the connector map
                        connCache.put(connTileId, connMap);
                    }
                }
            }
        }

        for (String connTileId : connCache.keySet()) {
            Tile connTile = Tile.getTileById(connTileId);
            Connector[][][] connMap = connCache.get(connTileId);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Connector[] connectors = connMap[x][y];

                    if (connectors == null) continue;

                    for (Connector connector : connectors) {
                        BufferedImage image = connTile.getTexture(connector);
                        ctx.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize, Main.getGamePanel());
                    }
                }
            }
        }
    }

    /**
     * Checks if a specific tile type is at specific coordinates
     */
    private boolean checkTileType(String tileId, int x, int y) {
        Tile tile;

        try {
            tile = tilemap[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            tile = null;
        }

        if (tile == null) return tileId == null;

        return tile.getId().equals(tileId);
    }

    public void setTile(int x, int y, Tile tile) {
        tilemap[x][y] = tile;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Simplex getSimplex() {
        return simplex;
    }
}
