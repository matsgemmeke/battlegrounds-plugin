package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.TacticalEffect;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum BattleTacticalEffect implements TacticalEffect {

    BLINDNESS(1, true) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {
            PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, duration, 1);
            gamePlayer.getPlayer().addPotionEffect(blindness);
        }
    },
    NOISE(2, false) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {
            Loadout loadout = gamePlayer.getLoadout();
            //Sound[] decoySound =
        }
    },
    SMOKE(3, false) {
        public void applyEffect(GamePlayer gamePlayer, Location location, int duration) {

        }
    };

    private boolean playerEffective;
    private int id;

    BattleTacticalEffect(int id, boolean playerEffective) {
        this.id = id;
        this.playerEffective = playerEffective;
    }

    public static TacticalEffect valueOf(int id) {
        for (BattleTacticalEffect tacticalEffect : values()) {
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