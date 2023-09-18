package org.acitech.entities;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;

public class Enemy extends Entity {

    public Enemy(double startX, double startY) {
        this.position = new Vector2D(startX, startY);
    }

    @Override
    protected void tick(double delta) {

    }

    @Override
    public void draw(Graphics2D ctx) {
        ctx.setColor(Color.red);
        ctx.fillRect((int) Math.round(this.position.getX()), (int) Math.round(this.position.getY()), 32, 32);
    }
}
