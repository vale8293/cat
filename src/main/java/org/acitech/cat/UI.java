package org.acitech.cat;

import org.acitech.cat.assets.AssetLoader;
import org.acitech.cat.inputs.Controls;
import org.acitech.cat.inventory.ItemStack;
import org.acitech.cat.utils.Caboodle;
import org.acitech.cat.utils.EventHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {

    private static final int heightOfHearts = 64;
    /** Top padding of the stats area */
    private static final int paddingOfStats = 16;
    private static final int heightOfInventory = 64;
    private static int pauseMenuSelection = 0;
    private static final Color pauseMenuSelectColor = new Color(0xff3399);
    private static final Color pauseMenuDefaultColor = new Color(0xffffff);
    private static float darknessTransition = 0;

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
        int cobalt = GamePanel.getPlayer().health; // thanks cobalt :)
        int statsPadding = (int) (paddingOfStats * scale);
        int heartWidth = (int) (72.0 * scale);
        int heartHeight = (int) (heightOfHearts * scale);
        int heartGap = (int) (4.0 * scale);

        for (int i = 0; i < GamePanel.getPlayer().maxHealth / 2; i++) {
            int spriteX;

            if (cobalt >= 2) { // Draws a full heart for every 2 health you have
                spriteX = 0; // full
                cobalt -= 2;
            } else if (cobalt == 1) { // Draws a half heart for every 1 health aside from the 2s (should be 1 or 0)
                spriteX = 1; // half
                cobalt--;
            } else { // Draws empty hearts for all health remaining in the max that isn't in the current
                spriteX = 2; // empty
            }

            // Draws hearts rightward with small gaps between
            BufferedImage texture = AssetLoader.UI_HEARTS.getSprite(spriteX, 0);
            ctx.drawImage(texture, x + statsPadding, statsPadding, heartWidth, heartHeight, Main.getGamePanel());
            x += heartWidth + heartGap;
        }
    }

    // Handles the mana star(s), functions the same as HP
    public static void drawMana(Graphics2D ctx) {
        double scale = getGuiScale();

        int x = 0;
        int cobalt = GamePanel.getPlayer().mana; // thanks cobalt :)
        int statsPadding = (int) (paddingOfStats * scale);
        int heartHeight = (int) (heightOfHearts * scale);
        int manaSize = (int) (16.0 * scale);
        int manaWidth = (int) (52.0 * scale);
        int manaHeight = (int) (52.0 * scale);
        int manaGap = (int) (4.0 * scale);

        for (int i = 0; i < GamePanel.getPlayer().maxMana / 6; i++) {
            int spriteX;

            if (cobalt >= 6) {
                spriteX = 0; // full pink
                cobalt -= 6;
            } else if (cobalt == 5) {
                spriteX = 1; // full blue
                cobalt -= 5;
            } else if (cobalt == 4) {
                spriteX = 2; // blue - 1
                cobalt -= 4;
            } else if (cobalt == 3) {
                spriteX = 3; // blue - 2
                cobalt -= 3;
            } else if (cobalt == 2) {
                spriteX = 4; // blue - 3
                cobalt -= 2;
            } else if (cobalt == 1) {
                spriteX = 5; // blue - 4
                cobalt--;
            } else {
                spriteX = 6; // empty
            }

            // Draws stars rightward with small gaps between
            BufferedImage texture = AssetLoader.UI_MANA.getSprite(spriteX, 0);
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

        BufferedImage barTexture = AssetLoader.UI_INV_BAR_DEFAULT;
        ctx.drawImage(barTexture, invX, invY, invWidth, invHeight, Main.getGamePanel());

        int itemScale = (int) (invHeight * 0.8);
        int itemYOffset = (int) (invHeight * 0.1);
        int textSize = (int) (20.0 * scale);

        for (int slot = 0; slot < GamePanel.getPlayer().spellInv.getMaxSlots(); slot++) {
            ItemStack item = GamePanel.getPlayer().spellInv.getItem(slot);
            if (item == null) continue;

            BufferedImage itemTexture = item.getType().getTexture();

            int itemPos = invX + invLeftPadding + (int) (itemScale * 1.25d) * slot;
            ctx.drawImage(itemTexture, itemPos, invY + itemYOffset, itemScale, itemScale, Main.getGamePanel());

            int itemCount = item.getCount();
            drawCenteredText(ctx, itemPos + itemScale, invY + (int) (itemScale * 0.8d) + itemYOffset, textSize, String.valueOf(itemCount), Color.WHITE);
        }

        for (int slot = 0; slot < GamePanel.getPlayer().defaultInv.getMaxSlots(); slot++) {
            ItemStack item = GamePanel.getPlayer().defaultInv.getItem(slot);
            if (item == null) continue;

            BufferedImage itemTexture = item.getType().getTexture();

            int itemPos = invX + invLeftPadding + (int) (itemScale * 1.25d) * (slot + GamePanel.getPlayer().spellInv.getMaxSlots());
            ctx.drawImage(itemTexture, itemPos, invY + itemYOffset, itemScale, itemScale, Main.getGamePanel());

            int itemCount = item.getCount();
            drawCenteredText(ctx, itemPos + itemScale, invY + (int) (itemScale * 0.8d) + itemYOffset, textSize, String.valueOf(itemCount), Color.WHITE);
        }

        BufferedImage cursorTexture = AssetLoader.UI_CURSOR;
        ctx.drawImage(cursorTexture, (int) (invX + invLeftPadding + itemScale * 1.25d * GamePanel.getPlayer().selectedSlot), invY + itemScale + itemYOffset, itemScale, (int) ((5d / 22d) * itemScale), Main.getGamePanel());
    }

    public static void drawStreak(Graphics2D ctx) {
        double scale = getGuiScale();

        int streakWidth = (int) (128.0 * scale);
        int streakHeight = (int) (40.0 * scale);
        int invHeight = (int) (heightOfInventory * scale);
        int invY = Main.getGamePanel().getHeight() - (int) (invHeight * 1.5d);
        int streakX = Main.getGamePanel().getWidth() / 2 - streakWidth / 2;
        int streakY = invY - invHeight + (int) (10.0 * scale);
        int textSize = (int) (20.0 * scale);

        BufferedImage streakTexture = AssetLoader.UI_STREAK_BAR.getSprite(((3 - (GamePanel.getPlayer().streakTimer + (GamePanel.getPlayer().streakTimerMax / 3) - 1) / (GamePanel.getPlayer().streakTimerMax / 3))), 0);
        ctx.drawImage(streakTexture, streakX, streakY, streakWidth, streakHeight, Main.getGamePanel());

        drawCenteredText(ctx, streakX + streakWidth / 2, streakY - textSize, textSize, "Streak " + GamePanel.getPlayer().currentStreak, Color.WHITE);
    }

    // Placeholder: Counts XP in-game (should be polished and repurposed)
    public static void drawXP(Graphics2D ctx) {
        int xpCount = GamePanel.getPlayer().xpCount;
        double scale = getGuiScale();
        int size = (int) (20 * scale);

        drawText(ctx, size, Main.getGamePanel().getHeight() - size * 2, size, String.valueOf(xpCount), Color.WHITE);
    }

    public static void drawPauseMenu(Graphics2D ctx) {
        double scale = getGuiScale();

        int menuWidth = (int) (350 * scale);
        int menuHeight = (int) (504 * scale);
        int menuX = Main.getGamePanel().getWidth() / 2 - menuWidth / 2;
        int menuY = Main.getGamePanel().getHeight() / 2 - menuHeight / 2;

        ctx.setColor(new Color(0f, 0f, 0f, 0.3f));
        ctx.fillRect(0, 0, Main.getGamePanel().getWidth(), Main.getGamePanel().getHeight());

        BufferedImage menuTexture = AssetLoader.UI_MENU;
        ctx.drawImage(menuTexture, menuX, menuY, menuWidth, menuHeight, Main.getGamePanel());

        drawCenteredText(ctx, menuX + menuWidth / 2, menuY + menuHeight / 2 - (int) (80.0f * scale), (int) (20.0f * scale), "Resume", pauseMenuSelection == 0 ? pauseMenuSelectColor : pauseMenuDefaultColor);
        drawCenteredText(ctx, menuX + menuWidth / 2, menuY + menuHeight / 2 - (int) (40.0f * scale), (int) (20.0f * scale), "Stay Paused", pauseMenuSelection == 1 ? pauseMenuSelectColor : pauseMenuDefaultColor);
        drawCenteredText(ctx, menuX + menuWidth / 2, menuY + menuHeight / 2 /*    Random  Spacer    */, (int) (20.0f * scale), "Be Paused", pauseMenuSelection == 2 ? pauseMenuSelectColor : pauseMenuDefaultColor);
        drawCenteredText(ctx, menuX + menuWidth / 2, menuY + menuHeight / 2 + (int) (40.0f * scale), (int) (20.0f * scale), "Keep Pausing", pauseMenuSelection == 3 ? pauseMenuSelectColor : pauseMenuDefaultColor);
        drawCenteredText(ctx, menuX + menuWidth / 2, menuY + menuHeight / 2 + (int) (80.0f * scale), (int) (20.0f * scale), "Exit Game", pauseMenuSelection == 4 ? pauseMenuSelectColor : pauseMenuDefaultColor);
    }

    public static void drawTopText(Graphics2D ctx, String text) {
        UI.drawCenteredText(ctx, Main.getGamePanel().getWidth() / 2, Main.getGamePanel().getHeight() / 4, (int) (20.0f * getGuiScale()), text, Color.white);
    }

    public static void drawDarknessTransition(Graphics2D ctx) {
        if (darknessTransition == 0) return;

        ctx.setColor(new Color(0.0f, 0.0f, 0.0f, darknessTransition));
        ctx.fillRect(0, 0, Main.getGamePanel().getWidth(), Main.getGamePanel().getHeight());

        darknessTransition = Math.max(0.0f, darknessTransition - 0.05f);
    }

    public static void restartDarknessTransition() {
        darknessTransition = 1.0f;
    }

    public static void drawText(Graphics2D ctx, int x, int y, int size, String text, Color color) {
        BufferedImage textImg = tintImage(generateText(size, text), color);

        ctx.drawImage(textImg, x, y, textImg.getWidth(), textImg.getHeight(), Main.getGamePanel());
    }

    public static void drawCenteredText(Graphics2D ctx, int x, int y, int size, String text, Color color) {
        double sub = size / 5.0d; // Unit for a sub pixel of a letter
        drawText(ctx, (int) (x - (size + sub) / 2.0d * text.length() + (size + sub) / 4.0d), (int) (y - size / 2.0d), size, text, color);
    }

    private static BufferedImage generateText(int size, String text) {
        String matText = text.toUpperCase();
        double sub = size / 5.0d; // Unit for a sub pixel of a letter
        BufferedImage textImg = new BufferedImage(size * matText.length() + (int) (sub * matText.length() - 1), size, BufferedImage.TRANSLUCENT);
        Graphics2D ctx = (Graphics2D) textImg.getGraphics();

        for (int i = 0; i < matText.length(); i++) {
            char letter = matText.charAt(i);

            BufferedImage letterTexture = AssetLoader.UI_FONT.getSprite(letter, 0);
            ctx.drawImage(letterTexture, (int) (i * size + i * sub), 0, size, size, Main.getGamePanel());
        }

        return textImg;
    }

    public static BufferedImage tintImage(BufferedImage sprite, float red, float green, float blue, float alpha) {
        BufferedImage maskImg = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TRANSLUCENT);
        int rgb = new Color(red, green, blue, alpha).getRGB();

        for (int i = 0; i < sprite.getWidth(); i++) {
            for (int j = 0; j < sprite.getHeight(); j++) {
                int color = sprite.getRGB(i, j);

                if (color != 0) {
                    maskImg.setRGB(i, j, rgb);
                }
            }
        }

        return maskImg;
    }

    public static BufferedImage tintImage(BufferedImage sprite, Color color) {
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;
        float a = color.getAlpha() / 255.0f;

        return tintImage(sprite, r, g, b, a);
    }

    private static double getGuiScale() {
        return Math.min(Math.min(Main.getGamePanel().getWidth(), Main.getGamePanel().getHeight()), 800.0) / 800.0;
    }

    @EventHandler(eventName = "keyDown")
    public static void onPauseMenuUpdate() {
        if (Controls.isKeyPressed(Controls.downKey)) {
            pauseMenuSelection = (int) Caboodle.wrap(pauseMenuSelection + 1, 0, 5);
        } else if (Controls.isKeyPressed(Controls.upKey)) {
            pauseMenuSelection = (int) Caboodle.wrap(pauseMenuSelection - 1, 0, 5);
        } else if (Controls.isKeyPressed(Controls.selectKey)) {
            switch (pauseMenuSelection) {
                case 0 -> Main.getGamePanel().setPaused(false);
                case 4 -> System.exit(0);
            }
        }
    }

}
