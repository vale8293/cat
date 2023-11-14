package org.acitech.entities.items;

import org.acitech.GamePanel;
import org.acitech.Main;
import org.acitech.entities.Item;
import org.acitech.inventory.ItemStack;
import org.acitech.inventory.ItemType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Water extends Item {

    private int width = 36;
    private int height = 36;

    public Water(double startX, double startY) {
        super(startX, startY);
    }

    @Override
    protected void tick(double delta) {
        super.tick(delta);

        if (gettingPickedUp) {
            System.out.println("I AM BEING PCIEKDKED UP");

            ItemStack remaining = GamePanel.player.inventory1.addItem(new ItemStack(ItemType.WATER, 1));

            if (remaining == null) {
                System.out.println("OOPS ITEM WENT INTO THE VOID");
            }
        }
    }

    @Override
    public void draw(Graphics2D ctx) {
        BufferedImage texture = Main.getResources().getTexture("items/water_material");
        ctx.drawImage(texture, (int) this.position.getX() - width / 2, (int) this.position.getY() - height / 2, width, height, Main.getGamePanel());
    }
}
