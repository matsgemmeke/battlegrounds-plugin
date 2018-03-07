package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.game.Spawn;
import org.bukkit.Location;

public class ArenaSpawn extends Location implements Spawn {

    private GamePlayer gamePlayer;
    private Team team;

    public ArenaSpawn(Location location, Team team) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ());
        this.team = team;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public Location getLocation() {
        return this;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isOccupied() {
        return gamePlayer != null;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}