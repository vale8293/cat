package org.acitech.tilemap.rooms;

import org.acitech.entities.enemies.Rico;
import org.acitech.tilemap.Room;
import org.acitech.tilemap.Tile;
import org.acitech.utils.Vector2d;

public class BossRoom extends Room {

    public BossRoom(int maxWidth, int maxHeight, int seed) {
        super(maxWidth, maxHeight, seed);
    }

    @Override
    protected void generateTilemap() {
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
    }

    @Override
    protected void generateEntities() {
        // TODO: change to boss enemy
        new Rico(this, Math.random() * getWidth() * Tile.tileSize, Math.random() * getHeight() * Tile.tileSize);
    }
}
