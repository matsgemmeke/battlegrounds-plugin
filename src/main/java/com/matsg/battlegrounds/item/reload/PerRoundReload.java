package com.matsg.battlegrounds.item.reload;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.item.ReloadSystem;

public class PerRoundReload implements ReloadSystem {

    public void reloadFirearm(Firearm firearm) {
        firearm.setAmmo(firearm.getAmmo() - 1);
        firearm.setMagazine(firearm.getMagazine() + 1);
        firearm.update();

        if (firearm.getAmmo() > 0 && firearm.getMagazine() < firearm.getMagazineSize()) { // Play the next reload sound prior to the actual reload
            firearm.playReloadSound();
        }
    }
}
