package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.util.BattleRunnable;

public enum FireMode {

    AUTOMATIC(0) {
        public void shoot(Firearm firearm, int rateOfFire, int burstRounds) {
            final int amount = rateOfFire / 5; // Bullets per interact event
            final long period = 4 / amount; // Period between bullets

            new BattleRunnable() {
                int shots = 0;
                public void run() {
                    if (++ shots <= amount && firearm.getMagazine() > 0) {
                        firearm.setMagazine(firearm.getMagazine() - 1);
                        firearm.shootProjectile();
                        firearm.update();

                        firearm.playShotSound();

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
    },
    BURST(1) {
        public void shoot(Firearm firearm, int rateOfFire, int burstRounds) {
            final int amount = rateOfFire / 5; // Bullets per interact event
            final long period = 4 / amount; // Period between bullets

            new BattleRunnable() {
                int shots = 0;
                public void run() {
                    firearm.playShotSound();
                    shots ++;

                    firearm.setMagazine(firearm.getMagazine() - 1);
                    firearm.shootProjectile();
                    firearm.update();

                    if (firearm.getMagazine() <= 0 || shots >= burstRounds) {
                        firearm.cooldown(firearm.getCooldown());
                        cancel();
                    }
                }
            }.runTaskTimer(0, period);
        }
    },
    SEMI_AUTOMATIC(2) {
        public void shoot(Firearm firearm, int rateOfFire, int burstRounds) {
            firearm.playShotSound();

            firearm.cooldown(firearm.getCooldown());
            firearm.setMagazine(firearm.getMagazine() - 1);
            firearm.shootProjectile();
            firearm.update();
        }
    };

    private int id;

    FireMode(int id) {
        this.id = id;
    }

    public static FireMode valueOf(int id) {
        for (FireMode fireMode : values()) {
            if (fireMode.id == id) {
                return fireMode;
            }
        }
        throw new IllegalArgumentException();
    }

    public abstract void shoot(Firearm firearm, int rateOfFire, int burstRounds);
}
