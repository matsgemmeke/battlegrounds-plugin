package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.WeaponContext;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface GameMode extends ComponentWrapper, WeaponContext {

    /**
     * Hook method that gets called whenever the gamemode gets instantiated by the plugin.
     */
    void onCreate();

    /**
     * Hook method that gets called whenever the game sets the gamemode to inactive.
     */
    void onDisable();

    /**
     * Hook method that gets called whenever the game sets the gamemode to active.
     */
    void onEnable();

    /**
     * Adds an objective to the gamemode.
     *
     * @param objective the objective to add
     */
    void addObjective(Objective objective);

    /**
     * Gets the id of the game mode.
     *
     * @return the game mode id
     */
    String getId();

    /**
     * Gets the name of the game mode.
     *
     * @return the game mode name
     */
    String getName();

    /**
     * Gets the list of objectives that can be achieved to win the game mode.
     *
     * @return the list of objectives
     */
    Iterable<Objective> getObjectives();

    /**
     * Gets the scoreboard of the gamemode.
     *
     * @return the gamemode's scoreboard
     */
    GameScoreboard getScoreboard();

    /**
     * Gets the gamemode's abbreviation.
     *
     * @return the gamemode's short name
     */
    String getShortName();

    /**
     * Gets the teams involved in the gamemode.
     *
     * @return the gamemode's teams
     */
    Iterable<Team> getTeams();

    /**
     * Gets whether the gamemode is currently in use by the game.
     *
     * @return whether the gamemode is active
     */
    boolean isActive();

    /**
     * Sets whether the gamemode is currently in use by the game.
     *
     * @param active whether the gamemode is active
     */
    void setActive(boolean active);

    /**
     * Adds a player to the game mode.
     *
     * @param gamePlayer The player to be added.
     */
    void addPlayer(GamePlayer gamePlayer);

    /**
     * Gets the objective that is achieved by the current game state. Returns null if
     * none of the objective's conditions are met.
     *
     * @return the achieved objective or null if none are achieved yet
     */
    Objective getAchievedObjective();

    /**
     * Gets the respawn point of a player.
     *
     * @param gamePlayer the player to get a respawn point of.
     * @return a respawn point as a spawn component
     */
    Spawn getRespawnPoint(GamePlayer gamePlayer);

    /**
     * Gets a list of the gamemode's teams, sorted by their performance.
     *
     * @return a sorted list of the gamemode's teams
     */
    List<Team> getSortedTeams();

    /**
     * Gets the team of a player. Returns null if the player is not assigned to a team.
     *
     * @param gamePlayer the player to get the team of
     * @return the player's team or null if the player is not assigned to a team.
     */
    Team getTeam(GamePlayer gamePlayer);

    /**
     * Gets the team by a certain id.
     *
     * @param teamId the team id
     * @return the corresponding team or null if a team by the id does not exist
     */
    Team getTeam(int teamId);

    /**
     * Gets the best performing team in the gamemode.
     *
     * @return the top team
     */
    Team getTopTeam();

    /**
     * Loads extra gamemode prerequisites of an arena from the game's data file.
     *
     * @param arena The arena being used.
     */
    void loadData(Arena arena);

    /**
     * Prepares the player for the gamemode.
     *
     * @param gamePlayer the player to prepare
     */
    void preparePlayer(GamePlayer gamePlayer);

    /**
     * Removes a player from the game.
     *
     * @param gamePlayer the player to remove
     */
    void removePlayer(GamePlayer gamePlayer);

    /**
     * Spreads players over the spawns in the game's arena.
     *
     * @param players the players to be spawned in
     * @return whether the spawning was succesfull
     */
    boolean spawnPlayers(Iterable<GamePlayer> players);

    /**
     * Starts the gamemode.
     */
    void start();

    /**
     * Starts the gamemode countdown.
     */
    void startCountdown();

    /**
     * Stops the gamemode
     */
    void stop();

    /**
     * Handles gamemode logic of a single tick.
     */
    void tick();
}
