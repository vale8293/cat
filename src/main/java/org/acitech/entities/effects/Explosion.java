package org.acitech.entities.effects;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.Entity;
import org.acitech.utils.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion extends Entity {
    // Animation & Visuals
    private int animationTick = -1;
    public int aniLength = 4;
    public int aniFrameDuration = 5;
    public int width = 160;
    public int height = 160;

    // Stats
    public int lifetime = 20;

    public Explosion(double startX, double startY) {
        this.position = new Vector2d(startX, startY);
    }

    @Override
    protected void tick(double delta) {
        if (lifetime > 0) {
            lifetime--;
        } else {
            this.dispose();
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        System.out.println(aniFrame);
        BufferedImage texture = Main.getResources().getTexture("effect/explosion/" + aniFrame + ":" + 0);
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }
}
