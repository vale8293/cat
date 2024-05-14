package org.acitech.entities.enemies;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.UI;
import org.acitech.entities.Enemy;
import org.acitech.entities.ai.Hawk;
import org.acitech.inventory.ItemType;
import org.acitech.tilemap.Room;
import org.acitech.utils.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pteri extends Enemy {

    public Pteri(Room room, double startX, double startY) {
        super(room, startX, startY, "Pteri", "Hawk");

        // Animation & Visuals
        this.aniLength = 6;
        this.aniFrameDuration = 5;
        this.width = 192;
        this.height = 192;

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
    public void draw(Graphics2D ctx) {
        BufferedImage texture;
        Hawk hawkAI = (Hawk) getAI();
        int stateNum = hawkAI.getStateNum();
        int direction;

        // Increments the frame of the animation
        animationTick += 1;
        animationTick = animationTick % (aniLength * aniFrameDuration);
        int aniFrame = animationTick / (aniFrameDuration);

        // Determine the facing direction of the boss
        Vector2d playerPos = GamePanel.getPlayer().position;
        if (this.position.getX() > playerPos.getX()) {
            direction = 0; // Add 0 in texture (Facing left)
        } else {
            direction = 1; // Add 1 in texture (Facing right)
        }

        System.out.println(aniFrame + ", " + (2 * stateNum + direction));
        texture = Main.getResources().getTexture("enemies/" + enemyName + "/" + aniFrame + ":" + (2 * stateNum + direction));
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.getCamera().getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.getCamera().getY(), width, height, Main.getGamePanel());

        // If an enemy gets hit, tint it red and have it fade until its immunity frames run out
        if (this.damageTimer > 0) {
            BufferedImage wow = UI.tintImage(texture, 1, 0, 0, ((float) this.damageTimer / this.immunity * 0.8f) / 2);
            ctx.drawImage(wow, (int) this.position.getX() - width / 2 - (int) GamePanel.getCamera().getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.getCamera().getY(), width, height, Main.getGamePanel());
        }
    }
}