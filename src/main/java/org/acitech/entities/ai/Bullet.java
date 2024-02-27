package org.acitech.entities.ai;

import org.acitech.entities.Projectile;

public class Bullet extends ProjectileAI {
    public Bullet(Projectile projectile) {
        super(projectile);
    }

    // Defines basic AI for projectiles like Fireball
    @Override
    public void execute(double delta) {
        double angle = this.projectile.angle;
        this.projectile.velocity.set(Math.cos(angle + Math.PI), Math.sin(angle + Math.PI));
        this.projectile.velocity.multiply(this.projectile.moveSpeed);

    }
}
