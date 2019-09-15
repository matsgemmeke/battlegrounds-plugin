package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.ItemType;

public class MeleeWeaponItemType implements ItemType {

    private ItemSlot defaultItemSlot;
    private String name;

    public MeleeWeaponItemType(String name) {
        this.name = name;
        this.defaultItemSlot = ItemSlot.MELEE_WEAPON;
    }

    public ItemSlot getDefaultItemSlot() {
        return defaultItemSlot;
    }

    public String getName() {
        return name;
    }

    public boolean hasSubTypes() {
        return false;
    }

    public boolean isRemovable() {
        return true;
    }
}
