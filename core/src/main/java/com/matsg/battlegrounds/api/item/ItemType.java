package com.matsg.battlegrounds.api.item;

public interface ItemType {

    ItemSlot getDefaultItemSlot();

    String getNameKey();

    boolean hasSubTypes();

    boolean isRemovable();
}
