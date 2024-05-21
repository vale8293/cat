package org.acitech.cat.tilemap;

import org.acitech.cat.GamePanel;
import org.acitech.cat.tilemap.rooms.BossRoom;
import org.acitech.cat.tilemap.rooms.StandardRoom;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Map {

    private final ArrayList<Room> rooms = new ArrayList<>();
    private final Random seedRng;

    public Map(int seed) {
        this.seedRng = new Random(seed);

        for (int i = 0; i < 2; i++) {
            StandardRoom room = new StandardRoom(21, 21, this.seedRng.nextInt());
            room.flushNewEntities();
            rooms.add(room);
        }

        BossRoom room = new BossRoom(21, 21, this.seedRng.nextInt());
        room.flushNewEntities();
        rooms.add(room);
    }

    /** Tick the current room */
    public void tick(double delta) {
        getCurrentRoom().tick(delta);
    }

    /** Draw the current room */
    public void draw(Graphics2D ctx) {
        getCurrentRoom().draw(ctx);
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public Room getCurrentRoom() {
        return GamePanel.getPlayer().getRoom();
    }
}
