package com.matsg.battlegrounds.game.gamemode;

import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.game.GamePlayer;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.di.DIContainer;
import com.matsg.battlegrounds.game.BattleGamePlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameMode implements GameMode {

    protected List<Team> teams;
    protected String name, shortName;

    public AbstractGameMode(String name, String shortName) {
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