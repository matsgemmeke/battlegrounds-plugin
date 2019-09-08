package com.matsg.battlegrounds.mode.zombies.item.powerup;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpCallback;
import com.matsg.battlegrounds.mode.zombies.item.PowerUpEffect;
import com.matsg.battlegrounds.util.XMaterial;
import org.bukkit.Material;

public class MaxAmmo implements PowerUpEffect {

    private Game game;
    private int duration;
    private Material material;
    private String name;

    public MaxAmmo(Game game, String name) {
        this.game = game;
        this.name = name;
        this.duration = 0;
        this.material = XMaterial.GUNPOWDER.parseMaterial();
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
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            for (Weapon weapon : gamePlayer.getLoadout().getWeapons()) {
                if (weapon != null) {
                    weapon.resetState();
                    weapon.update();
                }
            }
        }
        callback.onPowerUpEnd();
    }

    public boolean isApplicableForActivation() {
        return true;
    }

    public double modifyDamage(double damage) {
        return damage;
    }

    public int modifyPoints(int points) {
        return points;
    }
}
