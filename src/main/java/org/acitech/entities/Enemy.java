package org.acitech.entities;

import org.acitech.GamePanel;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;

public class Enemy extends Entity {

    public Enemy(double startX, double startY) {
        this.position = new Vector2D(startX, startY);
        this.friction = 0.9;
    }

    @Override
    protected void tick(double delta) {
        double angle = Math.atan2(GamePanel.player.position.getY() - this.position.getY(), GamePanel.player.position.getX() - this.position.getX());
        double x = Math.cos(angle) * 0.5;
        double y = Math.sin(angle) * 0.5;
        if (this.position.distance(GamePanel.player.position) < 100) {
            x *= -1;
            y *= -1;
        }
        this.acceleration = new Vector2D(x, y);
    }

    @Override
    public void draw(Graphics2D ctx) {
        ctx.setColor(Color.red);
        ctx.fillRect((int) Math.round(this.position.getX()), (int) Math.round(this.position.getY()), 32, 32);
    }
}
