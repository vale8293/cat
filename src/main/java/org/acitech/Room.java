package org.acitech;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Room {

    private final Tile[][] tilemap;
    private final int width;
    private final int height;

    public Room(int width, int height) {
        this.tilemap = new Tile[width][height];
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D ctx) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = tilemap[x][y];
                if (tile == null) continue;

                BufferedImage image = tile.getFullTexture();

                ctx.drawImage(image, x * 64, y * 64, 64, 64, Main.getGamePanel());
            }
        }
    }

    public void setTile(int x, int y, Tile tile) {
        tilemap[x][y] = tile;
    }

}
