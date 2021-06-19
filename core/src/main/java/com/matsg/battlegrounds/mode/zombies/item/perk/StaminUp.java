package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import org.bukkit.Color;

public class StaminUp extends PerkEffect {

    private static final float BUFFED_WALK_SPEED = 0.25F;
    private static final float NORMAL_WALK_SPEED = 0.2F;

    public StaminUp(String displayName) {
        super(PerkEffectType.STAMIN_UP, displayName, Color.fromRGB(250, 200, 100));
    }

    public void apply() {
        gamePlayer.getPlayer().setWalkSpeed(BUFFED_WALK_SPEED);
    }

    public void removePerk() {
        gamePlayer.getPlayer().setWalkSpeed(NORMAL_WALK_SPEED);
    }
}
