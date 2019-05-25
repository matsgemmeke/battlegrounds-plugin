package com.matsg.battlegrounds.item.perk;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Color;

public class QuickRevive extends AbstractPerkEffect {

    private static final double BUFFED_REVIVE_SPEED = 2.0;
    private static final double NORMAL_REVIVE_SPEED = 1.0;

    public QuickRevive(String displayName) {
        super(displayName, Color.fromRGB(50, 150, 200));
    }

    public void apply(GamePlayer gamePlayer) {
        gamePlayer.setReviveSpeed(BUFFED_REVIVE_SPEED);
    }

    public void remove(GamePlayer gamePlayer) {
        gamePlayer.setReviveSpeed(NORMAL_REVIVE_SPEED);
    }
}
