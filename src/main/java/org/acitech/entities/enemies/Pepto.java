package org.acitech.entities.enemies;

import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;

public class Pepto extends Enemy {

    public Pepto(double startX, double startY) {
        super(startX, startY, "Pepto", "Fighter");

        // Animation & Visuals
        this.aniLength = 3;
        this.aniFrameDuration = 8;
        this.width = 120;
        this.height = 120;

        // Stats
        this.maxHealth = 3;
        this.health = maxHealth;
        this.moveSpeed = 0.8;
        this.kbMult = 30;
        this.aggroDistance = 400;

        // Rewards
        this.xpDrop = 3;

        // Item Pool (Feather: 1/1)
        this.itemPool.add(ItemType.FEATHER);
    }
}