package org.acitech.entities.projectiles;

import org.acitech.entities.Projectile;
import org.acitech.tilemap.Room;

public class Feather extends Projectile {

    public Feather(Room room, double startX, double startY, double rot) {
        super(room, startX, startY, rot, "feather", "bullet");

        // Animation & Visuals
        this.aniLength = 4;
        this.aniFrameDuration = 6;
        this.width = 32;
        this.height = 32;

        // Stats
        this.damageElement = "none";
        this.onDeath = "none";
        this.moveSpeed = 12;
        this.damage = 2;
        this.maxCollisions = 1;
        this.kbMult = 5;
    }
}
