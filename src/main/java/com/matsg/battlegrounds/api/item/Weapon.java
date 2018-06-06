package com.matsg.battlegrounds.api.item;

public interface Weapon extends Item {

    Weapon clone();

    String getDescription();

    ItemSlot getItemSlot();

    WeaponType getType();

    void refillAmmo();

    void remove();

    void setItemSlot(ItemSlot itemSlot);
}