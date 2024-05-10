package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.ai.AI;
import org.acitech.entities.ai.Bullet;
import org.acitech.tilemap.Room;
import org.acitech.utils.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Projectile extends Entity {

    // Identifiers
    private final String projectileName;
    public final double angle;
    public final Vector2d originPosition;
    private AI projectileAI;

    // Animation & Visuals
    private int animationTick = 0;
    public int aniLength = 1;
    public int aniFrameDuration = 1;
    public int width = 160;
    public int height = 160;

    // Stats
    public String onDeath = "none"; // Specifies what happens after the projectile runs out of collisions (ex: explode)
    public int onDeathDamage = 0;
    public int maxCollisions = 1;
    public int damage = 1;
    public String damageElement = "None";
    public double moveSpeed = 1;
    public int kbMult = 10;

    public Projectile(Room room, double startX, double startY, double rot, String projectileName, String ai) {
        super(room);

        this.position = new Vector2d(startX, startY);
        this.originPosition = position.copy();
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
    protected void tick(double delta) {
        if (this.projectileAI != null) {
            this.projectileAI.execute(delta);
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture;

        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        texture = Main.getResources().getTexture("projectiles/" + projectileName + "/" + aniFrame + ":" + 0);

        AffineTransform oldXForm = ctx.getTransform();

        ctx.translate(this.originPosition.getX() - (int) GamePanel.getCamera().getX(), this.originPosition.getY() - (int) GamePanel.getCamera().getY());
        ctx.rotate(this.angle - Math.PI / 2);
        ctx.drawImage(texture, -width / 2, (int) -this.originPosition.distance(this.position) - height / 2, width, height, Main.getGamePanel());

        ctx.setTransform(oldXForm);
    }
}