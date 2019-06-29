package com.matsg.battlegrounds.api.item;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface Loadout extends Cloneable {

    Equipment getEquipment();

    void setEquipment(Equipment equipment);

    int getLoadoutNr();

    MeleeWeapon getMeleeWeapon();

    void setMeleeWeapon(MeleeWeapon meleeWeapon);

    String getName();

    void setName(String name);

    Firearm getPrimary();

    void setPrimary(Firearm primary);

    Firearm getSecondary();

    void setSecondary(Firearm secondary);

    Loadout clone();

    Map<String, String> convertToMap();

    Weapon getWeapon(ItemSlot itemSlot);

    Weapon getWeapon(ItemStack itemStack);

    Weapon getWeapon(String name);

    Weapon getWeaponIgnoreMetadata(ItemStack itemStack);

    Weapon[] getWeapons();

    void removeWeapon(Weapon weapon);

    void updateInventory();
}
