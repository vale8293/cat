package org.acitech;

import org.acitech.inventory.ItemStack;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {

    // Draws all UI elements
    public void draw(Graphics2D ctx) {
        drawHealth(ctx);
        drawMana(ctx);
        drawInventory(ctx);
        drawStreak(ctx);
        drawXP(ctx);
    }

    // Handles the graphics for the health bar
    public void drawHealth(Graphics2D ctx) {
        int x = 0;
        int cobalt = GamePanel.player.health; // thanks cobalt :)

        for (int i = 0; i < GamePanel.player.maxHealth / 2; i++) {
            BufferedImage texture;

            // Draws a full heart for every 2 health you have
            if (cobalt >= 2) {
                texture = Main.getResources().getTexture("ui/hearts/0:0"); // full
                cobalt -= 2;
            }

            // Draws a half heart for every 1 health aside from the 2s (should be 1 or 0)
            else if (cobalt == 1) {
                texture = Main.getResources().getTexture("ui/hearts/1:0"); // half
                cobalt--;
            }

            // Draws empty hearts for all health remaining in the max that isn't in the current
            else {
                texture = Main.getResources().getTexture("ui/hearts/2:0"); // empty
            }

            // Draws hearts rightward with small gaps between
            ctx.drawImage(texture, x + 16, 16, 72, 64, Main.getGamePanel());
            x += texture.getWidth() * 4 + 4;
        }
    }

    // Handles the mana star(s), functions the same as HP
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

            // Draws stars rightward with small gaps between
            ctx.drawImage(texture, x + 16, 84, 52, 52, Main.getGamePanel());
            x += texture.getWidth() * 4 + 4;
        }
    }

    // Handles the inventory bar and items (might be rewritten)
    public void drawInventory(Graphics2D ctx) {
        int invWidth = Math.min((int) (Main.getGamePanel().getWidth() * 0.75d), 688);
        int invHeight = (int) (invWidth * (64d / 688d));
        int invLeftPadding = (int) (invWidth * (32d / 688d));
        int invX = Main.getGamePanel().getWidth() / 2 - invWidth / 2;
        int invY = Main.getGamePanel().getHeight() - (int) (invHeight * 1.5d);

        BufferedImage barTexture = Main.getResources().getTexture("ui/inv_bar_default");
        ctx.drawImage(barTexture, invX, invY, invWidth, invHeight, Main.getGamePanel());

        int itemScale = (int) (invHeight * 0.8);
        int itemYOffset = (int) (invHeight * 0.1);

        for (int slot = 0; slot < GamePanel.player.spellInv.getMaxSlots(); slot++) {
            ItemStack item = GamePanel.player.spellInv.getItem(slot);
            if (item == null) continue;

            BufferedImage itemTexture = item.getType().getTexture();

            int itemPos = invX + invLeftPadding + (int) (itemScale * 1.25) * (slot); // TODO: FINISH ME
            ctx.drawImage(itemTexture, itemPos, invY + itemYOffset, itemScale, itemScale, Main.getGamePanel());

            int itemCount = item.getCount();
            BufferedImage amountTextureOnes = Main.getResources().getTexture("ui/numbers/" + itemCount % 10 + ":0");
            ctx.drawImage(amountTextureOnes, itemPos + itemScale / 2, invY + itemScale / 2 + itemYOffset, itemScale / 2, itemScale / 2, Main.getGamePanel());

            if (itemCount / 10 > 0) {
                BufferedImage amountTextureTens = Main.getResources().getTexture("ui/numbers/" + (itemCount / 10) % 10 + ":0");
                ctx.drawImage(amountTextureTens, itemPos, invY + itemScale / 2 + itemYOffset, itemScale / 2, itemScale / 2, Main.getGamePanel());
            }
        }

        for (int slot = 0; slot < GamePanel.player.defaultInv.getMaxSlots(); slot++) {
            ItemStack item = GamePanel.player.defaultInv.getItem(slot);
            if (item == null) continue;

            BufferedImage itemTexture = item.getType().getTexture();

            int itemPos = invX + invLeftPadding + (int) (itemScale * 1.25) * (slot + GamePanel.player.spellInv.getMaxSlots());
            ctx.drawImage(itemTexture, itemPos, invY + itemYOffset, itemScale, itemScale, Main.getGamePanel());

            int itemCount = item.getCount();
            BufferedImage amountTextureOnes = Main.getResources().getTexture("ui/numbers/" + itemCount % 10 + ":0");
            ctx.drawImage(amountTextureOnes, itemPos + itemScale / 2, invY + itemScale / 2 + itemYOffset, itemScale / 2, itemScale / 2, Main.getGamePanel());

            if (itemCount / 10 > 0) {
                BufferedImage amountTextureTens = Main.getResources().getTexture("ui/numbers/" + (itemCount / 10) % 10 + ":0");
                ctx.drawImage(amountTextureTens, itemPos, invY + itemScale / 2 + itemYOffset, itemScale / 2, itemScale / 2, Main.getGamePanel());
            }
        }
    }

    public void drawStreak(Graphics2D ctx) {
        int streakWidth = 128;
        int streakHeight = 64;
        int streakX = Main.getGamePanel().getWidth() / 2 - streakWidth / 2;
        int streakY = Main.getGamePanel().getHeight() - 180;

        BufferedImage streakTexture = Main.getResources().getTexture("ui/streak_bar/" + ((3 - (GamePanel.player.streakTimer + 59) / 60)) + ":0");
        ctx.drawImage(streakTexture, streakX, streakY, streakWidth, streakHeight, Main.getGamePanel());

        int streakCount = GamePanel.player.currentStreak;
        BufferedImage amountTextureOnes = Main.getResources().getTexture("ui/numbers/" + streakCount % 10 + ":0");
        ctx.drawImage(amountTextureOnes, streakX + 112, streakY, 20, 20, Main.getGamePanel());

        BufferedImage amountTextureTens = Main.getResources().getTexture("ui/numbers/" + (streakCount / 10) % 10 + ":0");
        ctx.drawImage(amountTextureTens, streakX + 96, streakY, 20, 20, Main.getGamePanel());
    }

    // Placeholder: Counts XP in-game (should be polished and repurposed)
    public void drawXP(Graphics2D ctx) {

        int xpX = 0;
        int xpY = Main.getGamePanel().getHeight() - 25;

        int xpCount = GamePanel.player.xpCount;
        BufferedImage amountTextureOnes = Main.getResources().getTexture("ui/numbers/" + xpCount % 10 + ":0");
        ctx.drawImage(amountTextureOnes, xpX + 48, xpY, 20, 20, Main.getGamePanel());

        BufferedImage amountTextureTens = Main.getResources().getTexture("ui/numbers/" + (xpCount / 10) % 10 + ":0");
        ctx.drawImage(amountTextureTens, xpX + 32, xpY, 20, 20, Main.getGamePanel());

        BufferedImage amountTextureHunds = Main.getResources().getTexture("ui/numbers/" + (xpCount / 100) % 10 + ":0");
        ctx.drawImage(amountTextureHunds, xpX + 16, xpY, 20, 20, Main.getGamePanel());

        BufferedImage amountTextureThous = Main.getResources().getTexture("ui/numbers/" + (xpCount / 1000) % 10 + ":0");
        ctx.drawImage(amountTextureThous, xpX, xpY, 20, 20, Main.getGamePanel());
    }
}
