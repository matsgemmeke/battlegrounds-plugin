package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.Location;
import org.bukkit.event.Event;

import java.util.List;

public interface Game {

    /**
     * Gets the currently active arena the game is using.
     *
     * @return The active arena or null if the game has no arenas set up
     */
    Arena getArena();

    /**
     * Sets the game's active arena.
     *
     * @param arena The arena to activate
     */
    void setArena(Arena arena);

    /**
     * Gets the list of all arenas added to the game.
     *
     * @return The full arena list
     */
    List<Arena> getArenaList();

    /**
     * Gets the game configuration variables.
     *
     * @return The game configuration
     */
    GameConfiguration getConfiguration();

    /**
     * Sets the game configuration.
     *
     * @param configuration The new game configuration
     */
    void setConfiguration(GameConfiguration configuration);

    /**
     * Gets the current countdown of the game.
     *
     * @return The countdown of the game or null if the game is not in its starting state
     */
    Countdown getCountdown();

    /**
     * Sets the countdown of the game.
     *
     * @param countdown The new game countdown
     */
    void setCountdown(Countdown countdown);

    /**
     * Gets the data file of the game where all of its settings are stored.
     *
     * @return The cache file of the game
     */
    CacheYaml getDataFile();

    /**
     * Gets the gamemode the game is currently using.
     *
     * @return The game's gamemode
     */
    GameMode getGameMode();

    /**
     * Sets the active gamemode of the game.
     *
     * @param gameMode The gamemode to activate
     */
    void setGameMode(GameMode gameMode);

    /**
     * Gets the game joining sign of this game.
     *
     * @return The game joining sign
     */
    GameSign getGameSign();

    /**
     * Sets the game joining sign.
     *
     * @param gameSign The game joining sign of this game
     */
    void setGameSign(GameSign gameSign);

    /**
     * Gets the id of this game.
     *
     * @return The game id
     */
    int getId();

    /**
     * Gets the item registry of this game.
     *
     * @return The game's item registry
     */
    ItemRegistry getItemRegistry();

    /**
     * Gets the game's lobby location.
     *
     * @return The game lobby or null if is not set up
     */
    Location getLobby();

    /**
     * Gets the mob manager of the game.
     *
     * @return the game's mob manager
     */
    MobManager getMobManager();

    /**
     * Gets the player manager of this game.
     *
     * @return The game's player manager
     */
    PlayerManager getPlayerManager();

    /**
     * Gets the location players should be sent to after the game has ended.
     *
     * @return The main lobby or the main world's spawn point if the main lobby is not set up
     */
    Location getSpawnPoint();

    /**
     * Gets the current state of the game.
     *
     * @return The game's current state
     */
    GameState getState();

    /**
     * Sets the state of the game.
     *
     * @param state The state to set the game to
     */
    void setState(GameState state);

    /**
     * Gets the time control of the game.
     *
     * @return The game's time control
     */
    TimeControl getTimeControl();

    /**
     * Calls an event to the plugin manager.
     *
     * @param event The event to call
     */
    void callEvent(Event event);

    /**
     * Gets the first available component id in the game.
     *
     * @return the first available id
     */
    int findAvailableComponentId();

    /**
     * Gets the instance of a certain gamemode the game is using.
     *
     * @param gameModeClass The gamemode class.
     * @return The instance of the corresponding gamemode or null if the game does not use the gamemode type.
     */
    <T extends GameMode> T getGameMode(Class<T> gameModeClass);

    /**
     * Restores all changes in the game and arena so it is ready to start a new game.
     */
    void rollback();

    /**
     * Starts the countdown which prepares the arena and players to start the game.
     */
    void startCountdown();

    /**
     * Starts the game.
     */
    void startGame();

    /**
     * Stops the game when the gamemode has ended or the game is unable to continue.
     */
    void stop();

    /**
     * Updates the scoreboard that is currently being displayed.
     *
     * @return Whether the scoreboard was updated.
     */
    boolean updateScoreboard();

    /**
     * Updates the game joining sign.
     *
     * @return Whether the game sign has updated or not
     */
    boolean updateSign();
}
