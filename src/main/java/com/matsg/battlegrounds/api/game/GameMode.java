package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.player.GamePlayer;

public interface GameMode {

    String getName();

    String getShortName();

    Iterable<Team> getTeams();

    void spawnPlayers(GamePlayer... players);
}