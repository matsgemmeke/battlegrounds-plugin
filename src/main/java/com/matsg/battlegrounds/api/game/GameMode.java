package com.matsg.battlegrounds.api.game;

public interface GameMode {

    String getName();

    String getShortName();

    Iterable<Team> getTeams();

    void spawnPlayers(GamePlayer... players);
}