package org.acitech.inventory;

import org.acitech.Main;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ItemType {
    WATER(8, "items/water_material", 1, "consumable", "aqua", null),
    STRING(16, "items/string_material", 1, "none", "none", null),
    BONE(4, "cow", 1,"throwable", "none", null),
    FEATHER(8, "items/feather_material", 1,"none", "none", null),
    FIRE_TOME_1(1, "spells/fire_tome_lv1", 3, "none", "fire", null),
    AQUA_TOME_1(1, "spells/aqua_tome_lv1", 3, "none", "aqua", null),
    HEALTH_POTION(5, "items/health_potion", 4, "consumable", "none", null),
    MANA_POTION(5, "items/mana_potion", 4, "consumable", "none", null),
    ATTACK_POTION(5, "items/attack_potion", 4, "consumable", "none", 60),
    DEFENSE_POTION(5, "cow", 4, "consumable", "none", 30), // lol
    SPEED_POTION(5, "items/speed_potion", 4, "consumable", "none", 120);

    /*
     * Predefined groups of similar item types
     */
    public static Set<ItemType> getSpellTypes() {
        return new HashSet<>(Arrays.asList(FIRE_TOME_1, AQUA_TOME_1));
    }

    public static Set<ItemType> getPotionTypes() {
        return new HashSet<>(Arrays.asList(HEALTH_POTION, MANA_POTION, ATTACK_POTION, DEFENSE_POTION, SPEED_POTION));
    }

    /** How many items can be in one stack */
    private final int stackSize;

    /** The item's texture */
    private final String textureKey;

    /** The item's action when used */
    private final String useMod;

    /** The item's elemental effect with that action */
    private final String effectMod;

    /** A potion's duration */
    private final int duration;

    // Whether an item will go into
    // inv 1 (Inventory bar, Materials & Misc)
    // inv 2 (Inventory extensions, Backpack?)
    // inv 3 (Shift & Space, Spells)
    // inv 4 (1 2 3 4, Potions)
    private final int defaultInvNum;

    ItemType(int stackSize, String textureKey, int defaultInvNum, String useMod, String effectMod, Integer duration) {
        this.stackSize = stackSize;
        this.textureKey = textureKey;
        this.defaultInvNum = defaultInvNum;
        this.useMod = useMod;
        this.effectMod = effectMod;
        this.duration = duration;
    }

    public int getStackSize() {
        return stackSize;
    }

    public BufferedImage getTexture() {
        return Main.getResources().getTexture(this.textureKey);
    }

    public String getUseMod() {
        return useMod;
    }

    public String getEffectMod() {
        return effectMod;
    }
}