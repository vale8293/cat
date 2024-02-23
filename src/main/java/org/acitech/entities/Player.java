package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.KeyHandler;
import org.acitech.Main;
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
    public int maxHealth = 6; // Can be changed in gameplay
    public int health = maxHealth; // Can be changed in gameplay
    public int maxMana = 6; // Can be changed in gameplay
    public int mana = maxMana; // Can be changed in gameplay
    public int xpCount = 0; // Can be changed in gameplay
    public int currentStreak = 0;
    public int streakTimerMax = 180;
    public int streakTimer = 0;

        // Combat & Movement
    public int scratchDamage = 1; // Can be changed in gameplay
    public int scratchCooldown = 20; // Can be changed in gameplay
    public int scratchTimer = scratchCooldown;
    public int meleeDefense = 0; // Can be changed in gameplay
    public int magicDefense = 0; // Can be changed in gameplay
    public int kbMult = 20; // Can be changed in gameplay
    public int immunity = 30; // Can be changed in gameplay
    public int damageTimer;
    public String elementState = "base";
    public boolean bufferInput = false; // For implementing a buffer system later maybe
    public Player() {
        this.friction = 0.9;
    }

    // Inventory
    public Inventory defaultInv = new Inventory(8);
    public Inventory spellInv = new Inventory(2);

    // Load important assets
    Clip sndScratch = Main.getResources().getSound("player_scratch");

    @Override
    // Do this stuff every frame
    protected void tick(double delta) {

        // Decrements cooldowns
        if (this.damageTimer > 0) {
            this.damageTimer--;
        }
        if (this.scratchTimer > 0) {
            this.scratchTimer--;
        }

        if (this.streakTimer > 0) {
            this.streakTimer--;
        }
        if (this.streakTimer == 0) {
            this.currentStreak = 0;
        }

        // Checks all the possible keys
        if (KeyHandler.wDown) {
            this.acceleration = this.acceleration.add(new Vector2d(0, -.5));
        }
        if (KeyHandler.aDown) {
            this.acceleration = this.acceleration.add(new Vector2d(-.5, 0));
        }
        if (KeyHandler.sDown) {
            this.acceleration = this.acceleration.add(new Vector2d(0, .5));
        }
        if (KeyHandler.dDown) {
            this.acceleration = this.acceleration.add(new Vector2d(.5, 0));
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

        // Checks for which spell effects to use
        if (KeyHandler.shiftDown && KeyHandler.spaceDown) {
            elementState = "base";
        }

        else if (KeyHandler.shiftDown) {
            elementState = "fire";
        }

        else if (KeyHandler.spaceDown) {
            elementState = "base";
        }

        else {
            elementState = "base";
        }

        // Checks for mouse input
        if (!KeyHandler.mouseClicks.isEmpty()) {


            for (KeyHandler.Click click : KeyHandler.mouseClicks) {
                // Checks for clicks (Scratch / Other thing)
                switch (click.getButton()) {

                    // Left Click
                    case (1) -> {
                        if (this.scratchTimer == 0) {
                            double angle = Math.atan2(Main.getGamePanel().getCameraCenter().getY() + width / 2d - click.getY(), Main.getGamePanel().getCameraCenter().getX() + height / 2d - click.getX());
                            Scratch scratch = new Scratch((int) this.position.getX(), (int) this.position.getY(), 120, angle);
                            sndScratch.setFramePosition(0);
                            sndScratch.loop(0);
                            Main.getGamePanel().addNewEntity(scratch);
                            sndScratch.start();
                            this.scratchTimer = scratchCooldown;
                        }
                    }

                    // Middle Click
                    case (2) -> System.out.print("This is click 2 ");


                    // Right Click
                    case (3) -> System.out.print("This is click 3 ");
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

        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }

    /**
     * Loops through a list of items to be added to the players inventory
     * @return List of items that have been picked up
     */
    public ArrayList<Item> pickupItems(ArrayList<Item> items) {
        for (Item item : new ArrayList<>(items)) {
            ItemStack remaining;

            if (ItemType.getSpellTypes().contains(item.getItemStack().getType())) {
                remaining = spellInv.addItem(item.getItemStack());
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
