package com.matsg.battlegrounds.api.game;

public interface GameMode {

    String getName();

    Iterable<Team> getTeams();
}