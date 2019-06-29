package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import org.bukkit.Material;

public class DoublePoints implements PowerUpEffect {

    private int duration;
    private Material material;
    private String name;

    public DoublePoints(String name, int duration) {
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

    public void activate(Game game, PowerUpCallback callback) {
        callback.onPowerUpEnd();
    }

    public double modifyDamage(double damage) {
        return damage;
    }

    public int modifyPoints(int points) {
        return points * 2;
    }
}
