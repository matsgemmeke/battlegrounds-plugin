package com.matsg.battlegrounds.game.mode;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.storage.Yaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.util.MessageHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractGameMode implements GameMode {

    protected Battlegrounds plugin;
    protected boolean active;
    protected Game game;
    protected List<Objective> objectives;
    protected List<Team> teams;
    protected MessageHelper messageHelper;
    protected String name, shortName;
    protected Yaml config;

    public AbstractGameMode(Battlegrounds plugin, Game game, Yaml config) {
        this.plugin = plugin;
        this.game = game;
        this.config = config;
        this.active = false;
        this.messageHelper = new MessageHelper();
        this.objectives = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Iterable<Team> getTeams() {
        return teams;
    }

    protected Objective getAchievedObjective() {
        for (Objective objective : objectives) {
            if (objective.isAchieved(game)) {
                return objective;
            }
        }
        return null;
    }

    protected List<Team> getSortedTeams() {
        List<Team> list = new ArrayList<>();
        list.addAll(teams);

        Collections.sort(list, new Comparator<Team>() {
            public int compare(Team o1, Team o2) {
                return ((Integer) o2.getScore()).compareTo(o1.getScore()); // Reverse sort
            }
        });

        return list;
    }

    public Team getTeam(GamePlayer gamePlayer) {
        for (Team team : teams) {
            if (team.hasPlayer(gamePlayer)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeam(int id) {
        for (Team team : teams) {
            if (team.getId() == id) {
                return team;
            }
        }
        return null;
    }

    public Team getTopTeam() {
        return getSortedTeams().get(0);
    }

    public void loadData(Arena arena) { }

    public void onDisable() { }

    public void onEnable() { }
}
