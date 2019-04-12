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
     * @return All existing games
     */
    Collection<Game> getGames();

    /**
     * Gets if a game by a particular id exists.
     *
     * @param id The id to check
     * @return Whether a game by this id exists
     */
    boolean exists(int id);

    /**
     * Gets all online players who are currently in a game.
     *
     * @return All players who are in a game
     */
    Iterable<GamePlayer> getAllPlayers();

    /**
     * Gets the arena of a game with a certain name. Returns null if there is no arena with such name.
     *
     * @param game The game which owns the arena
     * @param name The name of the arena
     * @return The arena of the game with the name
     */
    Arena getArena(Game game, String name);

    /**
     * Gets the arena which contains a certain location. Returns null if the location is not inside an arena.
     *
     * @param location The location
     * @return
     */
    Arena getArena(Location location);

    /**
     * Gets a list of all arenas of all existing games.
     *
     * @return A list of all arenas from every game
     */
    Iterable<Arena> getArenaList();

    /**
     * Gets the game of a certain arena.
     *
     * @param arena The arena to get its game from
     * @return The game which owns the arena
     */
    Game getGame(Arena arena);

    /**
     * Gets a game with a certain id. Returns null if no game exists with such id.
     *
     * @param id The id of the game
     * @return The game with the corresponding id
     */
    Game getGame(int id);

    /**
     * Gets the game this player is currently in. Returns null if the player is not in a game.
     *
     * @param player The player to get its game of
     * @return The game of which the player is in
     */
    Game getGame(Player player);

    /**
     * Gets the GamePlayer instance of a certain player. Returns null if the player is not in a game.
     *
     * @param player The player to get the instance of
     * @return The GamePlayer instance of this player
     */
    GamePlayer getGamePlayer(Player player);

    /**
     * Stops all currently running games
     */
    void shutdown();
}
