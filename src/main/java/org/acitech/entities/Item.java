package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.inventory.ItemStack;
import org.acitech.utils.Vector2d;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Item extends Entity {

    private int width = 36;
    private int height = 36;
    public int pickupImmunity = 20;
    public double moveSpeed = 0.75;
    private boolean isDisappearing = false;
    private boolean inPickupRange = false;
    private ItemStack itemStack;

    public Item(double startX, double startY, ItemStack itemStack) {
        this.position = new Vector2d(startX, startY);
        this.friction = 0.95;
        this.itemStack = itemStack;
    }

    @Override
    // Do this stuff every frame
    protected void tick(double delta) {
        Vector2d playerPos = GamePanel.player.position;
        if (this.pickupImmunity > 0) {
            this.pickupImmunity--;
        }

        // Gets the angle between the player and the item
        Vector2d direction = playerPos.directionTo(this.position).multiply(moveSpeed * 0.5);

        if (this.pickupImmunity <= 0) {
            // Sucks up the item if it's close enough to the player
            if (this.position.distance(playerPos) < 100) {
                this.acceleration.add(direction);

                this.inPickupRange = true;
            } else {
                this.inPickupRange = false;
            }
        }

        // If the entity is disappearing, decrease its size and dispose it
        if (this.isDisappearing) {
            this.height = (int) (this.height * 0.85);
            this.width = (int) (this.width * 0.85);

            if (this.height < 2) {
                this.dispose();
            }
        }
    }

    @Override
    // Handles graphics
    public void draw(Graphics2D ctx) {
        BufferedImage texture = this.itemStack.getType().getTexture();
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }

    public boolean isInPickupRange() {
        return inPickupRange;
    }

    /**
     * Marks the entity up for disappearing
     */
    public void disappear() {
        this.isDisappearing = true;
    }

    public boolean isDisappearing() {
        return isDisappearing;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}