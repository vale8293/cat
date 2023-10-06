package org.acitech.entities;

import org.acitech.GamePanel;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;

public class Enemy extends Entity {

    private int width = 32;
    private int height = 32;

    public Enemy(double startX, double startY) {
        this.position = new Vector2D(startX, startY);
        this.friction = 0.9;
    }

    @Override
    protected void tick(double delta) {
        Vector2D playerPos = GamePanel.player.position;

        // Gets the angle between the player and the enemy
        double angle = Math.atan2(playerPos.getY() - this.position.getY(), playerPos.getX() - this.position.getX());
        double x = Math.cos(angle) * 0.5;
        double y = Math.sin(angle) * 0.5;
        if (this.position.distance(playerPos) < 112) {
            x *= -1;
            y *= -1;
        }
        this.acceleration = new Vector2D(x, y);
    }

    @Override
    public void draw(Graphics2D ctx) {
        ctx.setColor(Color.red);
        ctx.fillRect((int) this.position.getX() - width / 2, (int) this.position.getY() - height / 2, width, height);
    }
}
