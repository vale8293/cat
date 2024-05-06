package org.acitech.entities.projectiles;

import org.acitech.entities.Projectile;
import org.acitech.tilemap.Room;

public class Fireball extends Projectile {

    public Fireball(Room room, double startX, double startY, double rot) {
        super(room, startX, startY, rot, "fireball", "bullet");

        // Animation & Visuals
        this.aniLength = 5;
        this.aniFrameDuration = 5;
        this.width = 160;
        this.height = 160;

        // Stats
        this.damageElement = "fire";
        this.onDeath = "explosion";
        this.onDeathDamage = 3;
        this.moveSpeed = 12;
        this.damage = 5;
        this.maxCollisions = 1;
        this.kbMult = 3;
    }
}
