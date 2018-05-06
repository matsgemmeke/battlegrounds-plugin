package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.player.GamePlayer;

public interface GameMode {

    void addPlayer(GamePlayer gamePlayer);

    String getName();

    String getSimpleName();

    Team getTeam(GamePlayer gamePlayer);

    Team getTeam(int id);

    Iterable<Team> getTeams();

    void onStart();

    void onStop();

    void removePlayer(GamePlayer gamePlayer);

    void spawnPlayers(GamePlayer... players);
}