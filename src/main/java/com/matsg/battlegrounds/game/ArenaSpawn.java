package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.SpawnOccupant;
import org.bukkit.Location;

public class ArenaSpawn implements Spawn {

    private boolean teamBase;
    private int id, teamId;
    private Location location;
    private SpawnOccupant occupant;

    public ArenaSpawn(int id, Location location, int teamId) {
        this.id = id;
        this.location = location;
        this.teamId = teamId;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public SpawnOccupant getOccupant() {
        return occupant;
    }

    public void setOccupant(SpawnOccupant occupant) {
        this.occupant = occupant;
    }

    public int getTeamId() {
        return teamId;
    }

    public boolean isOccupied() {
        return occupant != null;
    }

    public boolean isTeamBase() {
        return teamBase;
    }

    public void setTeamBase(boolean teamBase) {
        this.teamBase = teamBase;
    }
}
