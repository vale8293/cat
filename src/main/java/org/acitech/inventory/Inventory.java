package org.acitech.inventory;

import java.util.ArrayList;

public class Inventory {

    private ItemStack[] slots;
    private final int maxSlots;

    public Inventory(int maxSlots) {
        this.maxSlots = maxSlots;
        this.slots = new ItemStack[maxSlots];
    }

    public boolean setItem(int slot, ItemStack itemStack) {
        if (slot > maxSlots) return false;

        slots[slot] = itemStack;
        return true;
    }

    public ItemStack getItem(int slot) {
        if (slot > maxSlots) return null;

        return slots[slot];
    }

    public void emptySlot(int slot) {
        if (slot > maxSlots) return;

        slots[slot] = null;
    }

    public ArrayList<Integer> getEmptySlots() {
        ArrayList<Integer> emptySlots = new ArrayList<>();

        for (int slot = 0; slot < this.maxSlots; slot++) {
            if (slots[slot] == null) emptySlots.add(slot);
        }

        return emptySlots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }
}
