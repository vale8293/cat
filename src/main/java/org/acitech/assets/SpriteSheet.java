package org.acitech.assets;

import java.awt.image.BufferedImage;

public class SpriteSheet {

    private final BufferedImage[][] sprites;
    private final int rows;
    private final int cols;

    public SpriteSheet(BufferedImage sheet, int width, int height) {
        int sheetWidth = sheet.getWidth();
        int sheetHeight = sheet.getHeight();

        if (sheetWidth % width != 0 || sheetHeight % height != 0) {
            throw new RuntimeException("Sprite sheet is improperly sized");
        }

        this.rows = sheetWidth / width;
        this.cols = sheetHeight / height;

        sprites = new BufferedImage[this.rows][this.cols];

        for (int x = 0; x < this.rows; x++) {
            for (int y = 0; y < this.cols; y++) {
                BufferedImage sprite = sheet.getSubimage(x * width, y * height, width, height);
                sprites[x][y] = sprite;
            }
        }
    }

    public BufferedImage getSprite(int x, int y) {
        if (x < 0 || x >= this.rows || y < 0 || y >= this.cols) return null;

        return sprites[x][y];
    }
}
