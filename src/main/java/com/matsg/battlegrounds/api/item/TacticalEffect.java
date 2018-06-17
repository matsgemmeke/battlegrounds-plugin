package com.matsg.battlegrounds.api.item;

import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;

public interface TacticalEffect {

    void applyEffect(GamePlayer gamePlayer, Location location, int duration);

    void onIgnite(Tactical tactical, org.bukkit.entity.Item item);
}