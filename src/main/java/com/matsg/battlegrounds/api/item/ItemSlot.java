package com.matsg.battlegrounds.api.item;

public enum ItemSlot {

    FIREARM_PRIMARY(0),
    FIREARM_SECONDARY(1),
    EXPLOSIVE(2),
    KNIFE(3),
    BUILDER(4),
    PERK_1(5),
    PERK_2(6),
    PERK_3(7),
    PERK_4(8);

    public static final ItemSlot[] PERK_SLOTS = { PERK_1, PERK_2, PERK_3, PERK_4 };
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