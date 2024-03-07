package org.acitech.inventory;

import java.util.ArrayList;

public class Inventory {

    public ItemStack[] slots;
    public int maxSlots;

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

    // Gets all the slots that are a specific item type
    public ArrayList<Integer> getSlotsWithType(ItemType type) {
        ArrayList<Integer> typeSlots = new ArrayList<>();

        for (int slot = 0; slot < this.maxSlots; slot++) {
            if (slots[slot] != null && slots[slot].getType().equals(type)) {
                typeSlots.add(slot);
            }
        }

        return typeSlots;
    }


    // Dynamically adds an item into the inventory
    public ItemStack addItem(ItemStack item) {
        ItemType itemType = item.getType();
        ArrayList<Integer> slots = this.getSlotsWithType(itemType); // Get all the slots that share the same item type as the given item
        slots.addAll(this.getEmptySlots()); // Add every empty slot that the item can go into

        // Loop through each available slot
        for (int slot : slots) {
            if (item.getCount() == 0) return null; // Stop looping and return if the item is gone

            ItemStack itemSlot = this.slots[slot];

            if (itemSlot != null) { // The current iterated slot shares the same type as the item
                int newCount = itemSlot.getCount() + item.getCount();
                int overflowCount = 0;

                if (newCount > itemType.getStackSize()) {
                    overflowCount = newCount - itemType.getStackSize();
                }

                itemSlot.setCount(newCount);
                item.setCount(overflowCount);

//                if (overflowCount == 0) { // TODO: finish me
//                    return null;
//                }
            } else { // The current iterated slot is an empty one
                this.slots[slot] = item;
                return null;
            }
        }

        // Return the remaining item if the count is greater than 0, otherwise return null
        return item.getCount() > 0 ? item : null;
    }

    public int getMaxSlots() {
        return maxSlots;
    }
}
