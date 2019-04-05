package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.game.mode.GameModeType;

public interface GameMode {

    /**
     * Adds a player to the game mode.
     *
     * @param gamePlayer The player to be added.
     */
    void addPlayer(GamePlayer gamePlayer);

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

    Spawn getRespawnPoint(GamePlayer gamePlayer);

    GameScoreboard getScoreboard();

    String getShortName();

    Team getTeam(GamePlayer gamePlayer);

    Team getTeam(int id);

    Iterable<Team> getTeams();

    Team getTopTeam();

    GameModeType getType();

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
