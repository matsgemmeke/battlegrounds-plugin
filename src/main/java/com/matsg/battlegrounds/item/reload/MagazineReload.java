package com.matsg.battlegrounds.item.reload;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.item.ReloadSystem;

public class MagazineReload implements ReloadSystem {

    public void reloadFirearm(Firearm firearm) {
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
