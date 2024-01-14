package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.KeyHandler;
import org.acitech.Main;
import org.acitech.inventory.Inventory;
import org.acitech.inventory.ItemStack;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

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

        // Combat & Movement
    public int scratchDamage = 1; // Can be changed in gameplay
    public int scratchCooldown = 20; // Can be changed in gameplay
    public int scratchTimer = scratchCooldown;
    public int meleeDefense = 0; // Can be changed in gameplay
    public int magicDefense = 0; // Can be changed in gameplay
    public int kbMult = 20; // Can be changed in gameplay
    public int immunity = 30; // Can be changed in gameplay
    public int damageTimer;
    public Player() {
        this.friction = 0.9;
    }

    // Inventory
    public Inventory inventory1 = new Inventory(8);
    public Inventory inventory2 = new Inventory(2);

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

        // Checks all the possible keys
        if (KeyHandler.wDown) {
            this.acceleration = this.acceleration.add(new Vector2D(0, -.5));
        }
        if (KeyHandler.aDown) {
            this.acceleration = this.acceleration.add(new Vector2D(-.5, 0));
        }
        if (KeyHandler.sDown) {
            this.acceleration = this.acceleration.add(new Vector2D(0, .5));
        }
        if (KeyHandler.dDown) {
            this.acceleration = this.acceleration.add(new Vector2D(.5, 0));
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

        // Checks for mouse input
        if (!KeyHandler.mouseClicks.isEmpty()) {

            // Checks for clicks (Scratch)
            for (KeyHandler.Click click : KeyHandler.mouseClicks) {
                if (this.scratchTimer == 0) {
                    double angle = Math.atan2(Main.getGamePanel().getCameraCenter().getY() + width / 2d - click.getY(), Main.getGamePanel().getCameraCenter().getX() + height / 2d - click.getX());
                    Scratch scratch = new Scratch((int) this.position.getX(), (int) this.position.getY(), 120, angle);
                    Clip clip = Main.getResources().getSound("player_scratch");
                    clip.setFramePosition(0);
                    clip.loop(0);
                    Main.getGamePanel().addNewEntity(scratch);
                    clip.start();
                    this.scratchTimer = scratchCooldown;
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

        // If the player is moving enough, draw the sprite in the direction that movement is
        if (largest > 0.5) {
            switch (direction) {
                case "up" -> texture = Main.getResources().getTexture("player/running/" + aniFrame + ":3");
                case "down" -> texture = Main.getResources().getTexture("player/running/" + aniFrame + ":2");
                case "right" -> texture = Main.getResources().getTexture("player/running/" + aniFrame + ":1");
                case "left" -> texture = Main.getResources().getTexture("player/running/" + aniFrame + ":0");
            }
        }

        // Idle animation
        else {
            if (direction.equals("left")) {
                texture = Main.getResources().getTexture("player/idle/" + aniFrame / 3 + ":0");
            } else {
                texture = Main.getResources().getTexture("player/idle/" + aniFrame / 3 + ":1");
            }
        }

        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }

    // TODO: comment me
    public ArrayList<Item> pickupItems(ArrayList<Item> items) {
        for (Item item : items) {
            ItemStack remaining = GamePanel.player.inventory1.addItem(item.getItemStack());

            if (remaining != null) {
                item.setItemStack(remaining);
                items.remove(item);
            }
        }

        return items;
    }
}
