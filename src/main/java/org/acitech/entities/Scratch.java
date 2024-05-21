package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.assets.AssetLoader;
import org.acitech.tilemap.Room;
import org.acitech.utils.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Scratch extends Entity {

    public final double angle;
    private final Vector2d originPosition;
    private final int distance;
    private final int width = 64;
    private final int height = 64;
    private int animationTick = 0;
    private String element = "base";

    public Scratch(Room room, int originX, int originY, int distance, double rot, String element) {
        super(room);

        this.originPosition = new Vector2d(originX, originY);

        double y = this.originPosition.getY() + Math.sin(rot) * -distance;
        double x = this.originPosition.getX() + Math.cos(rot) * -distance;

        this.position = new Vector2d(x, y);
        this.distance = distance;
        this.angle = rot;

        this.element = element;

        dealAoeDamage();
    }

    private void dealAoeDamage() {
        for (Entity entity : this.getRoom().getEntities()) {
            if (!(entity instanceof Enemy enemy)) continue;

            // Gets the position of the scratch
            double dist = enemy.position.distance(this.position);

            // If the scratch makes contact with the enemy
            // regain 1 mana
            // knock it back, lose 1hp, and start i-frames, extend streak
            if (dist < 100) {
                // TODO: lets not always assume that the player is the one dealing the scratch
                boolean dealtDamage = enemy.dealDamage(GamePanel.getPlayer().scratchDamage - enemy.defense, this);

                if (dealtDamage) {
                    enemy.dealKnockback(enemy.kbMult * 0.5, GamePanel.getPlayer().position, true);
                    GamePanel.getPlayer().mana = Math.min(GamePanel.getPlayer().mana + 1, GamePanel.getPlayer().maxMana);
                    GamePanel.getPlayer().streakTimer = GamePanel.getPlayer().streakTimerMax;
                }
            }
        }
    }

    @Override
    // Do this stuff every frame
    protected void tick(double delta) {
        animationTick += 1;

        if (animationTick > 12) {
            this.dispose();
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture = AssetLoader.EFFECT_SCRATCH.getSprite(animationTick / 3, 0);

        AffineTransform oldXForm = ctx.getTransform();

        ctx.translate(this.originPosition.getX() - (int) GamePanel.getCamera().getX(), this.originPosition.getY() - (int) GamePanel.getCamera().getY());
        ctx.rotate(this.angle - Math.PI / 2);
        ctx.drawImage(texture, -width / 2, -this.distance - height / 2, width, height, Main.getGamePanel());

        ctx.setTransform(oldXForm);
    }
}
