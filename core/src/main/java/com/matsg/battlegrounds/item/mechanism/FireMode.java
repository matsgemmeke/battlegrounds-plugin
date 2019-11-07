package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.item.WeaponMechanism;

public interface FireMode extends WeaponMechanism<Firearm> {

    void shoot();
}
