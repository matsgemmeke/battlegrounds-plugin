package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Weapon;

public interface WeaponMechanism<T extends Weapon> {

    T getWeapon();

    void setWeapon(T weapon);
}
