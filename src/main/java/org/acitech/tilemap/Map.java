package org.acitech.tilemap;

import java.util.ArrayList;
import java.util.Random;

public class Map {

    private final ArrayList<Room> rooms = new ArrayList<>();
    private int currentRoomIndex = -1;
    private final Random seedRng;

    public Map(int seed) {
        this.seedRng = new Random(seed);
    }

    public void generateRooms(int amount) {
        for (int i = 0; i < amount; i++) {
            Room room = new Room(20, 20, this.seedRng.nextInt());
            rooms.add(room);
        }

        if (currentRoomIndex == -1) {
            currentRoomIndex = 0;
        }
    }

    public Room getRoom(int index) {
        return rooms.get(index);
    }

    public Room getCurrentRoom() {
        return rooms.get(this.currentRoomIndex);
    }

    public void changeRoom(int index) {
        this.currentRoomIndex = index;
    }
}
