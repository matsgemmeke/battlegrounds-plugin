package com.matsg.battlegrounds.api.item;

import org.bukkit.inventory.ItemStack;

public interface Loadout {

    Equipment getEquipment();

    int getId();

    Knife getKnife();

    String getName();

    FireArm getPrimary();

    FireArm getSecondary();

    Weapon getWeapon(ItemSlot itemSlot);

    Weapon getWeapon(ItemStack itemStack);

    Weapon getWeaponIgnoreMetadata(ItemStack itemStack);

    Weapon[] getWeapons();

    void setEquipment(Equipment equipment);

    void setKnife(Knife knife);

    void setName(String name);

    void setPrimary(FireArm primary);

    void setSecondary(FireArm secondary);

    void updateInventory();
}