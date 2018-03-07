package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.player.GamePlayer;

public interface GameMode {

    void addPlayer(GamePlayer gamePlayer);

    String getName();

    String getShortName();

    Iterable<Team> getTeams();

    void removePlayer(GamePlayer gamePlayer);

    void spawnPlayers(GamePlayer... players);
}