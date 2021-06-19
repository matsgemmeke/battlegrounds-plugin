package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import org.bukkit.Color;

import java.util.HashMap;

public class SpeedCola extends PerkEffect {

    private static final double RELOAD_SPEED_BUFF = 1.5;
    private HashMap<Firearm, Integer> originalValues;

    public SpeedCola(String displayName) {
        super(PerkEffectType.SPEED_COLA, displayName, Color.fromRGB(0, 128, 0));
        this.originalValues = new HashMap<>();
    }

    public void apply() {
        for (Firearm firearm : gamePlayer.getLoadout().getFirearms()) {
            if (firearm != null) {
                originalValues.put(firearm, firearm.getReloadDuration());

                firearm.setReloadDuration(Math.round((float) (firearm.getReloadDuration() / RELOAD_SPEED_BUFF)));
            }
        }
    }

    public void refresh() {
        for (Firearm firearm : gamePlayer.getLoadout().getFirearms()) {
            if (!originalValues.containsKey(firearm)) {
                originalValues.put(firearm, firearm.getReloadDuration());

                firearm.setReloadDuration(Math.round((float) (firearm.getReloadDuration() / RELOAD_SPEED_BUFF)));
            }
        }
    }

    public void removePerk() {
        for (Firearm firearm : gamePlayer.getLoadout().getFirearms()) {
            if (firearm != null) {
                firearm.setReloadDuration(originalValues.get(firearm));
            }
        }
    }
}
