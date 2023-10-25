package org.acitech.entities;

import org.acitech.GamePanel;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;

public class Item extends Entity {

    private int width = 32;
    private int height = 32;

    public Item(double startX, double startY) {
        this.position = new Vector2D(startX, startY);
        this.friction = 0.98;
    }

    @Override
    protected void tick(double delta) {
        Vector2D playerPos = GamePanel.player.position;

        // Gets the angle between the player and the item
        double angle = Math.atan2(playerPos.getY() - this.position.getY(), playerPos.getX() - this.position.getX());
        double x = Math.cos(angle) * 0.5;
        double y = Math.sin(angle) * 0.5;
        // Sucks up the item if it's close enough to the player
        if (this.position.distance(playerPos) < 100) {
            this.acceleration = new Vector2D(x, y);
            this.height = (int) (this.height * 0.85);
            this.width = (int) (this.width * 0.85);
            if (this.height < 2) {
                this.dispose();

            }
        }

//        if (this.isDisposed()) {
//            // Add to the player's inventory in the first empty slot
//        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        ctx.setColor(Color.green);
        ctx.fillRect((int) this.position.getX() - width / 2, (int) this.position.getY() - height / 2, width, height);
    }
}