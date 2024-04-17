package org.acitech.entities.ai;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.Enemy;
import org.acitech.entities.Entity;
import org.acitech.entities.Projectile;
import org.acitech.entities.effects.Explosion;

import javax.sound.sampled.Clip;

public class Bullet implements AI {
    private final Projectile projectile;
    private static final Clip exploSfx = Main.getResources().getSound("explosion"); // like from splatoon

    public Bullet(Projectile projectile) {
        this.projectile = projectile;
    }

    // Defines basic AI for projectiles like Fireball
    @Override
    public void execute(double delta) {
        double angle = this.projectile.angle;
        this.projectile.velocity.set(Math.cos(angle + Math.PI), Math.sin(angle + Math.PI));
        this.projectile.velocity.multiply(this.projectile.moveSpeed);

        if (collisionCheck(65) >= this.projectile.maxCollisions) {
            // Self destruct
            if (this.projectile.onDeath.equalsIgnoreCase("explosion")) {
                switch (this.projectile.damageElement) {
                    case ("fire") -> this.projectile.onDeathDamage = this.projectile.damage / 2;
                    case ("aqua") -> this.projectile.onDeathDamage = this.projectile.damage / 3;
                }

                Main.getGamePanel().addNewEntity(new Explosion(this.projectile.position.getX(), this.projectile.position.getY(), this.projectile.damageElement, this.projectile.onDeathDamage));
                exploSfx.start();
            }

            this.projectile.dispose();
        }
    }

    @Override
    public void damageHandler(Entity damager) {}

    private int collisionCheck(double radius) {
        int collisions = 0;

        for (Entity entity : GamePanel.entities) {
            if (!(entity instanceof Enemy enemy)) continue;

            if (this.projectile.position.distance(enemy.position) < radius) {
                collisions += 1;
            }
        }

        return collisions;
    }
}