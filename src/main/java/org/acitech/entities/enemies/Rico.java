package org.acitech.entities.enemies;

import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;

public class Rico extends Enemy {

    public Rico(double startX, double startY) {
        super(startX, startY, "Rico", "Fighter");

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

    @Override
    protected void scratchHandler() {

    }
}