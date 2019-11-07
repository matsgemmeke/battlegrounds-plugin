package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import org.bukkit.scheduler.BukkitRunnable;

public class PowerUpActivationPeriod extends BukkitRunnable {

    private PowerUpCallback callback;

    public PowerUpActivationPeriod(PowerUpCallback callback) {
        this.callback = callback;
    }

    public void run() {
        callback.onPowerUpEnd();
    }
}
