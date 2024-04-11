package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.KeyHandler;
import org.acitech.Main;
import org.acitech.entities.projectiles.Aquaball;
import org.acitech.entities.projectiles.Fireball;
import org.acitech.inventory.Inventory;
import org.acitech.inventory.ItemStack;
import org.acitech.inventory.ItemType;
import org.acitech.utils.Vector2d;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity {

    // Animation & Visuals
    private int animationTick = 0;
    public int aniLength = 6;
    public int aniFrameDuration = 4;
    public int width = 160;
    public int height = 160;

    // Stats
        // UI
    public int maxHealth = 6; // Can be changed in gameplay (Default: 6)
    public int health = this.maxHealth; // Can be changed in gameplay
    public boolean alive = true;
    public int maxMana = 6; // Can be changed in gameplay (Default: 6)
    public int mana = this.maxMana; // Can be changed in gameplay
    public int xpCount = 0; // Can be changed in gameplay (Default: 0)
    public int level = 1; // Can be changed in gameplay (Default: 1)
    public int currentStreak = 0; // Can be changed in gameplay (Default: 0)
    public int streakTimerMax = 180; // Can be changed in gameplay (Default: 180)
    public int streakTimer = 0;

        // Combat & Movement
    public int scratchDamage = 1; // Can be changed in gameplay (Default: 1)
    public int scratchCooldown = 20; // Can be changed in gameplay (Default: 20)
    public int scratchTimer = this.scratchCooldown;
    public int spellCooldown = 40; // Can be changed in gameplay (Default: 40)
    public int spellTimer = this.spellCooldown;
    public double moveSpeed = 0.5;
    public int meleeDefense = 0; // Can be changed in gameplay (Default: 0)
    public int magicDefense = 0; // Can be changed in gameplay (Default: 0)
    public int kbMult = 20; // Can be changed in gameplay (Default: 20)
    public int immunity = 30; // Can be changed in gameplay (Default: 30)
    public int damageTimer;
    public String elementState = "base";
    // public boolean bufferInput = false; // For implementing a buffer system later maybe
    public Player() {
        this.friction = 0.9;
    }

    // Inventory
    public Inventory defaultInv = new Inventory(8);
    public Inventory spellInv = new Inventory(2);
    public Inventory potionInv = new Inventory(4);

    // Misc.
    public Vector2d clickPos = new Vector2d();

    // Load important assets
    Clip sndScratch = Main.getResources().getSound("scratch");
    Clip sndFire = Main.getResources().getSound("fireball");

    @Override
    // Do this stuff every frame
    protected void tick(double delta) {
        if (this.alive) {
            // Decrements cooldowns
            if (this.damageTimer > 0) {
                this.damageTimer--;
            }
            if (this.scratchTimer > 0) {
                this.scratchTimer--;
            }
            if (this.spellTimer > 0) {
                this.spellTimer--;
            }

            if (this.streakTimer > 0) {
                this.streakTimer--;
            }
            if (this.streakTimer <= 0) {
                this.currentStreak = 0;
            }

            // Checks all the possible keys
            if (KeyHandler.wDown) {
                this.acceleration = this.acceleration.add(new Vector2d(0, -this.moveSpeed));
            }
            if (KeyHandler.aDown) {
                this.acceleration = this.acceleration.add(new Vector2d(-this.moveSpeed, 0));
            }
            if (KeyHandler.sDown) {
                this.acceleration = this.acceleration.add(new Vector2d(0, this.moveSpeed));
            }
            if (KeyHandler.dDown) {
                this.acceleration = this.acceleration.add(new Vector2d(this.moveSpeed, 0));
            }

            // Placeholders for testing
            if (KeyHandler.zDown) {
                if (this.health > 0) {
                    this.health -= 1;
                }
            }
            if (KeyHandler.xDown) {
                if (this.mana > 0) {
                    this.mana -= 1;
                }
            }

            this.deathCheck();

            // Checks for which spell effects to use
            if (KeyHandler.shiftDown && KeyHandler.spaceDown) {
                elementState = "base";
            } else if (KeyHandler.shiftDown) {
                if (this.spellInv.getItem(0) != null) {
                    switch (this.spellInv.getItem(0).getType()) {
                        case FIRE_TOME_1 -> elementState = "fire";
                        case AQUA_TOME_1 -> elementState = "aqua";
                    }
                }
            } else if (KeyHandler.spaceDown) {
                if (this.spellInv.getItem(1) != null) {
                    switch (this.spellInv.getItem(1).getType()) {
                        case FIRE_TOME_1 -> elementState = "fire";
                        case AQUA_TOME_1 -> elementState = "aqua";
                    }
                }
            } else {
                elementState = "base";
            }

            // Checks for mouse input
            if (!KeyHandler.mouseClicks.isEmpty()) {
                for (KeyHandler.Click click : KeyHandler.mouseClicks) {
                    // Checks for clicks (Scratch / Other thing)
                    switch (click.getButton()) {

                        // Left Click
                        case (1) -> { // Scratches
                            if (this.scratchTimer == 0) {
                                clickPos.set(click.getX(), click.getY());
                                double angle = click.toVector().angleTo(Main.getGamePanel().getCameraCenter().getY() + width / 2d, Main.getGamePanel().getCameraCenter().getX() + height / 2d) + Math.PI;
                                Scratch scratch = new Scratch((int) this.position.getX(), (int) this.position.getY(), 120, angle, this.elementState);
                                sndScratch.setFramePosition(0);
                                sndScratch.loop(0);
                                Main.getGamePanel().addNewEntity(scratch);
                                sndScratch.start();
                                this.scratchTimer = this.scratchCooldown;
                            }
                        }

                        // Middle Click
                        case (2) -> {
                            System.out.print("middle click");
//                            if (Check if there is an item in that slot, check what it does when used (in ItemType)) {
//                              todo: hey julian this is for using an item after you add the cursor in, doesn't have to be middle click but it can be, have fun :D
//                            }
                        }

                        // Right Click
                        case (3) -> {
                            switch (elementState) {
                                case ("base") -> { // todo: Pounces
                                    System.out.print("hello");
                                    System.out.print("hello");
                                }

                                case ("fire") -> { // Uses a fireball
                                    if (this.spellTimer == 0) { // If spells are off cooldown
                                        double angle = click.toVector().angleTo(Main.getGamePanel().getCameraCenter().getY() + width / 2d, Main.getGamePanel().getCameraCenter().getX() + height / 2d) + Math.PI;
                                        Fireball fireball = new Fireball(this.position.getX(), this.position.getY(), angle);

                                        if (this.mana >= fireball.manaCost) {
                                            Main.getGamePanel().addNewEntity(fireball);
                                            sndFire.setFramePosition(0);
                                            sndFire.loop(0);
                                            sndFire.start();
                                            this.mana -= fireball.manaCost;
                                            this.spellTimer = this.spellCooldown;
                                        }
                                    }
                                }

                                case ("aqua") -> { // Uses a aquaball
                                    if (this.spellTimer == 0) {
                                        double angle = click.toVector().angleTo(Main.getGamePanel().getCameraCenter().getY() + width / 2d, Main.getGamePanel().getCameraCenter().getX() + height / 2d) + Math.PI;
                                        Aquaball aquaball = new Aquaball(this.position.getX(), this.position.getY(), angle);

                                        if (this.mana >= aquaball.manaCost) {
                                            Main.getGamePanel().addNewEntity(aquaball);
                                            sndFire.setFramePosition(0);
                                            sndFire.loop(0);
                                            sndFire.start();
                                            this.mana -= aquaball.manaCost;
                                            this.spellTimer = this.spellCooldown;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void damageTaken(int damage, String element) {
        this.health -= Math.max(damage - this.meleeDefense, 0);
        this.damageTimer = this.immunity;
        this.streakTimer = 0;
        this.currentStreak = 0;
    }

    public void deathCheck() {
        if (this.health <= 0) {
            this.alive = false;
            this.kbMult = 0;
        }
    }

    public String levelUpCheck() {
        switch (this.level) {
            case (1) -> { // Raise mana cap by 1 star, restore mana
                if (this.xpCount >= 20) {
                    this.level += 1;
                    this.maxMana += 6;
                    this.mana = this.maxMana;
                    return "+Mana!";
                }
            }
            case (2) -> { // Make streak timer take longer to dissipate, adjust current streak timer if not 0
                if (this.xpCount >= 50) {
                    this.level += 1;
                    this.streakTimerMax += 60;
                    if (this.streakTimer > 0) {
                        this.streakTimer += 60;
                    }
                    return "+Streak Duration!";
                }
            }
            case (3) -> { // Raise max health by 1 heart, restore health
                if (this.xpCount >= 100) {
                    this.level += 1;
                    this.maxHealth += 2;
                    this.health = maxHealth;
                    return "+HP!";
                }
            }
            case (4) -> { // Raise scratch damage by 1
                if (this.xpCount >= 200) {
                    this.level += 1;
                    this.scratchDamage += 1;
                    return "+Scratch Damage!";
                }
            }
            default -> {}
        }
        return "";
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

        // If the player is moving enough, draw the sprite in the direction that movement is
        if (largest > 0.5) {
            switch (direction) {
                case "left" -> texture = Main.getResources().getTexture("player/" + elementState + "/" + aniFrame + ":0");
                case "right" -> texture = Main.getResources().getTexture("player/" + elementState + "/" + aniFrame + ":1");
                case "up" -> texture = Main.getResources().getTexture("player/" + elementState + "/" + aniFrame + ":2");
                case "down" -> texture = Main.getResources().getTexture("player/" + elementState + "/" + aniFrame + ":3");
            }
        }

        // Idle animation
        else {
            if (direction.equals("left")) {
                texture = Main.getResources().getTexture("player/" + elementState + "/" + aniFrame + ":4");
            } else {
                texture = Main.getResources().getTexture("player/" + elementState + "/" + aniFrame + ":5");
            }
        }

        if (!this.alive) {
            texture = Main.getResources().getTexture("player/death");
        }

        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }

    /**
     * Loops through a list of items to be added to the players inventory
     * @return List of items that have been picked up
     */
    public ArrayList<Item> pickupItems(ArrayList<Item> items) {
        for (Item item : new ArrayList<>(items)) {

            switch (item.getItemStack().getType()) {
                case HEALTH_POTION -> {
                    this.health++;
                }
                case MANA_POTION -> {
                    this.mana += 12;
                }
                case ATTACK_POTION -> {
                    this.scratchDamage++;
                }
                case SPEED_POTION -> {
                    this.moveSpeed += 0.2;
                }
            }

            ItemStack remaining;

            if (ItemType.getSpellTypes().contains(item.getItemStack().getType())) {
                remaining = spellInv.addItem(item.getItemStack());
            } else if (ItemType.getPotionTypes().contains(item.getItemStack().getType())) {
                remaining = potionInv.addItem(item.getItemStack());
            } else {
                remaining = defaultInv.addItem(item.getItemStack());
            }

            if (remaining != null) {
                item.setItemStack(remaining);
                items.remove(item);
            }
        }

        return items;
    }
}
