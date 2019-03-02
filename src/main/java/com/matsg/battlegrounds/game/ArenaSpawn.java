package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;

public class ArenaSpawn implements Spawn {

    private boolean teamBase;
    private GamePlayer gamePlayer;
    private int index, teamId;
    private Location location;

    public ArenaSpawn(int index, Location location, int teamId) {
        this.index = index;
        this.location = location;
        this.teamId = teamId;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getIndex() {
        return index;
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