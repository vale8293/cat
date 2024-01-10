package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Experience extends Entity {

    private int animationTick = 0;
    public int aniLength = 4;
    public int aniFrameDuration = 3;
    protected int width = 36;
    protected int height = 36;
    public double moveSpeed = 2;
    public int xpDrop;

    public Experience(double startX, double startY, int xpDrop) {
        this.position = new Vector2D(startX, startY);
        this.xpDrop = xpDrop;
        this.friction = 0.9;
    }

    @Override
    protected void tick(double delta) {
        Vector2D playerPos = GamePanel.player.position;
        System.out.println("I am an experience entity");
        System.out.println(GamePanel.player.xpCount);

        // Gets the angle between the player and the XP Orb
        double angle = Math.atan2(playerPos.getY() - this.position.getY(), playerPos.getX() - this.position.getX());
        double x = Math.cos(angle) * 0.5;
        double y = Math.sin(angle) * 0.5;
        this.acceleration = new Vector2D(x, y);
        this.acceleration = this.acceleration.scalarMultiply(moveSpeed);
        if (this.position.distance(playerPos) < ((double) this.width) ||
                this.position.distance(playerPos) < ((double) this.height)) {
            GamePanel.player.xpCount += this.xpDrop;
            this.dispose();
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture;

        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        int xpSize = 0;

        texture = Main.getResources().getTexture("items/experience/" + aniFrame + ":" + xpSize);
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }
}