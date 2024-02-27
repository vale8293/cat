package org.acitech.entities.ai;

import org.acitech.entities.Projectile;

abstract public class ProjectileAI {
    final Projectile projectile;

    public ProjectileAI(Projectile projectile) {
        this.projectile = projectile;
    }
    public abstract void execute(double delta);
}
