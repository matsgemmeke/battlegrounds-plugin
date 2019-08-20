package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.item.WeaponContext;
import com.matsg.battlegrounds.mode.GameModeType;

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

    void addObjective(Objective objective);

    <T extends ArenaComponent> ComponentFactory<T> getComponentFactory(Class<T> componentClass);

    /**
     * Gets the name of the game mode.
     *
     * @return The game mode name.
     */
    String getName();

    /**
     * Gets the list of objectives that can be achieved to win the game mode.
     *
     * @return The list of objectives.
     */
    Iterable<Objective> getObjectives();

    GameScoreboard getScoreboard();

    String getShortName();

    Iterable<Team> getTeams();

    GameModeType getType();

    boolean isActive();

    void setActive(boolean active);

    /**
     * Adds a player to the game mode.
     *
     * @param gamePlayer The player to be added.
     */
    void addPlayer(GamePlayer gamePlayer);

    Objective getAchievedObjective();

    Spawn getRespawnPoint(GamePlayer gamePlayer);

    List<Team> getSortedTeams();

    Team getTeam(GamePlayer gamePlayer);

    Team getTeam(int id);

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

    void removePlayer(GamePlayer gamePlayer);

    boolean spawnPlayers(Iterable<GamePlayer> players);

    void start();

    void startCountdown();

    void stop();

    void tick();
}
