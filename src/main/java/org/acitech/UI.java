package org.acitech;

import org.acitech.inventory.ItemStack;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {

    private static final int heightOfHearts = 64;

    /** Top padding of the stats area */
    private static final int paddingOfStats = 16;

    private static final int heightOfInventory = 64;

    // Draws all UI elements
    public static void draw(Graphics2D ctx) {
        drawHealth(ctx);
        drawMana(ctx);
        drawInventory(ctx);
        drawStreak(ctx);
        drawXP(ctx);
    }

    // Handles the graphics for the health bar
    public static void drawHealth(Graphics2D ctx) {
        double scale = getGuiScale();

        int x = 0;
        int cobalt = GamePanel.player.health; // thanks cobalt :)
        int statsPadding = (int) (paddingOfStats * scale);
        int heartWidth = (int) (72.0 * scale);
        int heartHeight = (int) (heightOfHearts * scale);
        int heartGap = (int) (4.0 * scale);

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
            ctx.drawImage(texture, x + statsPadding, statsPadding, heartWidth, heartHeight, Main.getGamePanel());
            x += heartWidth + heartGap;
        }
    }

    // Handles the mana star(s), functions the same as HP
    public static void drawMana(Graphics2D ctx) {
        double scale = getGuiScale();

        int x = 0;
        int cobalt = GamePanel.player.mana; // thanks cobalt :)
        int statsPadding = (int) (paddingOfStats * scale);
        int heartHeight = (int) (heightOfHearts * scale);
        int manaSize = (int) (16.0 * scale);
        int manaWidth = (int) (52.0 * scale);
        int manaHeight = (int) (52.0 * scale);
        int manaGap = (int) (4.0 * scale);

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
            ctx.drawImage(texture, x + statsPadding, statsPadding + heartHeight + manaSize, manaWidth, manaHeight, Main.getGamePanel());
            x += manaWidth + manaGap;
        }
    }

    // Handles the inventory bar and items (might be rewritten)
    public static void drawInventory(Graphics2D ctx) {
        double scale = getGuiScale();

        int invWidth = (int) (688.0 * scale);
        int invHeight = (int) (heightOfInventory * scale);
        int invX = Main.getGamePanel().getWidth() / 2 - invWidth / 2;
        int invY = Main.getGamePanel().getHeight() - (int) (invHeight * 1.5d);
        int invLeftPadding = (int) (32.0 * scale);

        BufferedImage barTexture = Main.getResources().getTexture("ui/inv_bar_default");
        ctx.drawImage(barTexture, invX, invY, invWidth, invHeight, Main.getGamePanel());

        int itemScale = (int) (invHeight * 0.8);
        int itemYOffset = (int) (invHeight * 0.1);

        for (int slot = 0; slot < GamePanel.player.spellInv.getMaxSlots(); slot++) {
            ItemStack item = GamePanel.player.spellInv.getItem(slot);
            if (item == null) continue;

            BufferedImage itemTexture = item.getType().getTexture();

            int itemPos = invX + invLeftPadding + (int) (itemScale * 1.25) * (slot);
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

    public static void drawStreak(Graphics2D ctx) {
        double scale = getGuiScale();

        int streakWidth = (int) (128.0 * scale);
        int streakHeight = (int) (streakWidth * 0.5);
        int invHeight = (int) (heightOfInventory * scale);
        int invY = Main.getGamePanel().getHeight() - (int) (invHeight * 1.5d);
        int streakX = Main.getGamePanel().getWidth() / 2 - streakWidth / 2;
        int streakY = invY - invHeight - (int) (24.0 * scale);

        BufferedImage streakTexture = Main.getResources().getTexture("ui/streak_bar/" + ((3 - (GamePanel.player.streakTimer + (GamePanel.player.streakTimerMax / 3) - 1) / (GamePanel.player.streakTimerMax / 3))) + ":0");
        ctx.drawImage(streakTexture, streakX, streakY, streakWidth, streakHeight, Main.getGamePanel());

        int streakCount = GamePanel.player.currentStreak;
        BufferedImage amountTextureOnes = Main.getResources().getTexture("ui/numbers/" + streakCount % 10 + ":0");
        ctx.drawImage(amountTextureOnes, streakX + (int) (112.0 * scale), streakY, (int) (20.0 * scale), (int) (20.0 * scale), Main.getGamePanel());

        BufferedImage amountTextureTens = Main.getResources().getTexture("ui/numbers/" + (streakCount / 10) % 10 + ":0");
        ctx.drawImage(amountTextureTens, streakX + (int) (96.0 * scale), streakY, (int) (20.0 * scale), (int) (20.0 * scale), Main.getGamePanel());
    }

    // Placeholder: Counts XP in-game (should be polished and repurposed)
    public static void drawXP(Graphics2D ctx) {
        double scale = getGuiScale();

        int numberScale = (int) (20 * scale);
        int xpX = 0;
        int xpY = Main.getGamePanel().getHeight() - (int) (25.0 * scale);

        int xpCount = GamePanel.player.xpCount;
        BufferedImage amountTextureOnes = Main.getResources().getTexture("ui/numbers/" + xpCount % 10 + ":0");
        ctx.drawImage(amountTextureOnes, xpX + (int) (48 * scale), xpY, numberScale, numberScale, Main.getGamePanel());

        BufferedImage amountTextureTens = Main.getResources().getTexture("ui/numbers/" + (xpCount / 10) % 10 + ":0");
        ctx.drawImage(amountTextureTens, xpX + (int) (32 * scale), xpY, numberScale, numberScale, Main.getGamePanel());

        BufferedImage amountTextureHunds = Main.getResources().getTexture("ui/numbers/" + (xpCount / 100) % 10 + ":0");
        ctx.drawImage(amountTextureHunds, xpX + (int) (16 * scale), xpY, numberScale, numberScale, Main.getGamePanel());

        BufferedImage amountTextureThous = Main.getResources().getTexture("ui/numbers/" + (xpCount / 1000) % 10 + ":0");
        ctx.drawImage(amountTextureThous, xpX, xpY, numberScale, numberScale, Main.getGamePanel());
    }

    public static void drawPauseMenu(Graphics2D ctx) {
        double scale = getGuiScale();

        int menuWidth = (int) (350 * scale);
        int menuHeight = (int) (504 * scale);
        int menuX = Main.getGamePanel().getWidth() / 2 - menuWidth / 2;
        int menuY = Main.getGamePanel().getHeight() / 2 - menuHeight / 2;

        ctx.setColor(new Color(0f, 0f, 0f, 0.3f));
        ctx.fillRect(0, 0, Main.getGamePanel().getWidth(), Main.getGamePanel().getHeight());

        BufferedImage menuTexture = Main.getResources().getTexture("ui/menu");
        ctx.drawImage(menuTexture, menuX, menuY, menuWidth, menuHeight, Main.getGamePanel());
    }

    private static double getGuiScale() {
        return Math.min(Math.min(Main.getGamePanel().getWidth(), Main.getGamePanel().getHeight()), 800.0) / 800.0;
    }
}
