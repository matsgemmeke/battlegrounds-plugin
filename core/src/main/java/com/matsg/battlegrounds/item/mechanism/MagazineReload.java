package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.api.item.Firearm;

public class MagazineReload implements ReloadSystem {

    private Firearm firearm;

    public MagazineReload() { }

    private MagazineReload(Firearm firearm) {
        this.firearm = firearm;
    }

    public Firearm getWeapon() {
        return firearm;
    }

    public void setWeapon(Firearm firearm) {
        this.firearm = firearm;
    }

    public ReloadSystem clone() {
        return new MagazineReload(firearm);
    }

    public void reload() {
        int magazineSpace = firearm.getMagazineSize() - firearm.getMagazine();

        if (magazineSpace > firearm.getAmmo()) { // In case the magazine cannot be filled completely use the remaining ammo
            firearm.setMagazine(firearm.getMagazine() + firearm.getAmmo());
            firearm.setAmmo(0);
        } else {
            firearm.setAmmo(firearm.getAmmo() - magazineSpace);
            firearm.setMagazine(firearm.getMagazineSize());
        }

        firearm.setReloading(false);
        firearm.update();
    }
}
