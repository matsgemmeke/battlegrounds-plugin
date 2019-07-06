package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Color;

public class DoubleTap extends AbstractPerkEffect {

    private static final double BUFFED_FIREARM_DAMAGE = 1.33;
    private static final double NORMAL_FIREARM_DAMAGE = 1.0;

    public DoubleTap(String displayName) {
        super(displayName, Color.fromRGB(250, 125, 0));
    }

    public void apply(GamePlayer gamePlayer) {
        gamePlayer.setFirearmDamage(BUFFED_FIREARM_DAMAGE);
    }

    public void remove(GamePlayer gamePlayer) {
        gamePlayer.setFirearmDamage(NORMAL_FIREARM_DAMAGE);
    }
}
