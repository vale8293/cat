package org.acitech.cat.entities.projectiles;

import org.acitech.cat.entities.Projectile;
import org.acitech.cat.tilemap.Room;

public class Windball extends Projectile {

    public Windball(Room room, double startX, double startY, double rot, String owner) {
        super(room, startX, startY, rot, owner, "windball", "bullet");

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
        this.lifetime = 120;
        this.kbMult = 350;
    }
}
