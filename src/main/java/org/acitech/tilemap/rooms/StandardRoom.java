package org.acitech.tilemap.rooms;

import org.acitech.entities.enemies.Jordan;
import org.acitech.entities.enemies.Pepto;
import org.acitech.entities.enemies.Rico;
import org.acitech.tilemap.Room;
import org.acitech.tilemap.Tile;
import org.acitech.utils.Vector2d;

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
                    setTile(x, y, Tile.grass);
                } else if (noise > 0.3333) {
                    setTile(x, y, Tile.dirt);
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
