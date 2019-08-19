package com.matsg.battlegrounds.api.item;

public interface Weapon extends Item, PlayerProperty, TransactionItem {

    WeaponContext getContext();

    void setContext(WeaponContext context);

    String getDescription();

    ItemSlot getItemSlot();

    void setItemSlot(ItemSlot itemSlot);

    ItemType getType();

    Weapon clone();

    void resetState();
}
