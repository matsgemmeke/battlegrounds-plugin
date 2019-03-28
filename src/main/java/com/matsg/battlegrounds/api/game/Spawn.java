package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;

public interface Spawn extends GameComponent {

    GamePlayer getGamePlayer();

    Location getLocation();

    int getTeamId();

    boolean isOccupied();

    boolean isTeamBase();

    void setGamePlayer(GamePlayer gamePlayer);

    void setTeamBase(boolean teamBase);
}
