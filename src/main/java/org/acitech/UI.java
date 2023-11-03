package org.acitech;

import org.acitech.inventory.Inventory;
import org.acitech.inventory.ItemStack;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {

    public void draw(Graphics2D ctx) {
        drawHealth(ctx);
        drawMana(ctx);
        drawInventory(ctx);
    }

    public void drawHealth(Graphics2D ctx) {
        int x = 0;
        int cobalt = GamePanel.player.health; // thanks cobalt :)

        for (int i = 0; i < GamePanel.player.maxHealth / 2; i++) {
            BufferedImage texture;

            if (cobalt >= 2) {
                texture = Main.getResources().getTexture("ui/hearts/0:0"); // full
                cobalt -= 2;
            } else if (cobalt == 1) {
                texture = Main.getResources().getTexture("ui/hearts/1:0"); // half
                cobalt--;
            } else {
                texture = Main.getResources().getTexture("ui/hearts/2:0"); // empty
            }

            ctx.drawImage(texture, x + 16, 16, 72, 64, Main.getGamePanel());
            x += texture.getWidth() * 4 + 4;
        }
    }

    public void drawMana(Graphics2D ctx) {
        int x = 0;
        int cobalt = GamePanel.player.mana; // thanks cobalt :)

        for (int i = 0; i < GamePanel.player.maxMana / 6; i++) {
            BufferedImage texture;

            if (cobalt >= 6) {
                texture = Main.getResources().getTexture("ui/mana/0:0"); // full pink
                cobalt -= 6;
            } else if (cobalt == 5) {
                texture = Main.getResources().getTexture("ui/mana/1:0"); // full blue
                cobalt -= 5;
            } else if (cobalt == 4) {
                texture = Main.getResources().getTexture("ui/mana/2:0"); // blue - 1
                cobalt -= 4;
            } else if (cobalt == 3) {
                texture = Main.getResources().getTexture("ui/mana/3:0"); // blue - 2
                cobalt -= 3;
            } else if (cobalt == 2) {
                texture = Main.getResources().getTexture("ui/mana/4:0"); // blue - 3
                cobalt -= 2;
            } else if (cobalt == 1) {
                texture = Main.getResources().getTexture("ui/mana/5:0"); // blue - 4
                cobalt--;
            } else {
                texture = Main.getResources().getTexture("ui/mana/6:0"); // empty
            }

            ctx.drawImage(texture, x + 16, 84, 52, 52, Main.getGamePanel());

            x += texture.getWidth() * 4 + 4;
        }
    }

    public void drawInventory(Graphics2D ctx) {
        int invX = Main.getGamePanel().getWidth() / 2 - 346;
        int invY = Main.getGamePanel().getHeight() - 96;
        int invWidth = 692;
        int invHeight = 64;

        BufferedImage barTexture = Main.getResources().getTexture("ui/inv_bar_default");
        ctx.drawImage(barTexture, invX, invY, invWidth, invHeight, Main.getGamePanel());

        for (int slot = 2; slot < GamePanel.player.inventory.getMaxSlots(); slot++) {
            ItemStack item = GamePanel.player.inventory.getItem(slot);
            if (item == null) continue;

            BufferedImage itemTexture = item.getType().getTexture();

            int itemPos = Main.getGamePanel().getWidth() / 2 - invWidth / 2 - 32 + 64 * slot;
            ctx.drawImage(itemTexture, itemPos, invY, 48, 48, Main.getGamePanel());
        }
    }
}
