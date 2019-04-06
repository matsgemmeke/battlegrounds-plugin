package com.matsg.battlegrounds.api.item;

public interface Weapon extends Item, Property {

    Weapon clone();

    String getDescription();

    ItemSlot getItemSlot();

    ItemType getType();

    void resetState();

    void setItemSlot(ItemSlot itemSlot);
}
