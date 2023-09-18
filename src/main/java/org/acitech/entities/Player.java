package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.KeyHandler;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;

public class Player extends Entity {

    @Override
    protected void tick(double delta) {
        if (KeyHandler.wDown) {
            this.position = this.position.add(new Vector2D(0, -5));
        }
        if (KeyHandler.aDown) {
            this.position = this.position.add(new Vector2D(-5, 0));
        }
        if (KeyHandler.sDown) {
            this.position = this.position.add(new Vector2D(0, 5));
        }
        if (KeyHandler.dDown) {
            this.position = this.position.add(new Vector2D(5, 0));
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        ctx.setColor(Color.white);
        ctx.fillRect((int) Math.round(this.position.getX()), (int) Math.round(this.position.getY()), 32 * 2, 32 * 2);
    }
}
