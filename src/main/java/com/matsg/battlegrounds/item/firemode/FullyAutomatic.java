package com.matsg.battlegrounds.item.firemode;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.item.FireMode;
import com.matsg.battlegrounds.util.BattleRunnable;

public class FullyAutomatic implements FireMode {

    public void shoot(Firearm firearm, int fireRate, int burst) {
        final int amount = fireRate / 5; // Bullets per interaction
        final long period = 4 / amount; // Amount of ticks between bullets

        new BattleRunnable() {
            int shots = 0;

            public void run() {
                if (++shots <= amount && firearm.getMagazine() > 0) {
                    firearm.playShotSound();
                    firearm.shootProjectile();
                    firearm.setMagazine(firearm.getMagazine() - 1);
                    firearm.update();

                    if (shots == amount || firearm.getMagazine() <= 0) {
                        firearm.setShooting(false); // Preventing quicker shooting when fast right clicking
                    }
                }

                if (shots > 4) { // Cancel after the runnable is relevant to prevent bullets shooting out of sync
                    firearm.setShooting(false); // Make sure the gun can shoot again
                    cancel();
                }
            }
        }.runTaskTimer(0, period);
    }
}
