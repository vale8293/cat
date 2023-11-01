package org.acitech.inventory;

import org.acitech.Main;

import java.awt.image.BufferedImage;

public enum ItemType {
    WATER(8, "items/water_material"),
    STRING(16, "items/water_material"),
    BONE(4, "items/water_material");

    private final int stackSize;
    private final String textureKey;

    ItemType(int stackSize, String textureKey) {
        this.stackSize = stackSize;
        this.textureKey = textureKey;
    }

    public int getStackSize() {
        return stackSize;
    }

    public BufferedImage getTexture() {
        return Main.getResources().getTexture(this.textureKey);
    }
}