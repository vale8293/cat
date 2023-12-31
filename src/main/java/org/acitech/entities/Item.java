package org.acitech.entities;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.inventory.ItemStack;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Item extends Entity {

    private int width = 36;
    private int height = 36;
    protected boolean gettingPickedUp = false;
    private ItemStack itemStack;

    public Item(double startX, double startY, ItemStack itemStack) {
        this.position = new Vector2D(startX, startY);
        this.friction = 0.95;
        this.itemStack = itemStack;
    }

    @Override
    protected void tick(double delta) {
        Vector2D playerPos = GamePanel.player.position;

        // Gets the angle between the player and the item
        double angle = Math.atan2(playerPos.getY() - this.position.getY(), playerPos.getX() - this.position.getX());
        double x = Math.cos(angle) * 0.5;
        double y = Math.sin(angle) * 0.5;

        // Sucks up the item if it's close enough to the player
        if (this.position.distance(playerPos) < 100) {
            this.acceleration = new Vector2D(x, y);
            this.height = (int) (this.height * 0.85);
            this.width = (int) (this.width * 0.85);

            if (this.height < 2) {
                gettingPickedUp = true;
            }
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture = this.itemStack.getType().getTexture();
        ctx.drawImage(texture, (int) this.position.getX() - width / 2 - (int) GamePanel.camera.getX(), (int) this.position.getY() - height / 2 - (int) GamePanel.camera.getY(), width, height, Main.getGamePanel());
    }

    public boolean isGettingPickedUp() {
        return gettingPickedUp;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}