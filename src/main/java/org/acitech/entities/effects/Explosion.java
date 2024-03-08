package org.acitech.entities.effects;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.Enemy;
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
    public int damage = 3;

    public Explosion(double startX, double startY) {
        this.position = new Vector2d(startX, startY);
    }

    @Override
    protected void tick(double delta) {
        if (lifetime > 0) {
            this.lifetime--;
        } else {
            this.dispose();
        }

        // Looks for any instances of enemies
        for (Entity entity : GamePanel.entities) {
            if (!(entity instanceof Enemy enemy)) continue;

            if (this.position.distance(enemy.position) < 130) {
                enemy.dealDamage(this.damage);
            }
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        BufferedImage texture = Main.getResources().getTexture("effect/explosion/" + aniFrame + ":" + 0);
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }
}