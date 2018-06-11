package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.util.EnumMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractGameMode implements GameMode {

    protected Game game;
    protected int killsToWin, timeLimit;
    protected List<Team> teams;
    protected String name, simpleName;
    protected Yaml yaml;

    public AbstractGameMode(Game game, String name, String simpleName, Yaml yaml) {
        this.game = game;
        this.name = name;
        this.simpleName = simpleName;
        this.teams = new ArrayList<>();
        this.yaml = yaml;
    }

    public Yaml getConfig() {
        return yaml;
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public Iterable<Team> getTeams() {
        return teams;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    protected String getEndMessage() {
        if (killsToWin != 0 && getTopTeam().getKills() >= killsToWin) {
            return EnumMessage.ENDREASON_SCORE.getMessage();
        } else if (timeLimit != 0 && game.getTimeControl().getTime() > timeLimit) {
            return EnumMessage.ENDREASON_TIME.getMessage();
        } else if (game.getPlayerManager().getLivingPlayers().length <= 1) {
            return EnumMessage.ENDREASON_ELIMINATION.getMessage();
        }
        return null;
    }

    protected List<Team> getSortedTeams() {
        List<Team> list = new ArrayList<>();
        list.addAll(teams);

        Collections.sort(list, new Comparator<Team>() {
            public int compare(Team o1, Team o2) {
                return ((Integer) o1.getKills()).compareTo(o2.getKills());
            }
        });

        return list;
    }

    public void onStateChange(GameState state) {
        switch (state) {
            case IN_GAME:
                onStart();
                break;
            case RESETTING:
                onStop();
                break;
        }
    }

    public void onStart() {
        for (Spawn spawn : game.getArena().getSpawns()) {
            spawn.setGamePlayer(null);
        }
    }

    public void onStop() { }

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
}