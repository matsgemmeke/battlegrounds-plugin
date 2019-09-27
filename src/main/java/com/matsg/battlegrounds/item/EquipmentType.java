package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.ItemType;

public enum EquipmentType implements ItemType {

    LETHAL("item-type-lethal"),
    TACTICAL("item-type-tactical");

    private String nameKey;

    EquipmentType(String nameKey) {
        this.nameKey = nameKey;
    }

    public ItemSlot getDefaultItemSlot() {
        return ItemSlot.EQUIPMENT;
    }

    public String getNameKey() {
        return nameKey;
    }

    public boolean hasSubTypes() {
        return true;
    }

    public boolean isRemovable() {
        return true;
    }
}
