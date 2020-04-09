package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import org.bukkit.Color;

public class SpeedCola extends PerkEffect {

    private static final double RELOAD_SPEED_BUFF = 1.5;

    private Firearm[] firearms;

    public SpeedCola(String displayName) {
        super(PerkEffectType.SPEED_COLA, displayName, Color.fromRGB(0, 128, 0));
    }

    public void apply() {
        firearms = gamePlayer.getLoadout().getFirearms();

        for (Firearm firearm : gamePlayer.getLoadout().getFirearms()) {
            if (firearm != null) {
                firearm.setReloadDuration(Math.round((float) (firearm.getReloadDuration() / RELOAD_SPEED_BUFF)));
            }
        }
    }

    public void refresh() {
        for (int i = 0; i < firearms.length; i++) {
            Firearm firearm = gamePlayer.getLoadout().getFirearms()[i];
            // Check if the player had changed firearms and if so, apply the reload duration buff
            if (firearms[i] != firearm) {
                firearms[i] = firearm;
                firearm.setReloadDuration(Math.round((float) (firearm.getReloadDuration() / RELOAD_SPEED_BUFF)));
            }
        }
    }

    public void remove() {
        for (Firearm firearm : gamePlayer.getLoadout().getFirearms()) {
            if (firearm != null) {
                firearm.setReloadDuration(Math.round((float) (firearm.getReloadDuration() * RELOAD_SPEED_BUFF)));
            }
        }
    }
}
