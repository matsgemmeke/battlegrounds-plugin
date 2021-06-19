package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import org.bukkit.Color;

public class Juggernog extends PerkEffect {

    private static final double BUFFED_PLAYER_HEALTH = 40.0;
    private static final double NORMAL_PLAYER_HEALTH = 20.0;

    public Juggernog(String displayName) {
        super(PerkEffectType.JUGGERNOG, displayName, Color.fromRGB(200, 50, 50));
    }

    public void apply() {
        gamePlayer.getPlayer().setMaxHealth(BUFFED_PLAYER_HEALTH);
        gamePlayer.getPlayer().setHealth(BUFFED_PLAYER_HEALTH);
    }

    public void removePerk() {
        gamePlayer.getPlayer().setHealth(NORMAL_PLAYER_HEALTH);
        gamePlayer.getPlayer().setMaxHealth(NORMAL_PLAYER_HEALTH);
    }
}
