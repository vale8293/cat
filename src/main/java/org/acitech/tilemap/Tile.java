package org.acitech.tilemap;

import org.acitech.Main;

import java.awt.image.BufferedImage;
import java.util.List;

public class Tile {

    // Define the static tiles
    public static Tile grass = new Tile("grass", "environment/grass", true);
    public static Tile dirt = new Tile("dirt", "environment/dirt", false);

    public static final List<Tile> TILE_LIST = List.of(grass);

    public static Tile getTileById(String id) {
        return TILE_LIST.stream().filter(t -> t.id.equals(id)).findFirst().orElse(null);
    }

    public static List<Tile> getConnectedTiles() {
        return TILE_LIST.stream().filter(Tile::isConnected).toList();
    }

    // Tile constructor
    private final String id;
    private final String textureKey;
    private final boolean connected;

    public Tile(String id, String textureKey, boolean connected) {
        this.id = id;
        this.textureKey = textureKey;
        this.connected = connected;
    }

    public String getId() {
        return id;
    }

    public BufferedImage getTexture() {
        return Main.getResources().getTexture(textureKey);
    }

    public BufferedImage getFullTexture() {
        return connected ? Main.getResources().getTexture(textureKey + "/1:1") : getTexture();
    }

    public BufferedImage getTexture(Connector connector) {
        return Main.getResources().getTexture(textureKey + "/" + connector.getX() + ":" + connector.getY());
    }

    public boolean isConnected() {
        return connected;
    }
}
