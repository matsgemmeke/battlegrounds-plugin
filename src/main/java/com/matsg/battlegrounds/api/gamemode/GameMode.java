package com.matsg.battlegrounds.api.gamemode;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.game.GameScoreboard;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;

public interface GameMode extends StateListener {

    void addPlayer(GamePlayer gamePlayer);

    Yaml getConfig();

    String getName();

    Iterable<Objective> getObjectives();

    Spawn getRespawnPoint(GamePlayer gamePlayer);

    GameScoreboard getScoreboard();

    String getShortName();

    Team getTeam(GamePlayer gamePlayer);

    Team getTeam(int id);

    Iterable<Team> getTeams();

    int getTimeLimit();

    Team getTopTeam();

    void onDeath(GamePlayer gamePlayer, DeathCause deathCause);

    void onDisable();

    void onEnable();

    void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon, Hitbox hitbox);

    void removePlayer(GamePlayer gamePlayer);

    void setTimeLimit(int timeLimit);

    void spawnPlayers(GamePlayer... players);

    void tick();
}