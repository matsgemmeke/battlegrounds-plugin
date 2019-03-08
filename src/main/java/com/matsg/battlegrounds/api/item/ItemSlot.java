package com.matsg.battlegrounds.api.item;

public enum ItemSlot {

    FIREARM_PRIMARY(0),
    FIREARM_SECONDARY(1),
    MELEE_WEAPON(2),
    EQUIPMENT(3),
    MISCELLANEOUS(8);

    public static ItemSlot[] WEAPON_SLOTS = { FIREARM_PRIMARY, FIREARM_SECONDARY, MELEE_WEAPON, EQUIPMENT };
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