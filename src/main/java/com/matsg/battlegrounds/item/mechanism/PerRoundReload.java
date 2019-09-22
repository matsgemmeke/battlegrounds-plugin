package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Firearm;

public class PerRoundReload implements ReloadSystem {

    private Firearm firearm;

    public Firearm getWeapon() {
        return firearm;
    }

    public void setWeapon(Firearm firearm) {
        this.firearm = firearm;
    }

    public void reload() {
        firearm.setAmmo(firearm.getAmmo() - 1);
        firearm.setMagazine(firearm.getMagazine() + 1);
        firearm.update();

        if (firearm.getAmmo() > 0 && firearm.getMagazine() < firearm.getMagazineSize()) { // Play the next reload sound prior to the actual reload
            firearm.playReloadSound();
        }
    }
}
