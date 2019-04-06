package com.matsg.battlegrounds.item.perk;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Color;

public class Juggernog extends AbstractPerkEffect {

    public Juggernog() {
        super(new MessageHelper().create(TranslationKey.PERK_JUGGERNOG), Color.fromRGB(200, 50, 50));
    }

    public void apply(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setMaxHealth(40.0);
        gamePlayer.getPlayer().setHealth(40.0);
    }

    public void remove(GamePlayer gamePlayer) {
        gamePlayer.getPlayer().setHealth(20.0);
        gamePlayer.getPlayer().setMaxHealth(20.0);
    }
}
