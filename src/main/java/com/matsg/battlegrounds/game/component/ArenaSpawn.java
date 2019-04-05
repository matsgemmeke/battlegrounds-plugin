package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;

public class ArenaSpawn implements Spawn {

    private boolean teamBase;
    private GamePlayer gamePlayer;
    private int id, teamId;
    private Location location;

    public ArenaSpawn(int id, Location location, int teamId) {
        this.id = id;
        this.location = location;
        this.teamId = teamId;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public int getTeamId() {
        return teamId;
    }

    public boolean isOccupied() {
        return gamePlayer != null;
    }

    public boolean isTeamBase() {
        return teamBase;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setTeamBase(boolean teamBase) {
        this.teamBase = teamBase;
    }
}
