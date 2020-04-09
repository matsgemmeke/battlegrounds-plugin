package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Firearm;

public class SemiAutomatic implements FireMode {

    private Firearm firearm;

    public SemiAutomatic() { }

    private SemiAutomatic(Firearm firearm) {
        this.firearm = firearm;
    }

    public Firearm getWeapon() {
        return firearm;
    }

    public void setWeapon(Firearm firearm) {
        this.firearm = firearm;
    }

    public FireMode clone() {
        return new SemiAutomatic(firearm);
    }

    public void shoot() {
        firearm.playShotSound();
        firearm.shootProjectile();
        firearm.cooldown(firearm.getCooldown());
        firearm.setMagazine(firearm.getMagazine() - 1);
        firearm.update();
    }
}
