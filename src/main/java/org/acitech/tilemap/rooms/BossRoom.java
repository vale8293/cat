package org.acitech.tilemap.rooms;

import org.acitech.entities.enemies.Pteri;
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
                if (centerMap.distance(new Vector2d(x, y)) < Math.max(this.maxWidth, this.maxHeight) / 2d) {
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
        new Pteri(this, 19 * Tile.tileSize, 11 * Tile.tileSize);
    }
}
