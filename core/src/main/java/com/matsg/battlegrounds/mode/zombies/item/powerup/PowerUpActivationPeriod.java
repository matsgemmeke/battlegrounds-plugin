package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class PowerUpActivationPeriod extends BukkitRunnable {

    private Consumer<PowerUpEffect> callback;
    private PowerUpEffect powerUpEffect;

    public PowerUpActivationPeriod(PowerUpEffect powerUpEffect, Consumer<PowerUpEffect> callback) {
        this.powerUpEffect = powerUpEffect;
        this.callback = callback;
    }

    public void run() {
        callback.accept(powerUpEffect);
    }
}
