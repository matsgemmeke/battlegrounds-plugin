package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;

public interface Spawn extends ArenaComponent {

    GamePlayer getGamePlayer();

    void setGamePlayer(GamePlayer gamePlayer);

    Location getLocation();

    int getTeamId();

    boolean isOccupied();

    boolean isTeamBase();

    void setTeamBase(boolean teamBase);
}
