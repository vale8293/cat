package org.acitech.entities.projectiles;

import org.acitech.entities.Projectile;

public class Aquaball extends Projectile {

    public Aquaball(double startX, double startY, double rot) {
        super(startX, startY, rot, "waterball", "bullet");

        // Animation & Visuals
        this.aniLength = 5;
        this.aniFrameDuration = 5;
        this.width = 160;
        this.height = 160;

        // Stats
        this.damageElement = "aqua";
        this.onDeath = "explosion";
        this.moveSpeed = 8;
        this.manaCost = 2;
        this.damage = 3;
        this.maxCollisions = 2;
        this.kbMult = 2;
    }
}
