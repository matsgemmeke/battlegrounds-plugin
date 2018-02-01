package com.matsg.battlegrounds.api.item;

public interface Weapon extends Item {

    Weapon clone();

    WeaponType getType();

    void refillAmmo();

    void remove();
}