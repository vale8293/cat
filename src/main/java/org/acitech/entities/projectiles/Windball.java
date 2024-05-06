package org.acitech.entities.projectiles;

import org.acitech.entities.Projectile;
import org.acitech.tilemap.Room;

public class Windball extends Projectile {

    public Windball(Room room, double startX, double startY, double rot) {
        super(room, startX, startY, rot, "windball", "bullet");

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
        this.damage = 2;
        this.maxCollisions = 1;
        this.kbMult = 35;
    }
}
