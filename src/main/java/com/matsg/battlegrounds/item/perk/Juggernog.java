package com.matsg.battlegrounds.item.perk;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Color;

public class Juggernog extends AbstractPerkEffect {

    private static final double BUFFED_PLAYER_HEALTH = 40.0;
    private static final double NORMAL_PLAYER_HEALTH = 20.0;

    public Juggernog() {
        super(new MessageHelper().create(TranslationKey.PERK_JUGGERNOG), Color.fromRGB(200, 50, 50));
    }

    public void apply(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setMaxHealth(BUFFED_PLAYER_HEALTH);
        gamePlayer.getPlayer().setHealth(BUFFED_PLAYER_HEALTH);
    }

    public void remove(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setHealth(NORMAL_PLAYER_HEALTH);
        gamePlayer.getPlayer().setMaxHealth(NORMAL_PLAYER_HEALTH);
    }
}