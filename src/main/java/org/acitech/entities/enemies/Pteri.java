package org.acitech.entities.enemies;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.UI;
import org.acitech.entities.Enemy;
import org.acitech.inventory.ItemType;
import org.acitech.tilemap.Room;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pteri extends Enemy {
    private String facing;

    public Pteri(Room room, double startX, double startY) {
        super(room, startX, startY, "Pteri", "Hawk");

        // Animation & Visuals
        this.facing = "left";
        this.aniLength = 6;
        this.aniFrameDuration = 4;
        this.width = 160;
        this.height = 160;

        // Stats
        this.maxHealth = 80;
        this.health = maxHealth;
        this.moveSpeed = 1.1;
        this.kbMult = 30;
        this.aggroDistance = 700;

        // Rewards
        this.xpDrop = 200;

        // Item Pool (One potion of any type but mana)
        this.itemPool.add(ItemType.HEALTH_POTION);
        this.itemPool.add(ItemType.ATTACK_POTION);
        this.itemPool.add(ItemType.SPEED_POTION);
    }

    @Override
    // Handles graphics
    public void draw(Graphics2D ctx) {
        BufferedImage texture = Main.getResources().getTexture("cow");

        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        String direction;

//        // Determine the facing direction of the boss
//        Vector2d playerPos = GamePanel.getPlayer().position;
//        if (this.position.getX() > playerPos.getX()) {
//            direction = "left";
//        } else {
//            direction = "right";
//        }
//
//        // If the enemy is moving enough, draw the sprite in the direction that movement is
//        switch (direction) {
//            case "left" -> texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + 0);
//            case "right" -> texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + 1);
//        }

        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.getCamera().getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.getCamera().getY(), width, height, Main.getGamePanel());

        // If an enemy gets hit, tint it red and have it fade until its immunity frames run out
        if (this.damageTimer > 0) {
            BufferedImage wow = UI.tintImage(texture, 1, 0, 0, ((float) this.damageTimer / this.immunity * 0.8f) / 2);
            ctx.drawImage(wow, (int) this.position.getX() - width / 2 - (int) GamePanel.getCamera().getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.getCamera().getY(), width, height, Main.getGamePanel());
        }
    }
}