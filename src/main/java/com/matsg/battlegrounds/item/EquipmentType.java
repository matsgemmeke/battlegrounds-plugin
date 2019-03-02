package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.ItemType;
import com.matsg.battlegrounds.util.MessageHelper;

public enum EquipmentType implements ItemType {

    LETHAL("item-type-lethal"),
    TACTICAL("item-type-tactical");

    private MessageHelper messageHelper;
    private String name;

    EquipmentType(String path) {
        this.messageHelper = new MessageHelper();
        this.name = messageHelper.create(path);
    }

    public ItemSlot getDefaultItemSlot() {
        return ItemSlot.EQUIPMENT;
    }

    public String getName() {
        return name;
    }

    public boolean hasSubTypes() {
        return true;
    }

    public boolean isRemovable() {
        return true;
    }
}