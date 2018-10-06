package com.matsg.battlegrounds.api.item;

public interface ItemType {

    ItemSlot getDefaultItemSlot();

    String getName();

    boolean hasSubTypes();

    boolean isRemovable();
}