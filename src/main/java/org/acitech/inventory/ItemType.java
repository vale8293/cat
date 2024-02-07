package org.acitech.inventory;

import org.acitech.Main;

import java.awt.image.BufferedImage;

public enum ItemType {
    WATER(8, "items/water_material", 1, 1, 2),
    STRING(16, "items/string_material", 1, 0, 0),
    BONE(4, "cow", 1,2, 0),
    FEATHER(8, "items/feather_material", 1,0, 0),
    FIRE_TOME_1(1, "spells/fire_tome_lv1", 3, 0, 1),
    POTION_INST_HEALTH(1, "cow", 4, 1, 0),
    POTION_INST_MANA(1, "cow", 4, 1, 0),
    POTION_ATTACK_UP(1, "cow", 4, 1, 0),
    POTION_DEFENSE_UP(1, "cow", 4, 1, 0),
    POTION_SPEED_UP(1, "cow", 4, 1, 0);

    // Use Modifier:
    /*
     * 0: Nothing
     * 1: Consumable
     * 2: Throwable
     */

    // Effect Modifiers
    /*
     * 0: Nothing
     * 1: Fire
     * 2: Water
     * 3: Electric
     * 4: Air
     */

    // How many items can be in one stack
    private final int stackSize;

    // The item's texture
    private final String textureKey;

    // The item's action when used
    private final int useMod;

    // The item's elemental effect with that action
    private final int effectMod;


    // Whether an item will go into
    // inv 1 (Inventory bar, Materials & Misc)
    // inv 2 (Inventory extensions, Backpack?)
    // inv 3 (Shift & Space, Potions or Spells)
    // inv 4 (1 2 3 4, Potions)
    private final int defaultInvNum;

    ItemType(int stackSize, String textureKey, int defaultInvNum, int useMod, int effectMod) {
        this.stackSize = stackSize;
        this.textureKey = textureKey;
        this.defaultInvNum = defaultInvNum;
        this.useMod = useMod;
        this.effectMod = effectMod;
    }

    public int getStackSize() {
        return stackSize;
    }

    public BufferedImage getTexture() {
        return Main.getResources().getTexture(this.textureKey);
    }

    public int getUseMod() {
        return useMod;
    }

    public int getEffectMod() {
        return effectMod;
    }
}