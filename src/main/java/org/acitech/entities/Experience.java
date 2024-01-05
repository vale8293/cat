package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Experience extends Entity {

    private int width = 36;
    private int height = 36;
    protected boolean gettingPickedUp = false;

    public Experience(double startX, double startY) {
        this.position = new Vector2D(startX, startY);
        this.friction = 0.95;
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

            if (this.position.distance(playerPos) < 3) {
                gettingPickedUp = true;
            }
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture = Main.getResources().getTexture("items/string_material");
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }

    public boolean isGettingPickedUp() {
        return gettingPickedUp;
    }
}