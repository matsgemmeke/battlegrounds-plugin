package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.game.mode.GameModeType;

public interface GameMode {

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

    /**
     * Adds a player to the game mode.
     *
     * @param gamePlayer The player to be added.
     */
    void addPlayer(GamePlayer gamePlayer);

    Spawn getRespawnPoint(GamePlayer gamePlayer);

    Team getTeam(GamePlayer gamePlayer);

    Team getTeam(int id);

    Team getTopTeam();

    void onDeath(GamePlayer gamePlayer, DeathCause deathCause);

    void onDisable();

    void onEnable();

    void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox);

    void removePlayer(GamePlayer gamePlayer);

    void spawnPlayers(GamePlayer... players);

    void start();

    void stop();

    void tick();
}
