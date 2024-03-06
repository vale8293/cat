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
        this.onDeath = "explosion";
        this.moveSpeed = 12;
        this.manaCost = 3;
        this.damage = 2;
        this.maxCollisions = 1;
        this.kbMult = 3;
    }
}
