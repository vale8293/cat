package org.acitech.entities.enemies;

import org.acitech.Main;
import org.acitech.entities.Enemy;
import org.acitech.entities.Item;
import org.acitech.inventory.ItemStack;
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
        // this.itemPool.add(ItemType.WATER);
    }

    @Override
    protected void tick(double delta) {
        super.tick(delta);

        // If pepto dies, get rid of the pepto, todo: play an animation
        if (this.health <= 0) {
            // drop a water item
            Item water = new Item(this.position.getX(), this.position.getY(), new ItemStack(ItemType.WATER, 1));
            water.velocity = this.velocity;
            Main.getGamePanel().addNewEntity(water);
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        super.draw(ctx);
    }
}