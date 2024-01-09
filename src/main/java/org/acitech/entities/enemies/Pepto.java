package org.acitech.entities.enemies;

import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;

import java.awt.*;

public class Pepto extends Enemy {

    public Pepto(double startX, double startY) {
        super(startX, startY, "Pepto");

        this.aniLength = 3;
        this.aniFrameDuration = 8;
        this.width = 160;
        this.height = 160;
        this.maxHealth = 3;
        this.health = maxHealth;
        this.moveSpeed = 0.8;
        this.aggroDistance = 400;
        this.xpDrop = 3;
        this.itemPool.add(ItemType.FEATHER);
    }

    @Override
    protected void tick(double delta) {
        super.tick(delta);
    }

    @Override
    public void draw(Graphics2D ctx) {
        super.draw(ctx);
    }
}