package org.acitech.inventory;

public class ItemStack {

    private final ItemType type;
    private int count;

    public ItemStack(ItemType type, int amount) {
        this.type = type;
        this.count = Math.min(this.type.getStackSize(), amount);
    }
    public ItemStack(ItemType type) {
        this.type = type;
        this.count = 1;
    }

    public ItemType getType() {
        return type;
    }

    public void setCount(int amount) {
        this.count = Math.min(this.type.getStackSize(), amount);
    }
    public int getCount() {
        return count;
    }
}
