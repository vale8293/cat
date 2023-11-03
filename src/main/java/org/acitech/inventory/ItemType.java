package org.acitech.inventory;

import org.acitech.Main;

import java.awt.image.BufferedImage;

public enum ItemType {
    WATER(8, "items/water_material", true),
    STRING(16, "items/water_material", false),
    BONE(4, "items/water_material", true),
    POTION_INST_HEALTH(1, "cow", false),
    POTION_INST_MANA(1, "cow", false),
    POTION_ATTACK_UP(1, "cow", false),
    POTION_DEFENSE_UP(1, "cow", false),
    POTION_SPEED_UP(1, "cow", false);


    private final int stackSize;
    private final String textureKey;
    private final boolean throwable;

    ItemType(int stackSize, String textureKey, boolean throwable) {
        this.stackSize = stackSize;
        this.textureKey = textureKey;
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