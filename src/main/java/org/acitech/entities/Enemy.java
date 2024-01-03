package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.inventory.ItemStack;
import org.acitech.inventory.ItemType;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends Entity {

    private final String enemyName;
    public ArrayList<ItemType> itemPool = new ArrayList<>();
    private int animationTick = 0;
    public int aniLength = 4;
    protected int width = 160;
    protected int height = 160;
    public int maxHealth = 1;
    public int health = maxHealth;
    public int maxMana = 0;
    public int mana = maxMana;
    public int damage = 1;
    public int defense = 0;
    public double moveSpeed = 1;
    public int aggroDistance = 300;
    public int immunity = 20;
    public int damageTimer;

    public Enemy(double startX, double startY, String enemyName) {
        this.position = new Vector2D(startX, startY);
        this.friction = 0.9;
        this.enemyName = enemyName;
    }

    @Override
    protected void tick(double delta) {
        Vector2D playerPos = GamePanel.player.position;
        if (this.damageTimer > 0) this.damageTimer--; // Reduce damage timer

        // Gets the angle between the player and the enemy
        double angle = Math.atan2(playerPos.getY() - this.position.getY(), playerPos.getX() - this.position.getX());
        double x = Math.cos(angle) * 0.5;
        double y = Math.sin(angle) * 0.5;
        if (this.position.distance(playerPos) < aggroDistance) {
            this.acceleration = new Vector2D(x, y);
            this.acceleration = this.acceleration.scalarMultiply(moveSpeed);
            if (this.position.distance(playerPos) < (int) (this.width/2) ||
                    this.position.distance(playerPos) < (int) (this.height/2)) {
                if (GamePanel.player.damageTimer == 0) {
                    GamePanel.player.health -= Math.max(this.damage - GamePanel.player.meleeDefense, 0);
                    GamePanel.player.damageTimer = immunity;
                }
                this.velocity = new Vector2D(-20 * x, -20 * y);
                GamePanel.player.velocity = this.velocity.scalarMultiply(-1);
            }
        }

        // Looks for any instances of a scratch
        for (Entity entity : GamePanel.entities) {
            if (!(entity instanceof Scratch scratch)) continue;

            // Gets the position of the scratch
            double dist = scratch.position.distance(this.position);

            // If the scratch makes contact with the enemy
            // regain 1 mana
            // knock it back, lose 1hp, and start i-frames
            if (dist < 100) {
                if (this.immunity == 0) {
                    if (GamePanel.player.mana < GamePanel.player.maxMana) {
                        GamePanel.player.mana += 1;
                    }
                    this.velocity = new Vector2D(-20 * x, -20 * y);
                    this.health -= Math.max(GamePanel.player.scratchDamage - this.defense, 0);
                    this.immunity = 20;
                    this.damageTimer = 20;
                }

                if (this.immunity > 0) {
                    this.immunity -= 1;
                }
            }
        }

        // If rico dies, get rid of the enemy, todo: play an animation
        if (this.health <= 0) {
            this.dispose();

            // cause there do be stuff in the item pool
            if (itemPool.size() > 0) {
                int rngIndex = new Random().nextInt(itemPool.size());
                ItemType droppedItemType = itemPool.get(rngIndex);

                // spawn entity based.
                Item item = new Item(this.position.getX(), this.position.getY(), new ItemStack(ItemType.WATER, 1));

                item.velocity = this.velocity;
                Main.getGamePanel().addNewEntity(item);
            }
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture;

        animationTick += 1;

        animationTick = animationTick % (aniLength * aniLength);
        int aniFrame = animationTick / (aniLength);

        double largest = 0;
        String directionX = "left";
        String directionY = "up";

        // Check which direction is the largest
        if (Math.abs(this.velocity.getX()) > largest) {
            largest = Math.abs(this.velocity.getX());
            directionX = this.velocity.getX() > 0 ? "right" : "left";
        }

        // Check which direction is the largest
        if (Math.abs(this.velocity.getY()) > largest) {
            largest = Math.abs(this.velocity.getY());
            directionY = this.velocity.getY() > 0 ? "up" : "down";
        }

        // If the enemy is moving enough, draw the sprite in the direction that movement is
        if (largest > 0.5) {

            // Checks vertical movement
            if (directionY.equals("down")) {
                texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":2");
            }
            else {
                texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":3");
            }

            // Checks horizontal movement
            if (directionX.equals("left")) {
                texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":0");
            }
            else {
                texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":1");
            }
        }

        // Because speed is too low, must be idle, checks direction
        else {
            if (directionX.equals("left")) {
                texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":4");
            }
            else {
                texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":5");
            }
        }

        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());

        if (this.damageTimer > 0) {
            BufferedImage wow = tint(texture, 1, 0, 0, (float) this.damageTimer / 20 * 0.6f);
            ctx.drawImage(wow, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
        }
    }

    public static BufferedImage tint(BufferedImage sprite, float red, float green, float blue, float alpha) {
        BufferedImage maskImg = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TRANSLUCENT);
        int rgb = new Color(red, green, blue, alpha).getRGB();

        for (int i = 0; i < sprite.getWidth(); i++) {
            for (int j = 0; j < sprite.getHeight(); j++) {
                int color = sprite.getRGB(i, j);

                if (color != 0) {
                    maskImg.setRGB(i, j, rgb);
                }
            }
        }

        return maskImg;
    }
}