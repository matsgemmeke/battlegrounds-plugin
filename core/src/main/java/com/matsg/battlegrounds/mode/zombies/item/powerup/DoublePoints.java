package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class DoublePoints implements PowerUpEffect {

    private int duration;
    private Material material;
    private Plugin plugin;
    private String name;

    public DoublePoints(Plugin plugin, String name, int duration) {
        this.plugin = plugin;
        this.name = name;
        this.duration = duration;
        this.material = Material.GOLD_INGOT;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public void activate(PowerUpCallback callback) {
        PowerUpActivationPeriod activationPeriod = new PowerUpActivationPeriod(callback);
        activationPeriod.runTaskLater(plugin, duration);
    }

    public boolean isApplicableForActivation() {
        return true;
    }

    public double modifyDamage(double damage) {
        return damage;
    }

    public int modifyPoints(int points) {
        return points * 2;
    }
}
