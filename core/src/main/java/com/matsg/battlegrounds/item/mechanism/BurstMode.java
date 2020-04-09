package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.item.Firearm;
import org.bukkit.scheduler.BukkitRunnable;

public class BurstMode implements FireMode {

    private Firearm firearm;
    private int burst;
    private int rateOfFire;
    private TaskRunner taskRunner;

    public BurstMode(int rateOfFire, int burst, TaskRunner taskRunner) {
        this.rateOfFire = rateOfFire;
        this.burst = burst;
        this.taskRunner = taskRunner;
    }

    private BurstMode(int rateOfFire, int burst, TaskRunner taskRunner, Firearm firearm) {
        this.rateOfFire = rateOfFire;
        this.burst = burst;
        this.taskRunner = taskRunner;
        this.firearm = firearm;
    }

    public Firearm getWeapon() {
        return firearm;
    }

    public void setWeapon(Firearm firearm) {
        this.firearm = firearm;
    }

    public FireMode clone() {
        return new BurstMode(rateOfFire, burst, taskRunner, firearm);
    }

    public void shoot() {
        int amount = rateOfFire / 5; // Bullets per interact event
        long period = 4 / amount; // Period between bullets

        taskRunner.runTaskTimer(new BukkitRunnable() {
            int shots = 0;

            public void run() {
                firearm.playShotSound();
                firearm.shootProjectile();
                firearm.setMagazine(firearm.getMagazine() - 1);
                firearm.update();

                shots++;

                if (firearm.getMagazine() <= 0 || shots >= burst) {
                    firearm.cooldown(firearm.getCooldown());
                    cancel();
                }
            }
        }, 0, period);
    }
}
