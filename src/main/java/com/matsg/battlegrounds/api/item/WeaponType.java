package com.matsg.battlegrounds.api.item;

public interface WeaponType {

    ItemSlot getDefaultItemSlot();

    String getName();

    boolean hasSubTypes();

    boolean isRemovable();
}