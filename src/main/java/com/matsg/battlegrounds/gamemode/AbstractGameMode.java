package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.game.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameMode implements GameMode {

    protected Game game;
    protected List<Team> teams;
    protected String name, shortName;
    protected Yaml yaml;

    public AbstractGameMode(Game game, String name, String shortName, Yaml yaml) {
        this.game = game;
        this.name = name;
        this.shortName = shortName;
        this.teams = new ArrayList<>();
        this.yaml = yaml;
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