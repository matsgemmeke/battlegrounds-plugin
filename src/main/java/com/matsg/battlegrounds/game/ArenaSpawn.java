package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.game.Spawn;
import org.bukkit.Location;

public class ArenaSpawn extends Location implements Spawn {

    private boolean teamBase;
    private GamePlayer gamePlayer;
    private int index, teamId;

    public ArenaSpawn(int index, Location location, int teamId) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ());
        this.index = index;
        this.teamId = teamId;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getIndex() {
        return index;
    }

    public Location getLocation() {
        return this;
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