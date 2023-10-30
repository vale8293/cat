package org.acitech.tilemap;

public enum Connector {
    /**
     * The side on top of a full tile
     */
    TOP_SIDE(1, 0),
    BOTTOM_SIDE(1, 2),
    LEFT_SIDE(0, 1),
    RIGHT_SIDE(2, 1),
    UR_EDGE(2, 0),
    UL_EDGE(0, 0),
    DR_EDGE(2, 2),
    DL_EDGE(0, 2);

    private final int x;
    private final int y;

    Connector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
