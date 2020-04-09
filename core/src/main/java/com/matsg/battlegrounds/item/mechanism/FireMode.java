package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Firearm;

public interface FireMode extends WeaponMechanism<Firearm>, Cloneable {

    FireMode clone();

    void shoot();
}
