package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.ai.Bullet;
import org.acitech.entities.ai.ProjectileAI;
import org.acitech.entities.effects.Explosion;
import org.acitech.utils.Vector2d;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends Entity {

    // Identifiers
    private final String projectileName;
    public final double angle;
    private ProjectileAI projectileAI;

    // Animation & Visuals
    private int animationTick = 0;
    public int aniLength = 1;
    public int aniFrameDuration = 1;
    public int width = 160;
    public int height = 160;

    // Stats
    public String onDeath = "none"; // Specifies what happens after the projectile runs out of collisions (ex: explode)
    public int maxCollisions = 1;
    public int collisions = maxCollisions;
    public int manaCost = 1;
    public int damage = 1;
    public String damageElement = "None";
    public double moveSpeed = 1;
    public int kbMult = 10;

    // Load important stuff
    Clip sndExplo = Main.getResources().getSound("explosion"); // like from splatoon

    public Projectile(double startX, double startY, double rot, String projectileName, String ai) {
        this.position = new Vector2d(startX, startY);
        this.angle = rot;
        this.friction = 0.9;
        this.projectileName = projectileName;

        switch (ai.toLowerCase()) {
            case "bullet" -> this.projectileAI = new Bullet(this);
            case "mydog" -> System.out.print(":mydog:");
            default -> this.projectileAI = null;
        }
    }

    @Override
    // Do this stuff every frame
    protected void tick(double delta) {

        if (this.projectileAI != null) {
            this.projectileAI.execute(delta);
        }

        deathCheck();
    }

    // Defines basic AI for when a projectile expires
    public void deathCheck() {
        // If the projectile expires, get rid of it
        if (this.collisions <= 0) {
            switch (this.onDeath) {
                case ("explosion") -> {
                    Main.getGamePanel().addNewEntity(new Explosion(this.position.getX(), this.position.getY()));
                    sndExplo.start();
                }
                case ("waterExplosion") -> System.out.println("Placeholder");
            }

            this.dispose();
        }
    }

    @Override
    // Handles graphics
    public void draw(Graphics2D ctx) {
        BufferedImage texture = Main.getResources().getTexture("cow");

        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        double largest = 0;
        String direction = "right";

        // Check which direction has the largest speed
        if (Math.abs(this.velocity.getX()) > largest) {
            largest = Math.abs(this.velocity.getX());
            direction = this.velocity.getX() > 0 ? "right" : "left";
        }
        if (Math.abs(this.velocity.getY()) > largest) {
            largest = Math.abs(this.velocity.getY());
            direction = this.velocity.getY() > 0 ? "down" : "up";
        }

        // Draw the projectile's sprite in the direction that movement is
        if (largest > 0) {
            switch (direction) {
                case "left" -> texture = Main.getResources().getTexture("projectiles/" + projectileName + "/" + aniFrame + ":" + 0);
                case "right" -> texture = Main.getResources().getTexture("projectiles/" + projectileName + "/" + aniFrame + ":" + 1);
                case "up" -> texture = Main.getResources().getTexture("projectiles/" + projectileName + "/" + aniFrame + ":" + 2);
                case "down" -> texture = Main.getResources().getTexture("projectiles/" + projectileName + "/" + aniFrame + ":" + 3);
            }
        }

        // Idle animation (should never happen but placeholder)
        else {
            if (direction.equals("left")) {
                texture = Main.getResources().getTexture("projectiles/" + projectileName + "/" + aniFrame + ":" + 4);
            } else {
                texture = Main.getResources().getTexture("projectiles/" + projectileName + "/" + aniFrame + ":" + 5);
            }
        }

        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }
}