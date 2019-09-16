package com.matsg.battlegrounds.item.firemode;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.item.FireMode;
import com.matsg.battlegrounds.util.BattleRunnable;

public class BurstMode implements FireMode {

    public void shoot(Firearm firearm, int rateOfFire, int burstRounds) {
        final int amount = rateOfFire / 5; // Bullets per interact event
        final long period = 4 / amount; // Period between bullets

        new BattleRunnable() {
            int shots = 0;

            public void run() {
                firearm.playShotSound();
                firearm.shootProjectile();
                firearm.setMagazine(firearm.getMagazine() - 1);
                firearm.update();

                shots++;

                if (firearm.getMagazine() <= 0 || shots >= burstRounds) {
                    firearm.cooldown(firearm.getCooldown());
                    cancel();
                }
            }
        }.runTaskTimer(0, period);
    }
}
