package com.matsg.battlegrounds.api.item;

public enum ItemSlot {

    FIREARM_PRIMARY(0),
    FIREARM_SECONDARY(1),
    KNIFE(2),
    EQUIPMENT(3),
    MISCELLANEOUS(8);

    private int slot;

    ItemSlot(int slot) {
        this.slot = slot;
    }

    public static ItemSlot fromSlot(int slot) {
        for (ItemSlot itemSlot : values()) {
            if (itemSlot.getSlot() == slot) {
                return itemSlot;
            }
        }
        return null;
    }

    public int getSlot() {
        return slot;
    }
}