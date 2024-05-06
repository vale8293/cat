package org.acitech.entities.projectiles;

import org.acitech.entities.Projectile;
import org.acitech.tilemap.Room;

public class Aquaball extends Projectile {

    public Aquaball(Room room, double startX, double startY, double rot) {
        super(room, startX, startY, rot, "aquaball", "bullet");

        // Animation & Visuals
        this.aniLength = 5;
        this.aniFrameDuration = 5;
        this.width = 160;
        this.height = 160;

        // Stats
        this.damageElement = "aqua";
        this.onDeath = "explosion";
        this.moveSpeed = 8;
        this.damage = 3;
        this.maxCollisions = 2;
        this.kbMult = 2;
    }
}
