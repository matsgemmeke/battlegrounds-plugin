package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.Location;

import java.util.List;

public interface Game {

    /**
     * Gets the currently active arena the game is using.
     *
     * @return the active arena or null if the game has no arenas set up
     */
    Arena getArena();

    /**
     * Sets the game's active arena.
     *
     * @param arena the arena to activate
     */
    void setArena(Arena arena);

    /**
     * Gets the list of all arenas added to the game.
     *
     * @return the full arena list
     */
    List<Arena> getArenaList();

    /**
     * Gets the game configuration variables.
     *
     * @return the game configuration
     */
    GameConfiguration getConfiguration();

    /**
     * Sets the game configuration.
     *
     * @param configuration the new game configuration
     */
    void setConfiguration(GameConfiguration configuration);

    /**
     * Gets the arena of the game with a certain name. Returns null if there is no arena with such name.
     *
     * @param name the name of the arena
     * @return the corresponding arena
     */
    Arena getArena(String name);

    /**
     * Gets the data file of the game where all of its settings are stored.
     *
     * @return the cache file of the game
     */
    CacheYaml getDataFile();

    /**
     * Gets the gamemode the game is currently using.
     *
     * @return the game's gamemode
     */
    GameMode getGameMode();

    /**
     * Gets all gamemode the game can run.
     *
     * @return the game's gamemode list
     */
    List<GameMode> getGameModeList();

    /**
     * Gets the game joining sign of this game.
     *
     * @return the game joining sign
     */
    GameSign getGameSign();

    /**
     * Sets the game joining sign.
     *
     * @param gameSign the game joining sign of this game
     */
    void setGameSign(GameSign gameSign);

    /**
     * Gets the id of this game.
     *
     * @return the game id
     */
    int getId();

    /**
     * Gets the item registry of this game.
     *
     * @return the game's item registry
     */
    ItemRegistry getItemRegistry();

    /**
     * Gets the game's lobby location.
     *
     * @return the game lobby or null if is not set up
     */
    Location getLobby();

    /**
     * Sets the game's lobby location.
     *
     * @param lobby the game lobby location
     */
    void setLobby(Location lobby);

    /**
     * Gets the mob manager of the game.
     *
     * @return the game's mob manager
     */
    MobManager getMobManager();

    /**
     * Gets the player manager of this game.
     *
     * @return the game's player manager
     */
    PlayerManager getPlayerManager();

    /**
     * Gets the current state of the game.
     *
     * @return the game's current state
     */
    GameState getState();

    /**
     * Sets the state of the game.
     *
     * @param state the state to set the game to
     */
    void setState(GameState state);

    /**
     * Gets the time control of the game.
     *
     * @return the game's time control
     */
    TimeControl getTimeControl();

    /**
     * Activates a gamemode instance and makes it ready for use.
     *
     * @param gameMode the gamemode to activate
     */
    void activateGameMode(GameMode gameMode);

    /**
     * Gets the first available component id in the game.
     *
     * @return the first available id
     */
    int findAvailableComponentId();

    /**
     * Gets all component wrapper instances involved in the game.
     *
     * @return the game's component wrappers
     */
    ComponentWrapper[] getComponentWrappers();

    /**
     * Gets the instance of a certain gamemode the game is using.
     *
     * @param gameModeClass the gamemode class
     * @return the instance of the corresponding gamemode or null if the game does not use the gamemode type
     */
    <T extends GameMode> T getGameMode(Class<T> gameModeClass);

    /**
     * Restores all changes in the game and arena so it is ready to start a new game.
     */
    void rollback();

    /**
     * Starts the game.
     */
    void startGame();

    /**
     * Starts the countdown which prepares the arena and players to start the game.
     */
    void startGameModeCountdown();

    /**
     * Starts the lobby countdown.
     */
    void startLobbyCountdown();

    /**
     * Stops the game when the gamemode has ended or the game is unable to continue.
     */
    void stop();

    /**
     * Updates the scoreboard that is currently being displayed.
     *
     * @return whether the scoreboard was updated
     */
    boolean updateScoreboard();

    /**
     * Updates the game joining sign.
     *
     * @return whether the game sign has updated or not
     */
    boolean updateSign();
}
