package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;

public interface Arena extends ComponentWrapper, Extent {

    /**
     * Gets the name of the arena.
     *
     * @return the arena name
     */
    String getName();

    /**
     * Gets the spawn container of the arena.
     *
     * @return the spawn container
     */
    ComponentContainer<Spawn> getSpawnContainer();

    /**
     * Gets whether the arena is active or not.
     *
     * @return true if the arena is active, otherwise false
     */
    boolean isActive();

    /**
     * Sets the active state of the arena
     *
     * @param active whether the arena should be active or not
     */
    void setActive(boolean active);

    /**
     * Gets a random entry of the spawn collection which is not occupied by another
     * player.
     *
     * @return a random spawn entry
     */
    Spawn getRandomSpawn();

    /**
     * Gets a random entry of the spawn collection which is not occupied by another
     * player with a minimum distance from a location.
     *
     * @param location the location to calculate the distance from
     * @param distance the minimum distance between the spawn and given location
     * @return a random spawn entry with minimum distance
     */
    Spawn getRandomSpawn(Location location, double distance);

    /**
     * Gets a random entry of the spawn collection owned by a specific team.
     *
     * @param teamId the id of the team to find a random spawn of
     * @return a random spawn entry
     */
    Spawn getRandomSpawn(int teamId);

    /**
     * Gets a random entry of the spawn collection owned by a specific team with a
     * minimum distance from a location.
     *
     * @param teamId the id of the team to find a random spawn of
     * @param location the location to calculate the distance from
     * @param distance the minimum distance between the spawn and given location
     * @return a random spawn entry
     */
    Spawn getRandomSpawn(int teamId, Location location, double distance);

    /**
     * Gets the spawn which currently being used by a specific player.
     *
     * @param gamePlayer the player to find the spawn of
     * @return the spawn of the player if it is currently occupying one, otherwise null
     */
    Spawn getSpawn(GamePlayer gamePlayer);

    /**
     * Gets a spawn with a certain index number.
     *
     * @param index the spawn index
     * @return the spawn with the index number if one exists, otherwise null
     */
    Spawn getSpawn(int index);

    /**
     * Gets the amount of spawns.
     *
     * @return the spawn count
     */
    int getSpawnCount();

    /**
     * Gets the amount of spawns assigned to a certain team.
     *
     * @param teamId the team id
     * @return the spawn count
     */
    int getSpawnCount(int teamId);

    /**
     * Gets the base spawn of a team.
     *
     * @param teamId The id of the team to get the base spawn of
     * @return the team's base spawn
     */
    Spawn getTeamBase(int teamId);

    /**
     * Gets whether the arena has borders set up.
     *
     * @return true if the arena has borders, otherwise false
     */
    boolean hasBorders();
}
