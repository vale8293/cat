package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.tilemap.Room;
import org.acitech.utils.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Experience extends Entity {

    private int animationTick = 0;
    public int aniLength = 6;
    public int aniFrameDuration = 6;
    protected int width = 48;
    protected int height = 48;
    public double moveSpeed = 2;
    public int xpValue;

    public Experience(Room room, double startX, double startY, int xpValue) {
        super(room);

        this.position = new Vector2d(startX, startY);
        this.xpValue = xpValue;
        this.friction = 0.9;
    }

    @Override
    protected void tick(double delta) {
        Vector2d playerPos = GamePanel.getPlayer().position;

        // Gets the angle between the player and the XP Orb
        this.acceleration.add(playerPos.directionTo(this.position).multiply(moveSpeed * 0.5));

        if (this.position.distance(playerPos) < Math.max(this.width / 2, this.height / 2)) {
            GamePanel.getPlayer().xpCount += 1; // Increases xp by 1
            GamePanel.getPlayer().levelUpCheck(); // Checks if this pushes the player past a lv up threshold
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

        texture = Main.getResources().getTexture("items/experience/" + aniFrame + ":0");
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.getCamera().getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.getCamera().getY(), width, height, Main.getGamePanel());
    }
}