package org.acitech.cat.entities.projectiles;

import org.acitech.cat.entities.Projectile;
import org.acitech.cat.tilemap.Room;

public class Feather extends Projectile {

    public Feather(Room room, double startX, double startY, double rot, String owner) {
        super(room, startX, startY, rot, owner, "feather", "bullet");

        // Animation & Visuals
        this.aniLength = 4;
        this.aniFrameDuration = 6;
        this.width = 32;
        this.height = 32;

        // Stats
        this.damageElement = "none";
        this.onDeath = "none";
        this.moveSpeed = 12;
        this.damage = 1;
        this.maxCollisions = 1;
        this.lifetime = 120;
        this.kbMult = 5;
    }
}
