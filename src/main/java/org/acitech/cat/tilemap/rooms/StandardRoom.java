package org.acitech.cat.tilemap.rooms;

import org.acitech.cat.entities.enemies.Jordan;
import org.acitech.cat.entities.enemies.Pepto;
import org.acitech.cat.entities.enemies.Rico;
import org.acitech.cat.tilemap.Room;
import org.acitech.cat.tilemap.Tile;
import org.acitech.cat.utils.Vector2d;

import java.util.ArrayList;

public class StandardRoom extends Room {

    public StandardRoom(int maxWidth, int maxHeight, int seed) {
        super(maxWidth, maxHeight, seed);
    }

    @Override
    protected void generateTilemap() {
        for (int x = 0; x < this.maxWidth; x++) {
            for (int y = 0; y < this.maxHeight; y++) {
                double noise = this.terrainSimplex.get((double) x / 10, (double) y / 10, 0);

                if (noise > 0.6666) {
                    setTile(x, y, Tile.GRASS);
                } else if (noise > 0.3333) {
                    setTile(x, y, Tile.DIRT);
                }
            }
        }
    }

    @Override
    protected void generateEntities() {
        ArrayList<Vector2d> safeSpawns = this.getSafeTiles();

        // Test Rico
        for (int i = 0; i < 5; i++) {
            Vector2d pos = safeSpawns.get(spawnRng.nextInt(safeSpawns.size()));
            new Rico(this, pos.getX(), pos.getY());
        }

        // Test Pepto
        for (int i = 0; i < 5; i++) {
            Vector2d pos = safeSpawns.get(spawnRng.nextInt(safeSpawns.size()));
            new Pepto(this, pos.getX(), pos.getY());
        }

        // Test Jordan
        for (int i = 0; i < 5; i++) {
            Vector2d pos = safeSpawns.get(spawnRng.nextInt(safeSpawns.size()));
            new Jordan(this, pos.getX(), pos.getY());
        }
    }
}
