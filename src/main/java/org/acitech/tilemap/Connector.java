package org.acitech.tilemap;

import org.acitech.utils.Vector2d;

public enum Connector {
    /**
     * The side on top of a full tile
     */
    TOP_SIDE(1, 0, 0, -1),
    BOTTOM_SIDE(1, 2, 0, 1),
    LEFT_SIDE(0, 1, -1, 0),
    RIGHT_SIDE(2, 1, 1, 0),
    UR_EDGE(2, 0, 1, -1),
    UL_EDGE(0, 0, -1, -1),
    DR_EDGE(2, 2, 1, 1),
    DL_EDGE(0, 2, -1, 1);

    private final int textureX;
    private final int textureY;
    private final Vector2d offset;

    Connector(int textureX, int textureY, int offsetX, int offsetY) {
        this.textureX = textureX;
        this.textureY = textureY;
        this.offset = new Vector2d(offsetX, offsetY);
    }

    public int getTextureX() {
        return textureX;
    }
    public int getTextureY() {
        return textureY;
    }
    public Vector2d getOffset() {
        return offset.copy();
    }
}
