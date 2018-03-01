package com.matsg.battlegrounds.game.gamemode;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.game.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameMode implements GameMode {

    protected Game game;
    protected List<Team> teams;
    protected String name, shortName;

    public AbstractGameMode(Game game, String name, String shortName) {
        this.game = game;
        this.name = name;
        this.shortName = shortName;
        this.teams = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Iterable<Team> getTeams() {
        return teams;
    }
}