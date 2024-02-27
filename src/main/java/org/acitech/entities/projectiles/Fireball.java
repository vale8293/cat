package org.acitech.entities.projectiles;

import org.acitech.entities.Projectile;

public class Fireball extends Projectile {

    public Fireball(double startX, double startY, double rot) {
        super(startX, startY, rot, "fireball", "bullet");

        // Animation & Visuals
        this.aniLength = 5;
        this.aniFrameDuration = 5;
        this.width = 160;
        this.height = 160;

        // Stats
        this.moveSpeed = 12;
    }
}
