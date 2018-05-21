package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.gui.scoreboard.GameScoreboard;

public interface GameMode extends StateListener {

    void addPlayer(GamePlayer gamePlayer);

    Yaml getConfig();

    String getName();

    Spawn getRespawnPoint(GamePlayer gamePlayer);

    GameScoreboard getScoreboard();

    String getSimpleName();

    Team getTeam(GamePlayer gamePlayer);

    Team getTeam(int id);

    Iterable<Team> getTeams();

    void onKill(GamePlayer gamePlayer, GamePlayer killer, Weapon weapon);

    void removePlayer(GamePlayer gamePlayer);

    void spawnPlayers(GamePlayer... players);
}