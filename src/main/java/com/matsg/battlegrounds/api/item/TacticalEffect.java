package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;

public enum TacticalEffect {

    BLINDNESS(0, true) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {

        }
    },
    NOISE(1, false) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {

        }
    },
    SMOKE(2, false) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {

        }
    };

    private boolean playerEffective;
    private int id;

    TacticalEffect(int id, boolean playerEffective) {
        this.id = id;
        this.playerEffective = playerEffective;
    }

    public static TacticalEffect valueOf(int id) {
        for (TacticalEffect tacticalEffect : values()) {
            if (tacticalEffect.id == id) {
                return tacticalEffect;
            }
        }
        throw new IllegalArgumentException();
    }

    public boolean isPlayerEffective() {
        return playerEffective;
    }

    public abstract void applyEffect(GamePlayer gamePlayer, Location location, int duration);
}