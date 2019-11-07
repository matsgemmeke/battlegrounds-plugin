package com.matsg.battlegrounds.api.game;

import org.bukkit.Location;

public interface Spawn extends ArenaComponent {

    /**
     * Gets the location of the spawn.
     *
     * @return the spawn location
     */
    Location getLocation();

    /**
     * Gets the occupant of the spawn.
     *
     * @return the spawn occupant
     */
    SpawnOccupant getOccupant();

    /**
     * Sets the occupant of the spawn.
     *
     * @param occupant the spawn occupant
     */
    void setOccupant(SpawnOccupant occupant);

    /**
     * Gets the team id of which the spawn belongs to.
     *
     * @return the spawn team id
     */
    int getTeamId();

    /**
     * Gets whether the spawn is a base spawn of the team.
     *
     * @return whether the spawn is a base spawn
     */
    boolean isTeamBase();

    /**
     * Sets whether the spawn is a base spawn of the team.
     *
     * @param teamBase whether the spawn is a base spawn
     */
    void setTeamBase(boolean teamBase);

    /**
     * Gets whether the spawn is occupied by an occupant.
     *
     * @return whether the spawn is occupied
     */
    boolean isOccupied();
}
