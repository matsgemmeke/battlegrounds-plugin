package com.matsg.battlegrounds.item.firemode;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.item.FireMode;

public class SemiAutomatic implements FireMode {

    public void shoot(Firearm firearm, int fireRate, int burst) {
        firearm.playShotSound();
        firearm.shootProjectile();
        firearm.cooldown(firearm.getCooldown());
        firearm.setMagazine(firearm.getMagazine() - 1);
        firearm.update();
    }
}
