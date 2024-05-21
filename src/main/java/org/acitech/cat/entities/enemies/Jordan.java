package org.acitech.cat.entities.enemies;

import org.acitech.cat.entities.Enemy;
import org.acitech.cat.inventory.ItemType;
import org.acitech.cat.tilemap.Room;

public class Jordan extends Enemy {

    public Jordan(Room room, double startX, double startY) {
        super(room, startX, startY, "Jordan", "Skitter");

        // Animation & Visuals
        this.aniLength = 6;
        this.aniFrameDuration = 3;
        this.width = 80;
        this.height = 80;

        // Stats
        this.maxHealth = 3;
        this.health = maxHealth;
        this.moveSpeed = 1.3;
        this.kbMult = 20;
        this.aggroDistance = 500;

        // Rewards
        this.xpDrop = 5;

        // Item Pool (PLACEHOLDER String: 1/1)
        this.itemPool.add(ItemType.STRING);
    }
}