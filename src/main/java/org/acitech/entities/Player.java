package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.KeyHandler;
import org.acitech.Main;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Player extends Entity {

    private int animationTick = 0;

    public Player() {
        this.friction = 0.9;
    }

    @Override
    protected void tick(double delta) {
        if (KeyHandler.wDown) {
            this.acceleration = this.acceleration.add(new Vector2D(0, -.5));
        }
        if (KeyHandler.aDown) {
            this.acceleration = this.acceleration.add(new Vector2D(-.5, 0));
        }
        if (KeyHandler.sDown) {
            this.acceleration = this.acceleration.add(new Vector2D(0, .5));
        }
        if (KeyHandler.dDown) {
            this.acceleration = this.acceleration.add(new Vector2D(.5, 0));
        }
        if (KeyHandler.mouseClicks.size() > 0) {
            Clip clip = Main.getResources().getSound("player_scratch");
            clip.setFramePosition(0);
            clip.loop(0);
            clip.start();
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture = Main.getResources().getTexture("cow");

        int e = 4;
        animationTick += 1;
        animationTick = animationTick % (6 * e);
        int aniFrame = animationTick / e;

        double largest = 0;
        String direction = null;

        if (Math.abs(this.velocity.getX()) > largest) {
            largest = Math.abs(this.velocity.getX());
            direction = this.velocity.getX() > 0 ? "right" : "left";
        }
        if (Math.abs(this.velocity.getY()) > largest) {
            largest = Math.abs(this.velocity.getY());
            direction = this.velocity.getY() > 0 ? "down" : "up";
        }

        if (largest > 1.125) {
            switch (direction) {
                case "up": {
                    texture = Main.getResources().getTexture("player/running/" + aniFrame + ":3");
                    break;
                }
                case "down": {
                    texture = Main.getResources().getTexture("player/running/" + aniFrame + ":2");
                    break;
                }
                case "left": {
                    texture = Main.getResources().getTexture("player/running/" + aniFrame + ":0");
                    break;
                }
                case "right": {
                    texture = Main.getResources().getTexture("player/running/" + aniFrame + ":1");
                    break;
                }
            }
        }

        ctx.drawImage(texture, (int) Math.round(this.position.getX()), (int) Math.round(this.position.getY()), 64 * 3, 64 * 3, Main.getGamePanel());
    }
}
