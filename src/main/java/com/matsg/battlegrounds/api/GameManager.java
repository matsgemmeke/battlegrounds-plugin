package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface GameManager {

    /**
     * Gets all existing games
     *
     * @return all existing games
     */
    Collection<Game> getGames();

    /**
     * Gets if a game by a particular id exists.
     *
     * @param id the id to check
     * @return whether a game by this id exists
     */
    boolean exists(int id);

    /**
     * Gets all online players who are currently in a game.
     *
     * @return all players who are in a game
     */
    Iterable<GamePlayer> getAllPlayers();

    /**
     * Gets the arena which contains a certain location. Returns null if the location is not inside an arena.
     *
     * @param location the location
     * @return the arena which contains the location
     */
    Arena getArena(Location location);

    /**
     * Gets a list of all arenas of all existing games.
     *
     * @return a list of all arenas from every game
     */
    Iterable<Arena> getArenaList();

    /**
     * Gets the game of a certain arena.
     *
     * @param arena the arena to get its game from
     * @return the game which owns the arena
     */
    Game getGame(Arena arena);

    /**
     * Gets a game with a certain id. Returns null if no game exists with such id.
     *
     * @param id the id of the game
     * @return the game with the corresponding id
     */
    Game getGame(int id);

    /**
     * Gets the game this player is currently in. Returns null if the player is not in a game.
     *
     * @param player the player to get its game of
     * @return the game of which the player is in
     */
    Game getGame(Player player);

    /**
     * Gets the GamePlayer instance of a certain player. Returns null if the player is not in a game.
     *
     * @param player the player to get the instance of
     * @return the GamePlayer instance of this player
     */
    GamePlayer getGamePlayer(Player player);

    /**
     * Stops all currently running games
     */
    void shutdown();
}
