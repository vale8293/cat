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
    private String explosionType;
    private int animationTick = -1;
    public int aniLength = 4;
    public int aniFrameDuration = 3;
    public int kbMult = 0;
    public int width = 160;
    public int height = 160;

    // Stats
    public boolean hasDealtAOE = false;
    public int lifetime;
    public int onDeathDamage;

    public Explosion(double startX, double startY, String explosionType, int onDeathDamage) {
        this.position = new Vector2d(startX, startY);
        this.explosionType = explosionType;
        this.onDeathDamage = onDeathDamage;

        switch (explosionType) {
            case ("fire") -> {
                this.aniLength = 4;
                this.aniFrameDuration = 4;
                this.kbMult = 15;
            }
            case ("aqua"), ("wind") -> {
                this.aniLength = 5;
                this.aniFrameDuration = 3;
                this.kbMult = 10;
            }
            default -> {
                this.aniLength = 1;
                this.aniFrameDuration = 1;
                this.kbMult = 50;
            }
        }

        this.lifetime = this.aniLength * this.aniFrameDuration;
    }

    @Override
    protected void tick(double delta) {
        if (this.lifetime > 0) {
            this.lifetime--;
        } else {
            this.dispose();
        }

        if (!this.hasDealtAOE) {
            // Looks for any instances of enemies
            for (Entity entity : GamePanel.entities) {
                if (!(entity instanceof Enemy enemy)) continue;

                if (this.position.distance(enemy.position) < 130) {
                    enemy.dealDamage(this.onDeathDamage, this);
                    enemy.dealKnockback(this.kbMult * 0.5, this.position, true);
                }
            }

            this.hasDealtAOE = true;
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        BufferedImage texture = Main.getResources().getTexture("effect/explosion_" + explosionType + "/" + aniFrame + ":" + 0);
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }
}