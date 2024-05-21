package org.acitech.inventory;

import org.acitech.assets.AssetLoader;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ItemType {
    WATER(8, AssetLoader.ITEMS_WATER_MATERIAL, 1, "consumable", "aqua", null),
    STRING(16, AssetLoader.ITEMS_STRING_MATERIAL, 1, "none", "none", null),
    BONE(4, AssetLoader.COW, 1,"none", "none", null),
    FEATHER(8, AssetLoader.ITEMS_FEATHER_MATERIAL, 1,"none", "none", null),
    FIRE_TOME_1(1, AssetLoader.SPELLS_FIRE_TOME_LV1, 3, "none", "fire", null),
    AQUA_TOME_1(1, AssetLoader.SPELLS_AQUA_TOME_LV1, 3, "none", "aqua", null),
    HEALTH_POTION(3, AssetLoader.ITEMS_HEALTH_POTION, 1, "consumable", "none", null),
    MANA_POTION(3, AssetLoader.ITEMS_MANA_POTION, 1, "consumable", "none", null),
    ATTACK_POTION(3, AssetLoader.ITEMS_ATTACK_POTION, 1, "consumable", "none", 1800),
    SPEED_POTION(3, AssetLoader.ITEMS_SPEED_POTION, 1, "consumable", "none", 3600);

    /*
     * Predefined groups of similar item types
     */
    public static Set<ItemType> getSpellTypes() {
        return new HashSet<>(Arrays.asList(FIRE_TOME_1, AQUA_TOME_1));
    }

    public static Set<ItemType> getPotionTypes() {
        return new HashSet<>(Arrays.asList(HEALTH_POTION, MANA_POTION, ATTACK_POTION, SPEED_POTION));
    }

    /** How many items can be in one stack */
    private final int stackSize;

    /** The item's texture */
    private final BufferedImage texture;

    /** The item's action when used */
    private final String useMod;

    /** The item's elemental effect with that action */
    private final String effectMod;

    /** A potion's duration */
    private final Integer duration;

    // Whether an item will go into
    // inv 1 (Inventory bar, Materials & Misc)
    // inv 2 (Inventory extensions, Backpack?)
    // inv 3 (Shift & Space, Spells)
    // inv 4 (1 2 3 4, Potions)
    private final int defaultInvNum;

    ItemType(int stackSize, BufferedImage texture, int defaultInvNum, String useMod, String effectMod, Integer duration) {
        this.stackSize = stackSize;
        this.texture = texture;
        this.defaultInvNum = defaultInvNum;
        this.useMod = useMod;
        this.effectMod = effectMod;
        this.duration = duration;
    }

    public int getStackSize() {
        return stackSize;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public String getUseMod() {
        return useMod;
    }

    public String getEffectMod() {
        return effectMod;
    }
}