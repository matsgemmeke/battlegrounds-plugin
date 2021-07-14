package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import org.bukkit.Color;

public class QuickRevive extends PerkEffect {

    private static final float BUFFED_REVIVE_SPEED = 0.5f;
    private static final float NORMAL_REVIVE_SPEED = 1.0f;

    public QuickRevive(String displayName) {
        super(PerkEffectType.QUICK_REVIVE, displayName, Color.fromRGB(50, 150, 200));
    }

    public void apply() {
        gamePlayer.setReviveDuration(BUFFED_REVIVE_SPEED);
    }

    public void removePerk() {
        gamePlayer.setReviveDuration(NORMAL_REVIVE_SPEED);
    }
}
