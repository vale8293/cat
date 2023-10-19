package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rico extends Entity {

    private int animationTick = 0;
    private int width = 160;
    private int height = 160;
    public int maxHealth = 6;
    public int health = 6;

    public Rico(double startX, double startY) {
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
        if (this.position.distance(playerPos) < 400) {
            this.acceleration = new Vector2D(x, y);
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture = Main.getResources().getTexture("enemies/Rico/:" + "Placeholder");

        animationTick += 1;
        animationTick = animationTick % 24;
        int aniFrame = animationTick / 4;

        double largest = 0;
        String direction = null;

        // Check which direction is the largest
        if (Math.abs(this.velocity.getX()) > largest) {
            largest = Math.abs(this.velocity.getX());
            direction = this.velocity.getX() > 0 ? "right" : "left";
        }

        // If rico is moving enough, draw the sprite in the direction that movement is
        if (largest > 0.125) {
            switch (direction) {
                case "left": {
                    texture = Main.getResources().getTexture("enemies/Rico/" + aniFrame + ":0");
                    break;
                }
                case "right": {
                    texture = Main.getResources().getTexture("enemies/Rico/" + aniFrame + ":1");
                    break;
                }
                default:
                    texture = Main.getResources().getTexture("enemies/Rico/" + aniFrame + ":0");
            }
        } else {
            texture = Main.getResources().getTexture("enemies/Rico/" + aniFrame + ":2");
        }

        ctx.drawImage(texture, (int) this.position.getX() - width / 2, (int) this.position.getY() - height / 2, width, height, Main.getGamePanel());
    }

}