package org.acitech.entities.enemies;

import org.acitech.Main;
import org.acitech.entities.Enemy;
import org.acitech.entities.items.Water;
import org.acitech.inventory.ItemType;

import java.awt.*;

public class Rico extends Enemy {

    public Rico(double startX, double startY) {
        super(startX, startY, "Rico");

        this.width = 160;
        this.height = 160;
        this.maxHealth = 3;
        this.health = maxHealth;
        this.aggroDistance = 400;
        this.itemPool.add(ItemType.WATER);
    }

    @Override
    protected void tick(double delta) {
        super.tick(delta);

        // If rico dies, get rid of the rico, todo: play an animation
        if (this.health <= 0) {
            // drop a water item
            Water water = new Water(this.position.getX(), this.position.getY());
            water.velocity = this.velocity;
            Main.getGamePanel().addNewEntity(water);
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        super.draw(ctx);
    }
}