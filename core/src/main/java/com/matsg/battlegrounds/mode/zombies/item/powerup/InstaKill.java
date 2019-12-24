package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class InstaKill implements PowerUpEffect {

    private int duration;
    private Plugin plugin;
    private Material material;
    private String name;

    public InstaKill(Plugin plugin, String name, int duration) {
        this.plugin = plugin;
        this.name = name;
        this.duration = duration;
        this.material = XMaterial.SKELETON_SKULL.parseMaterial();
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

    public void activate(Consumer<PowerUpEffect> callback) {
        PowerUpActivationPeriod activationPeriod = new PowerUpActivationPeriod(this, callback);
        activationPeriod.runTaskLater(plugin, duration);
    }

    public boolean isApplicableForActivation() {
        return true;
    }

    public double modifyDamage(double damage) {
        return Double.MAX_VALUE;
    }

    public int modifyPoints(int points) {
        return points;
    }
}
