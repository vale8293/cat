package org.acitech.entities.enemies;

import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;

public class Jordan extends Enemy {

    public Jordan(double startX, double startY) {
        super(startX, startY, "Jordan", "Skitter");

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