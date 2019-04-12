package com.matsg.battlegrounds.item.perk;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Color;

public class SpeedCola extends AbstractPerkEffect {

    private static final double BUFFED_RELOAD_SPEED = 1.5;
    private static final double NORMAL_RELOAD_SPEED = 1.0;

    public SpeedCola() {
        super(new MessageHelper().create(TranslationKey.PERK_SPEED_COLA), Color.fromRGB(0, 128, 0));
    }

    public void apply(GamePlayer gamePlayer) {
        gamePlayer.setReloadSpeed(BUFFED_RELOAD_SPEED);
    }

    public void remove(GamePlayer gamePlayer) {
        gamePlayer.setReloadSpeed(NORMAL_RELOAD_SPEED);
    }
}
