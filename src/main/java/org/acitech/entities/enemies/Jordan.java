package org.acitech.entities.enemies;

import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;

public class Jordan extends Enemy {

    public Jordan(double startX, double startY) {
        super(startX, startY, "Jordan");

        // Animation & Visuals
        this.aniLength = 5;
        this.aniFrameDuration = 5;
        this.width = 160;
        this.height = 160;

        // Stats
        this.maxHealth = 3;
        this.health = maxHealth;
        this.moveSpeed = 1.3;
        this.kbMult = 25;
        this.aggroDistance = 500;

        // Rewards
        this.xpDrop = 5;

        // Item Pool (PLACEHOLDER String: 1/1)
        this.itemPool.add(ItemType.STRING);
    }

    // Changes AI to flee after being attacked
//    @Override
//    protected void tick(double delta) {
//        super.tick(delta);
//    }
//
}