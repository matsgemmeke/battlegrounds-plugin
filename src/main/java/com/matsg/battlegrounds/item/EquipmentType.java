package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.WeaponType;

public enum EquipmentType implements WeaponType {

    LETHAL("item-type-lethal"),
    TACTICAL("item-type-tactical");

    private String name;

    EquipmentType(String path) {
        this.name = BattlegroundsPlugin.getPlugin().getTranslator().getTranslation(path);
    }

    public ItemSlot getDefaultItemSlot() {
        return ItemSlot.EQUIPMENT;
    }

    public String getName() {
        return name;
    }
}