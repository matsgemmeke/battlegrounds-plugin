package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.GamePlayer;
import com.matsg.battlegrounds.api.game.Spawn;
import org.bukkit.Location;

public class ArenaSpawn extends Location implements Spawn {

    private GamePlayer gamePlayer;

    public ArenaSpawn(Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public Location getLocation() {
        return this;
    }

    public boolean isOccupied() {
        return gamePlayer != null;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}