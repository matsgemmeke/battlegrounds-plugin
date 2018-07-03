package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractGameMode implements GameMode {

    protected Game game;
    protected int timeLimit;
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

    protected Message getKillMessage(Hitbox hitbox) {
        switch (hitbox) {
            case HEAD:
                return EnumMessage.DEATH_HEADSHOT;
            case LEG:
                return EnumMessage.DEATH_PLAYER_KILL;
            case TORSO:
                return EnumMessage.DEATH_PLAYER_KILL;
            default:
                return null;
        }
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
}