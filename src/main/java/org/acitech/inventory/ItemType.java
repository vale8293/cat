package org.acitech.inventory;

import org.acitech.Main;

import java.awt.image.BufferedImage;

public enum ItemType {
    WATER(16, "items/water_material", 1, true),
    STRING(16, "items/string_material", 1, false),
    BONE(4, "items/water_material", 1, true),
    FEATHER(8, "items/feather_material", 1, true),
    POTION_INST_HEALTH(1, "cow", 2, false),
    POTION_INST_MANA(1, "cow", 2, false),
    POTION_ATTACK_UP(1, "cow", 2, false),
    POTION_DEFENSE_UP(1, "cow", 2, false),
    POTION_SPEED_UP(1, "cow", 2, false);

    // How many items can be in one stack
    private final int stackSize;

    // The item's texture
    private final String textureKey;

    // Whether an item will be thrown when used or not
    private final boolean throwable;

    // Whether an item will go into
    // inv 1 (White Notches, Materials & Misc)
    // inv 2 (Gold Notches, Potions or Spells)
    // inv 3 maybe (Anything, backpack sorta vibe)
    private final int defaultInvNum;

    ItemType(int stackSize, String textureKey, int defaultInvNum, boolean throwable) {
        this.stackSize = stackSize;
        this.textureKey = textureKey;
        this.defaultInvNum = defaultInvNum;
        this.throwable = throwable;
    }

    public int getStackSize() {
        return stackSize;
    }

    public BufferedImage getTexture() {
        return Main.getResources().getTexture(this.textureKey);
    }

    public boolean isThrowable() {
        return throwable;
    }
}