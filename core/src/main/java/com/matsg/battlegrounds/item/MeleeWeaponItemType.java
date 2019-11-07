package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.ItemType;

public class MeleeWeaponItemType implements ItemType {

    private ItemSlot defaultItemSlot;
    private String nameKey;

    public MeleeWeaponItemType(String nameKey) {
        this.nameKey = nameKey;
        this.defaultItemSlot = ItemSlot.MELEE_WEAPON;
    }

    public ItemSlot getDefaultItemSlot() {
        return defaultItemSlot;
    }

    public String getNameKey() {
        return nameKey;
    }

    public boolean hasSubTypes() {
        return false;
    }

    public boolean isRemovable() {
        return true;
    }
}
