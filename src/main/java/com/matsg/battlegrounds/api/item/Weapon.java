package com.matsg.battlegrounds.api.item;

public interface Weapon extends Item {

    Weapon clone();

    String getDescription();

    WeaponType getType();

    void refillAmmo();

    void remove();
}