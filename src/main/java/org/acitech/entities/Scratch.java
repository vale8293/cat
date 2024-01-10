package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Scratch extends Entity {

    public final double angle;
    private final Vector2D originPosition;
    private final int distance;
    private int width = 64;
    private int height = 64;
    private int animationTick = 0;

    public Scratch(int originX, int originY, int distance, double rot) {
        this.originPosition = new Vector2D(originX, originY);

        double y = this.originPosition.getY() + Math.sin(rot) * -distance;
        double x = this.originPosition.getX() + Math.cos(rot) * -distance;

        this.position = new Vector2D(x, y);
        this.distance = distance;
        this.angle = rot;
    }

    @Override
    protected void tick(double delta) {
        animationTick += 1;

        if (animationTick > 12) {
            this.dispose();
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture = Main.getResources().getTexture("effect/scratch/" + (animationTick / 3) + ":0");

        AffineTransform oldXForm = ctx.getTransform();

        ctx.translate(this.originPosition.getX() - (int) GamePanel.camera.getX(), this.originPosition.getY() - (int) GamePanel.camera.getY());
        ctx.rotate(this.angle - Math.PI / 2);
        ctx.drawImage(texture, -width / 2, -this.distance - height / 2, width, height, Main.getGamePanel());

        ctx.setTransform(oldXForm);
    }
}
