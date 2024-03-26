package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.ai.EnemyAI;
import org.acitech.entities.ai.Fighter;
import org.acitech.entities.ai.Skitter;
import org.acitech.inventory.ItemStack;
import org.acitech.inventory.ItemType;
import org.acitech.utils.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

abstract public class Enemy extends Entity {

    // Identifiers
    private final String enemyName;
    private final EnemyAI enemyAI;
    public ArrayList<ItemType> itemPool = new ArrayList<>();

    // Animation & Visuals
    private int animationTick = 0;
    public int aniLength = 1;
    public int aniFrameDuration = 1;
    public int width = 160;
    public int height = 160;

    // Stats & Combat
    protected int health, maxHealth = 1;
    protected int mana, maxMana = 0; // To be used in the future maybe
    public int damage = 1;
    public String damageElement = "None";
    public int defense = 0;
    public double moveSpeed = 1;
    public int kbMult = 20;
    public int aggroDistance = 300;
    public int immunity = 20;
    // public int heartChance = 15; // 15% chane to drop a heart that restores 1hp
    public int damageTimer;

    // Rewards
    public int xpDrop = 1;
    public int xpValue = 1;
    public int xpScatter = 10;
    public int itemDrop = 1;
    public int itemScatter = 5;

    public Enemy(double startX, double startY, String enemyName, String ai) {
        this.position = new Vector2d(startX, startY);
        this.friction = 0.9;
        this.enemyName = enemyName;

        switch (ai.toLowerCase()) {
            case "fighter" -> this.enemyAI = new Fighter(this);
            case "skitter" -> this.enemyAI = new Skitter(this);
            default -> this.enemyAI = null;
        }
    }

    @Override
    // Do this stuff every frame
    protected void tick(double delta) {
        if (this.damageTimer > 0) this.damageTimer--; // Reduce damage timer

        if (this.enemyAI != null) {
            this.enemyAI.execute(delta);
        }

        deathCheck();
    }

    // Defines basic AI for if an enemy dies
    protected void deathCheck() {
        // If the enemy dies, get rid of it, todo: play an animation (probably some kinda particle explosion)
        if (this.health <= 0) {
            this.dispose();

            // Drops XP based on the streak
            if (this.xpDrop > 0) {
                for (int i = 0; i < Math.ceil((xpDrop - 1) * (2 * (0.5 + GamePanel.player.currentStreak / 10.0))); i++) {

                    // Gets some random X & Y velocities to add to the drop velocity to scatter XP
                    double rngX = new Random().nextDouble(-xpScatter, xpScatter);
                    double rngY = new Random().nextDouble(-xpScatter, xpScatter);

                    // Drops the XP with the random velocities added
                    Experience experience = new Experience(this.position.getX(), this.position.getY(), this.xpValue);
                    experience.velocity.set(rngX, rngY);
                    Main.getGamePanel().addNewEntity(experience);
                }
            }

            // Increments the streak
            GamePanel.player.currentStreak += 1;

            // cause there do be stuff in the item pool
            if (!itemPool.isEmpty()) {
                for (int i = 0; i < itemDrop; i++) {

                    // Get a random number to get an item from the pool + X & Y velocities to add
                    double rngX = new Random().nextDouble(-itemScatter, itemScatter);
                    double rngY = new Random().nextDouble(-itemScatter, itemScatter);
                    int rngIndex = new Random().nextInt(itemPool.size());
                    ItemType droppedItemType = itemPool.get(rngIndex);

                    // Spawn the item of the enemy based on the pool
                    Item item = new Item(this.position.getX(), this.position.getY(), new ItemStack(droppedItemType, 1));
                    item.velocity.set(rngX, rngY);
                    Main.getGamePanel().addNewEntity(item);
                }
            }
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

        // If the enemy is moving enough, draw the sprite in the direction that movement is
        if (largest > 0.5) {
            switch (direction) {
                case "left" -> texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + 0);
                case "right" -> texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + 1);
                case "up" -> texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + 2);
                case "down" -> texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + 3);
            }
        }

        // Idle animation
        else {
            if (direction.equals("left")) {
                texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + 4);
            } else {
                texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + 5);
            }
        }

        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());

        // If an enemy gets hit, tint it red and have it fade until its immunity frames run out
        if (this.damageTimer > 0) {
            BufferedImage wow = tint(texture, 1, 0, 0, ((float) this.damageTimer / this.immunity * 0.8f) / 2);
            ctx.drawImage(wow, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
        }
    }

    // Configures the tint used above
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

    public boolean dealDamage(int amount) {
        if (this.damageTimer > 0) return false;

        this.health -= Math.max(0, amount);
        this.damageTimer = this.immunity;
        return true;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}