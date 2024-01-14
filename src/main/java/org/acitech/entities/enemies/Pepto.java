package org.acitech.entities.enemies;

import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;

import java.awt.*;

public class Pepto extends Enemy {

    public Pepto(double startX, double startY) {
        super(startX, startY, "Pepto");

        // Animation & Visuals
        this.aniLength = 3;
        this.aniFrameDuration = 8;
        this.width = 160;
        this.height = 160;

        // Stats
        this.maxHealth = 3;
        this.health = maxHealth;
        this.moveSpeed = 0.8;
        this.kbMult = 30;
        this.aggroDistance = 400;
        this.xpDrop = 3;

        // Item Pool (Feather: 1/1)
        this.itemPool.add(ItemType.FEATHER);
    }

    // TODO: I don't think this is needed but am too afraid to remove it for good
//    @Override
//    protected void tick(double delta) {
//        super.tick(delta);
//    }
//
//    @Override
//    public void draw(Graphics2D ctx) {
//        super.draw(ctx);
//    }
}