package com.matsg.battlegrounds.api.item;

import org.bukkit.inventory.ItemStack;

public interface Loadout extends Cloneable {

    Loadout clone();

    Equipment getEquipment();

    int getId();

    MeleeWeapon getMeleeWeapon();

    String getName();

    Firearm getPrimary();

    Firearm getSecondary();

    Weapon getWeapon(ItemSlot itemSlot);

    Weapon getWeapon(ItemStack itemStack);

    Weapon getWeaponIgnoreMetadata(ItemStack itemStack);

    Weapon[] getWeapons();

    void removeWeapon(Weapon weapon);

    void setEquipment(Equipment equipment);

    void setMeleeWeapon(MeleeWeapon meleeWeapon);

    void setName(String name);

    void setPrimary(Firearm primary);

    void setSecondary(Firearm secondary);

    void updateInventory();
}
