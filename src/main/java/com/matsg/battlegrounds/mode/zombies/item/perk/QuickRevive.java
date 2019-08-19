package com.matsg.battlegrounds.mode.zombies.item.perk;

import com.matsg.battlegrounds.mode.zombies.item.PerkEffect;
import org.bukkit.Color;

public class QuickRevive extends PerkEffect {

    private static final float BUFFED_REVIVE_SPEED = 5.0f;
    private static final float NORMAL_REVIVE_SPEED = 10.0f;

    public QuickRevive(String displayName) {
        super(displayName, Color.fromRGB(50, 150, 200));
    }

    public void apply() {
        gamePlayer.setReviveDuration(BUFFED_REVIVE_SPEED);
    }

    public void remove() {
        gamePlayer.setReviveDuration(NORMAL_REVIVE_SPEED);
    }
}