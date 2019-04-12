package com.matsg.battlegrounds.item.perk;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Color;

public class StaminUp extends AbstractPerkEffect {

    private static final float BUFFED_WALK_SPEED = 0.25F;
    private static final float NORMAL_WALK_SPEED = 0.2F;

    public StaminUp() {
        super(new MessageHelper().create(TranslationKey.PERK_STAMIN_UP), Color.fromRGB(250, 200, 100));
    }

    public void apply(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setWalkSpeed(BUFFED_WALK_SPEED);
    }

    public void remove(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setWalkSpeed(NORMAL_WALK_SPEED);
    }
}