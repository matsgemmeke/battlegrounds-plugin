package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.game.Spawn;
import org.bukkit.Location;

public class ArenaSpawn extends Location implements Spawn {

    private GamePlayer gamePlayer;
    private int teamId;

    public ArenaSpawn(Location location, int teamId) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ());
        this.teamId = teamId;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
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

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}