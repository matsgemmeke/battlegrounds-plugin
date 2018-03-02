package com.matsg.battlegrounds.api.item;

import org.bukkit.inventory.ItemStack;

public interface LoadoutClass {

    Knife getKnife();

    Lethal getLethal();

    String getName();

    FireArm getPrimary();

    FireArm getSecondary();

    Tactical getTactical();

    Weapon getWeapon(ItemSlot itemSlot);

    Weapon getWeapon(ItemStack itemStack);

    Weapon getWeaponIgnoreMetadata(ItemStack itemStack);

    Weapon[] getWeapons();

    void setKnife(Knife knife);

    void setLethal(Lethal lethal);

    void setPrimary(FireArm primary);

    void setSecondary(FireArm secondary);

    void setTactical(Tactical tactical);
}