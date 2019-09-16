package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Firearm;

public interface FireMode {

    void shoot(Firearm firearm, int fireRate, int burst);
}
