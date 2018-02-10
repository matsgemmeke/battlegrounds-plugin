package com.matsg.battlegrounds.api.game;

import org.bukkit.Location;

public interface Spawn {

    GamePlayer getGamePlayer();

    Location getLocation();

    boolean isOccupied();

    void setGamePlayer(GamePlayer gamePlayer);
}
