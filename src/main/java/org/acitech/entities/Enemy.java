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

abstract public class Enemy extends Entity {

    // Identifiers
    private final String enemyName;
    private final String enemyAI;
    public ArrayList<ItemType> itemPool = new ArrayList<>();

    // Animation & Visuals
    private int animationTick = 0;
    public int aniLength = 1;
    public int aniFrameDuration = 1;
    protected int width = 160;
    protected int height = 160;

    // Stats
        // Combat
    public int maxHealth = 1;
    public int health = maxHealth;
    public int maxMana = 0;
    public int mana = maxMana;
    public int damage = 1;
    public String damageElement = "None";
    public int defense = 0;
    public double moveSpeed = 1;
    public int kbMult = 20;
    public int aggroDistance = 300;
    public int immunity = 20;
    public int damageTimer;

        // Rewards
    public int xpDrop = 1;
    public int xpValue = 1;
    public int xpScatter = 100;
    public int itemDrop = 1;
    public int itemScatter = 50;

    public Enemy(double startX, double startY, String enemyName, String enemyAI) {
        this.position = new Vector2D(startX, startY);
        this.friction = 0.9;
        this.enemyName = enemyName;
        this.enemyAI = enemyAI;
    }

    protected abstract void scratchHandler();

    @Override
    // Do this stuff every frame
    protected void tick(double delta) {
        Vector2D playerPos = GamePanel.player.position; // Get player position
        if (this.damageTimer > 0) this.damageTimer--; // Reduce damage timer

        // Gets the angle between the player and the enemy
        double angle = Math.atan2(playerPos.getY() - this.position.getY(), playerPos.getX() - this.position.getX());
        double x = Math.cos(angle) * 0.5;
        double y = Math.sin(angle) * 0.5;

        if (this.enemyAI.equals("Fighter")) {
            fighterAI(x, y, playerPos);
        }
        else if (this.enemyAI.equals("Skitter")) {
            skitterAI(x, y, playerPos);
        }

        // boolean gotScratched = scratchCheck(x, y); // Move into AIs
        deathCheck();

    }

    // Defines basic AI for enemies like Rico and Pepto
    protected void fighterAI(double x, double y, Vector2D playerPos) {
        // If the enemy is close enough to the player, start Fighter AI
        if (this.position.distance(playerPos) < aggroDistance) {
            this.acceleration = new Vector2D(x, y);
            this.acceleration = this.acceleration.scalarMultiply(moveSpeed);

            // If the enemy makes contact with the player
            if (this.position.distance(playerPos) < ((double) this.width /2) ||
                    this.position.distance(playerPos) < ((double) this.height /2)) {

                // Deal damage w/ elemental effect (none by default)
                if (GamePanel.player.damageTimer == 0) {
                    GamePanel.player.damageTaken(this.damage, this.damageElement);
                }

                // Knock back the enemy and player
                this.velocity = new Vector2D(this.kbMult * -x, this.kbMult * -y);
                GamePanel.player.velocity = this.velocity.scalarMultiply((double) -GamePanel.player.kbMult / this.kbMult);
            }
        }
    }

    // Defines basic AI for enemies like Jordan
    protected void skitterAI(double x, double y, Vector2D playerPos) {
        // Declares AI Specific variables
        boolean gotScratched = scratchCheck(x, y);

        // If the enemy is close enough to the player, start Skitter AI
        if (this.position.distance(playerPos) < aggroDistance) {
            this.acceleration = new Vector2D(x, y);
            this.acceleration = this.acceleration.scalarMultiply(moveSpeed);

            // If the enemy makes contact with the player
            if (this.position.distance(playerPos) < ((double) this.width / 2) ||
                    this.position.distance(playerPos) < ((double) this.height / 2)) {

                // Deal damage w/ elemental effect (none by default)
                if (GamePanel.player.damageTimer == 0) {
                    GamePanel.player.damageTaken(this.damage, this.damageElement);
                }

                // Knock back the enemy and player
                this.velocity = new Vector2D(this.kbMult * -x, this.kbMult * -y);
                GamePanel.player.velocity = this.velocity.scalarMultiply((double) -GamePanel.player.kbMult / this.kbMult);
            }
        }
    }

    // Defines basic AI for when the player scratches an enemy
    protected boolean scratchCheck(double x, double y) {
        boolean gotScratched = false;

        // Looks for any instances of a scratch
        for (Entity entity : GamePanel.entities) {
            if (!(entity instanceof Scratch scratch)) continue;

            // Gets the position of the scratch
            double dist = scratch.position.distance(this.position);

            // If the scratch makes contact with the enemy
            // regain 1 mana
            // knock it back, lose 1hp, and start i-frames, extend streak
            if (dist < 100) {
                gotScratched = true;

                if (this.damageTimer == 0) {
                    if (GamePanel.player.mana < GamePanel.player.maxMana) {
                        GamePanel.player.mana += 1;
                    }
                    this.velocity = new Vector2D(this.kbMult * -x, this.kbMult * -y);
                    this.health -= Math.max(GamePanel.player.scratchDamage - this.defense, 0);
                    this.damageTimer = immunity;
                    GamePanel.player.streakTimer = GamePanel.player.streakTimerMax;
                }
            }
        }

        if (gotScratched) { // If the enemy got scratched, Trigger a scratch event
            this.scratchHandler();
        }
        return gotScratched;
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
                    double rngX = new Random().nextInt(xpScatter);
                    double rngY = new Random().nextInt(xpScatter);

                    // Drops the XP with the random velocities added
                    Experience experience = new Experience(this.position.getX(), this.position.getY(), this.xpValue);
                    experience.velocity = this.velocity.add(2, new Vector2D(rngX / 10, rngY / 10));
                    Main.getGamePanel().addNewEntity(experience);
                }
            }

            // Increments the streak
            GamePanel.player.currentStreak += 1;

            // cause there do be stuff in the item pool
            if (!itemPool.isEmpty()) {
                for (int i = 0; i < itemDrop; i++) {

                    // Get a random number to get an item from the pool + X & Y velocities to add
                    double rngX = new Random().nextInt(itemScatter);
                    double rngY = new Random().nextInt(itemScatter);
                    int rngIndex = new Random().nextInt(itemPool.size());
                    ItemType droppedItemType = itemPool.get(rngIndex);

                    // Spawn the item of the enemy based on the pool
                    Item item = new Item(this.position.getX(), this.position.getY(), new ItemStack(droppedItemType, 1));
                    item.velocity = this.velocity.add(2, new Vector2D(rngX / 10, rngY / 10));
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
}