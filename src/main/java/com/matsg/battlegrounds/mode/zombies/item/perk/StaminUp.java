package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Color;

public class StaminUp extends AbstractPerkEffect {

    private static final float BUFFED_WALK_SPEED = 0.25F;
    private static final float NORMAL_WALK_SPEED = 0.2F;

    public StaminUp(String displayName) {
        super(displayName, Color.fromRGB(250, 200, 100));
    }

    public void apply(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setWalkSpeed(BUFFED_WALK_SPEED);
    }

    public void remove(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setWalkSpeed(NORMAL_WALK_SPEED);
    }
}
