package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.item.Firearm;
import org.bukkit.scheduler.BukkitRunnable;

public class FullyAutomatic implements FireMode {

    private Firearm firearm;
    private int rateOfFire;
    private TaskRunner taskRunner;

    public FullyAutomatic(int rateOfFire, TaskRunner taskRunner) {
        this.rateOfFire = rateOfFire;
        this.taskRunner = taskRunner;
    }

    private FullyAutomatic(int rateOfFire, TaskRunner taskRunner, Firearm firearm) {
        this(rateOfFire, taskRunner);
        this.firearm = firearm;
    }

    public Firearm getWeapon() {
        return firearm;
    }

    public void setWeapon(Firearm firearm) {
        this.firearm = firearm;
    }

    public FireMode clone() {
        return new FullyAutomatic(rateOfFire, taskRunner, firearm);
    }

    public void shoot() {
        final int amount = rateOfFire / 5; // Bullets per interaction
        final long period = 4 / amount; // Amount of ticks between bullets

        taskRunner.runTaskTimer(new BukkitRunnable() {
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
        }, 0, period);
    }
}
