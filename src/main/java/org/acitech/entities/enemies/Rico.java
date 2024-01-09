package org.acitech.entities.enemies;

import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;

public class Rico extends Enemy {

    public Rico(double startX, double startY) {
        super(startX, startY, "Rico");

        this.width = 160;
        this.height = 160;
        this.maxHealth = 5;
        this.health = maxHealth;
        this.aggroDistance = 400;
        this.xpDrop = 5;
        this.aniLength = 6;
        this.aniFrameDuration = 6;
        this.itemPool.add(ItemType.WATER);
    }
}