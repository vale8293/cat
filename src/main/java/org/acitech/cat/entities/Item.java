package org.acitech.cat.entities;

import org.acitech.cat.GamePanel;
import org.acitech.cat.Main;
import org.acitech.cat.inventory.ItemStack;
import org.acitech.cat.tilemap.Room;
import org.acitech.cat.utils.Vector2d;

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

    public Item(Room room, double startX, double startY, ItemStack itemStack) {
        super(room);

        this.position = new Vector2d(startX, startY);
        this.friction = 0.95;
        this.itemStack = itemStack;
    }

    @Override
    protected void tick(double delta) {
        if (this.pickupImmunity > 0) {
            this.pickupImmunity--;
        }

        if (this.pickupImmunity <= 0) {
            double distance = this.position.distance(GamePanel.getPlayer().position);

            // Sucks up the item if it's close enough to the player
            if (distance < 250) {
                this.acceleration.add(GamePanel.getPlayer().position.directionTo(this.position).multiply(moveSpeed * 0.5));
            }
            this.inPickupRange = distance < 100;
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
    public void draw(Graphics2D ctx) {
        BufferedImage texture = this.itemStack.getType().getTexture();
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.getCamera().getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.getCamera().getY(), width, height, Main.getGamePanel());
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