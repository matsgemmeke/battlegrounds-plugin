package com.matsg.battlegrounds.item.perk;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Color;

public class DoubleTap extends AbstractPerkEffect {

    private static final double BUFFED_FIREARM_DAMAGE = 1.33;
    private static final double NORMAL_FIREARM_DAMAGE = 1.0;

    public DoubleTap() {
        super(new MessageHelper().create(TranslationKey.PERK_DOUBLE_TAP), Color.fromRGB(250, 125, 0));
    }

    public void apply(GamePlayer gamePlayer) {
        gamePlayer.setFirearmDamage(BUFFED_FIREARM_DAMAGE);
    }

    public void remove(GamePlayer gamePlayer) {
        gamePlayer.setFirearmDamage(NORMAL_FIREARM_DAMAGE);
    }
}
