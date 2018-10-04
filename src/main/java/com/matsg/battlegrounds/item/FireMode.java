package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.util.BattleRunnable;

public enum FireMode {

    AUTOMATIC(0) {
        public void shoot(FireArm fireArm, int rateOfFire, int burstRounds) {
            final int amount = rateOfFire / 5; // Bullets per interact event
            final long period = 4 / amount; // Period between bullets

            new BattleRunnable() {
                int shots = 0;
                public void run() {
                    if (++ shots <= amount && fireArm.getMagazine() > 0) {
                        fireArm.setMagazine(fireArm.getMagazine() - 1);
                        fireArm.shootProjectile();
                        fireArm.update();

                        fireArm.playShotSound();

                        if (shots == amount || fireArm.getMagazine() <= 0) {
                            fireArm.setShooting(false); // Preventing quicker shooting when fast right clicking
                        }
                    }
                    if (shots > 4) { // Cancel after the runnable is relevant to prevent bullets shooting out of sync
                        fireArm.setShooting(false); // Make sure the gun can shoot again
                        cancel();
                    }
                }
            }.runTaskTimer(0, period);
        }
    },
    BURST(1) {
        public void shoot(FireArm fireArm, int rateOfFire, int burstRounds) {
            final int amount = rateOfFire / 5; // Bullets per interact event
            final long period = 4 / amount; // Period between bullets

            new BattleRunnable() {
                int shots = 0;
                public void run() {
                    fireArm.playShotSound();
                    shots ++;

                    fireArm.setMagazine(fireArm.getMagazine() - 1);
                    fireArm.shootProjectile();
                    fireArm.update();

                    if (fireArm.getMagazine() <= 0 || shots >= burstRounds) {
                        fireArm.cooldown(fireArm.getCooldown());
                        cancel();
                    }
                }
            }.runTaskTimer(0, period);
        }
    },
    SEMI_AUTOMATIC(2) {
        public void shoot(FireArm fireArm, int rateOfFire, int burstRounds) {
            fireArm.playShotSound();

            fireArm.cooldown(fireArm.getCooldown());
            fireArm.setMagazine(fireArm.getMagazine() - 1);
            fireArm.shootProjectile();
            fireArm.update();
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

    public abstract void shoot(FireArm fireArm, int rateOfFire, int burstRounds);
}