package com.matsg.battlegrounds.api.item;

public interface Weapon extends Item, Property {

    String getDescription();

    ItemSlot getItemSlot();

    void setItemSlot(ItemSlot itemSlot);

    ItemType getType();

    Weapon clone();

    void resetState();
}
