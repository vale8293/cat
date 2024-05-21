package org.acitech.cat.entities.ai;

import org.acitech.cat.GamePanel;
import org.acitech.cat.assets.AssetLoader;
import org.acitech.cat.entities.Enemy;
import org.acitech.cat.entities.Entity;
import org.acitech.cat.entities.Projectile;
import org.acitech.cat.entities.effects.Explosion;

import javax.sound.sampled.Clip;

public class Bullet implements AI {

    private final Projectile projectile;

    public Bullet(Projectile projectile) {
        this.projectile = projectile;
    }

    // Defines basic AI for projectiles like Fireball
    @Override
    public void execute(double delta) {
        if (this.projectile.lifetime > 0) {
            this.projectile.lifetime--;
        } else {
            this.deathTypeCheck();
        }

        double angle = this.projectile.angle;
        this.projectile.velocity.set(Math.cos(angle + Math.PI), Math.sin(angle + Math.PI));
        this.projectile.velocity.multiply(this.projectile.moveSpeed);

        switch (this.projectile.getOwner()) {
            case "player" -> {
                if (collisionCheckEnemy(65) >= this.projectile.maxCollisions) {
                    // Self destruct
                    this.deathTypeCheck();
                }
            }
            case "enemy" -> {
                if (collisionCheckPlayer(50) >= this.projectile.maxCollisions) {
                    // Self destruct
                    this.deathTypeCheck();
                }
            }
        }
    }

    @Override
    public void damageHandler(Entity damager) {}

    private int collisionCheckEnemy(double radius) {
        int collisions = 0;

        for (Entity entity : this.projectile.getRoom().getEntities()) {
            if (!(entity instanceof Enemy enemy)) continue;

            if (this.projectile.position.distance(enemy.position) < radius) {
                enemy.dealDamage(this.projectile.damage, this.projectile);
                enemy.dealKnockback(this.projectile.damage, this.projectile.position, true);
                collisions += 1;
            }
        }

        return collisions;
    }

    private int collisionCheckPlayer(double radius) {
        int collisions = 0;

        if (this.projectile.position.distance(GamePanel.getPlayer().position) < radius) {
            GamePanel.getPlayer().damageTaken(this.projectile.damage, this.projectile.damageElement);
            collisions += 1;
        }

        return collisions;
    }

    private void deathTypeCheck() {
        if (this.projectile.onDeath.equalsIgnoreCase("explosion")) {
            switch (this.projectile.damageElement) {
                case "fire" -> this.projectile.onDeathDamage = this.projectile.damage / 2;
                case "aqua" -> this.projectile.onDeathDamage = this.projectile.damage / 3;
            }

            new Explosion(this.projectile.getRoom(), this.projectile.position.getX(), this.projectile.position.getY(), this.projectile.damageElement, this.projectile.onDeathDamage);
            Clip exploSfx = AssetLoader.EXPLOSION.createClip(); // like from splatoon
            exploSfx.start();
        }
        this.projectile.dispose();
    }
}