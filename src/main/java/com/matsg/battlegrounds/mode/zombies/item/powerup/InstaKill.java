package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Material;

public class InstaKill implements PowerUpEffect {

    private int duration;
    private Material material;
    private String name;

    public InstaKill(String name, int duration) {
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

    public void activate(Game game, PowerUpCallback callback) {
        callback.onPowerUpEnd();
    }

    public double modifyDamage(double damage) {
        return Double.MAX_VALUE;
    }

    public int modifyPoints(int points) {
        return points;
    }
}
