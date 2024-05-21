package org.acitech.cat.entities;

import org.acitech.cat.GamePanel;
import org.acitech.cat.Main;
import org.acitech.cat.assets.AssetLoader;
import org.acitech.cat.entities.ai.AI;
import org.acitech.cat.entities.ai.Bullet;
import org.acitech.cat.tilemap.Room;
import org.acitech.cat.utils.Vector2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Projectile extends Entity {

    // Identifiers
    public final String projectileName;
    protected String owner;
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
    public int lifetime;
    public int damage = 1;
    public String damageElement = "None";
    public double moveSpeed = 1;
    public int kbMult = 10;

    public Projectile(Room room, double startX, double startY, double rot, String owner, String projectileName, String ai) {
        super(room);

        this.position = new Vector2d(startX, startY);
        this.originPosition = position.copy();
        this.angle = rot;
        this.friction = 0.9;
        this.owner = owner;
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
        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        BufferedImage texture = AssetLoader.getProjectileByName(projectileName).getSprite(aniFrame, 0);

        AffineTransform oldXForm = ctx.getTransform();

        ctx.translate(this.originPosition.getX() - (int) GamePanel.getCamera().getX(), this.originPosition.getY() - (int) GamePanel.getCamera().getY());
        ctx.rotate(this.angle - Math.PI / 2);
        ctx.drawImage(texture, -width / 2, (int) -this.originPosition.distance(this.position) - height / 2, width, height, Main.getGamePanel());

        ctx.setTransform(oldXForm);
    }

    public String getOwner() {
        return owner;
    }
}