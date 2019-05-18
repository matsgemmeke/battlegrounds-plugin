package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;

public interface Arena extends ComponentWrapper, Extent {

    /**
     * Gets the name of the arena.
     *
     * @return The arena name
     */
    String getName();

    /**
     * Gets the spawn container of the arena.
     *
     * @return The spawn container.
     */
    ComponentContainer<Spawn> getSpawnContainer();

    /**
     * Gets whether the arena is active or not.
     *
     * @return True if the arena is active, otherwise false
     */
    boolean isActive();

    /**
     * Sets the active state of the arena
     *
     * @param active Whether the arena should be active or not
     */
    void setActive(boolean active);

    /**
     * Gets a random entry of the spawn collection which is not occupied by another
     * player.
     *
     * @return A random spawn entry
     */
    Spawn getRandomSpawn();

    /**
     * Gets a random entry of the spawn collection which is not occupied by another
     * player with a minimum distance from a location.
     *
     * @param location The location to calculate the distance from.
     * @param distance The minimum distance between the spawn and given location.
     * @return A random spawn entry with minimum distance.
     */
    Spawn getRandomSpawn(Location location, double distance);

    /**
     * Gets a random entry of the spawn collection owned by a specific team.
     *
     * @param teamId The id of the team to find a random spawn of
     * @return A random spawn entry
     */
    Spawn getRandomSpawn(int teamId);

    /**
     * Gets a random entry of the spawn collection owned by a specific team with a
     * minimum distance from a location.
     *
     * @param teamId The id of the team to find a random spawn of.
     * @param location The location to calculate the distance from.
     * @param distance The minimum distance between the spawn and given location.
     * @return A random spawn entry
     */
    Spawn getRandomSpawn(int teamId, Location location, double distance);

    /**
     * Gets the spawn which currently being used by a specific player.
     *
     * @param gamePlayer The player to find the spawn of
     * @return The spawn of the player if it is currently occupying one, otherwise null
     */
    Spawn getSpawn(GamePlayer gamePlayer);

    /**
     * Gets a spawn with a certain index number.
     *
     * @param index The spawn index
     * @return The spawn with the index number if one exists, otherwise null
     */
    Spawn getSpawn(int index);

    /**
     * Gets the base spawn of a team.
     *
     * @param teamId The id of the team to get the base spawn of
     * @return The team's base spawn
     */
    Spawn getTeamBase(int teamId);

    /**
     * Gets whether the arena has borders set up.
     *
     * @return True if the arena has borders, otherwise false
     */
    boolean hasBorders();
}
