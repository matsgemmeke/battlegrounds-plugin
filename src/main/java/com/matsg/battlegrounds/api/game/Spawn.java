package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;

public interface Spawn {

    GamePlayer getGamePlayer();

    Location getLocation();

    Team getTeam();

    boolean isOccupied();

    void setGamePlayer(GamePlayer gamePlayer);
}