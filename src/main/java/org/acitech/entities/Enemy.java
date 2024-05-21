package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.UI;
import org.acitech.assets.AssetLoader;
import org.acitech.entities.ai.AI;
import org.acitech.entities.ai.Fighter;
import org.acitech.entities.ai.Hawk;
import org.acitech.entities.ai.Skitter;
import org.acitech.inventory.ItemStack;
import org.acitech.inventory.ItemType;
import org.acitech.tilemap.Room;
import org.acitech.utils.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

abstract public class Enemy extends Entity {

    // Identifiers
    protected final String enemyName;
    private final AI enemyAI;
    public ArrayList<ItemType> itemPool = new ArrayList<>();

    // Animation & Visuals
    protected int animationTick = 0;
    public int aniLength = 1;
    public int aniFrameDuration = 1;
    public int width = 160;
    public int height = 160;

    // Stats & Combat
    protected int health, maxHealth = 1;
    // protected int mana, maxMana = 0; // To be used in the future maybe
    public int attackDamage = 1;
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

    public Enemy(Room room, double startX, double startY, String enemyName, String ai) {
        super(room);

        this.position = new Vector2d(startX, startY);
        this.friction = 0.9;
        this.enemyName = enemyName;

        switch (ai.toLowerCase()) {
            case "fighter" -> this.enemyAI = new Fighter(this);
            case "skitter" -> this.enemyAI = new Skitter(this);
            case "hawk" -> this.enemyAI = new Hawk(this);
            default -> this.enemyAI = null;
        }
    }

    @Override
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
                for (int i = 0; i < Math.ceil((xpDrop - 1) * (2 * (0.5 + GamePanel.getPlayer().currentStreak / 10.0))); i++) {

                    // Gets some random X & Y velocities to add to the drop velocity to scatter XP
                    double rngX = new Random().nextDouble(-xpScatter, xpScatter);
                    double rngY = new Random().nextDouble(-xpScatter, xpScatter);

                    // Drops the XP with the random velocities added
                    Experience experience = new Experience(this.getRoom(), this.position.getX(), this.position.getY(), this.xpValue);
                    experience.velocity.set(rngX, rngY);
                }
            }

            // Increments the streak
            GamePanel.getPlayer().currentStreak += 1;

            // cause there do be stuff in the item pool
            if (!itemPool.isEmpty()) {
                for (int i = 0; i < itemDrop; i++) {

                    // Get a random number to get an item from the pool + X & Y velocities to add
                    double rngX = new Random().nextDouble(-itemScatter, itemScatter);
                    double rngY = new Random().nextDouble(-itemScatter, itemScatter);
                    int rngIndex = new Random().nextInt(itemPool.size());
                    ItemType droppedItemType = itemPool.get(rngIndex);

                    // Spawn the item of the enemy based on the pool
                    Item item = new Item(getRoom(), this.position.getX(), this.position.getY(), new ItemStack(droppedItemType, 1));
                    item.velocity.set(rngX, rngY);
                }
            }
        }
    }
    @Override
    public void draw(Graphics2D ctx) {
        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        // Check which direction has the largest speed
        double largest = 0;
        String direction = "right";

        if (Math.abs(this.velocity.getX()) > largest) {
            largest = Math.abs(this.velocity.getX());
            direction = this.velocity.getX() > 0 ? "right" : "left";
        }
        if (Math.abs(this.velocity.getY()) > largest) {
            largest = Math.abs(this.velocity.getY());
            direction = this.velocity.getY() > 0 ? "down" : "up";
        }

        // If the enemy is moving enough, draw the sprite in the direction that movement is
        Integer spriteY = null;

        if (largest > 0.5) {
            switch (direction) {
                case "left" -> spriteY = 0;
                case "right" -> spriteY = 1;
                case "up" -> spriteY = 2;
                case "down" -> spriteY = 3;
            }
        } else { // Idle animation
            if (direction.equals("left")) {
                spriteY = 4;
            } else {
                spriteY = 5;
            }
        }

        BufferedImage texture = AssetLoader.getEnemyByName(enemyName).getSprite(aniFrame, spriteY);
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.getCamera().getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.getCamera().getY(), width, height, Main.getGamePanel());

        // If an enemy gets hit, tint it red and have it fade until its immunity frames run out
        if (this.damageTimer > 0) {
            BufferedImage wow = UI.tintImage(texture, 1, 0, 0, ((float) this.damageTimer / this.immunity * 0.8f) / 2);
            ctx.drawImage(wow, (int) this.position.getX() - width / 2 - (int) GamePanel.getCamera().getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.getCamera().getY(), width, height, Main.getGamePanel());
        }
    }

    public boolean dealDamage(int amount, Entity damager) {
        if (this.damageTimer > 0) return false;

        this.health -= Math.max(0, amount);
        this.damageTimer = this.immunity;

        if (this.enemyAI != null) {
            this.enemyAI.damageHandler(damager);
        }
        return true;
    }

    public void dealKnockback(double force, Vector2d origin, boolean override) {
        Vector2d direction = this.position.directionTo(origin).multiply(force);

        if (override) {
            this.velocity.set(direction);
        } else {
            this.acceleration.add(direction);
        }
    }

    public int getHealth() {
        return health;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public AI getAI() {
        return enemyAI;
    }
    public int getAnimationTick() {
        return animationTick;
    }
    public void resetTick(){
        animationTick = 0;
    }
}