package org.acitech;

import java.awt.image.BufferedImage;

public class Tile {

    // Define the static tiles
    public static Tile grass = new Tile("grass", "environment/grass", true);



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
        return connected ? getTexture(1, 1) : getTexture();
    }

    public BufferedImage getTexture(int x, int y) {
        return Main.getResources().getTexture(textureKey + "/" + x + ":" + y);
    }

    public boolean isConnected() {
        return connected;
    }
}
