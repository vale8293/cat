package org.acitech.entities.enemies;

import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;
import org.acitech.tilemap.Room;

public class Rico extends Enemy {

    public Rico(Room room, double startX, double startY) {
        super(room, startX, startY, "Rico", "Fighter");

        // Animation & Visuals
        this.aniLength = 6;
        this.aniFrameDuration = 6;
        this.width = 160;
        this.height = 160;

        // Stats
        this.maxHealth = 5;
        this.health = maxHealth;
        this.aggroDistance = 400;

        // Rewards
        this.xpDrop = 5;

        // Item Pool (Water: 1/1)
        this.itemPool.add(ItemType.WATER);
    }
}