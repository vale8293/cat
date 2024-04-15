package org.acitech.entities.projectiles;

import org.acitech.entities.Projectile;

public class Windball extends Projectile {

    public Windball(double startX, double startY, double rot) {
        super(startX, startY, rot, "windball", "bullet");

        // Animation & Visuals
        this.aniLength = 5;
        this.aniFrameDuration = 5;
        this.width = 160;
        this.height = 160;

        // Stats
        this.damageElement = "wind";
        this.onDeath = "explosion";
        this.onDeathDamage = 1;
        this.moveSpeed = 18;
        this.manaCost = 2;
        this.damage = 2;
        this.maxCollisions = 1;
        this.kbMult = 35;
    }
}
