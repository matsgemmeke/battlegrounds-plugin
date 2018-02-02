package com.matsg.battlegrounds.api.item;

public interface Weapon extends Item {

    Weapon clone();

    WeaponAttribute getAttribute(String attribute);

    Iterable<WeaponAttribute> getAttributes();

    String getDescription();

    WeaponType getWeaponType();

    void refillAmmo();

    void remove();
}