package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Firearm;

public interface ReloadSystem extends WeaponMechanism<Firearm>, Cloneable {

    ReloadSystem clone();

    void reload();
}
