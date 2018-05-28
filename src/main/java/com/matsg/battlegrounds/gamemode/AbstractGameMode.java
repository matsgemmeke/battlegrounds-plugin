package com.matsg.battlegrounds.gamemode;

import com.matsg.battlegrounds.api.config.Yaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.item.misc.SelectLoadout;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameMode implements GameMode {

    protected Game game;
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

    public Team getTeam(GamePlayer gamePlayer) {
        for (Team team : teams) {
            if (team.getPlayers().contains(gamePlayer)) {
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

    public void onStateChange(GameState state) {
        switch (state) {
            case IN_GAME:
                onStart();
            case RESETTING:
                onStop();
        }
    }

    public void onStart() {
        for (Spawn spawn : game.getArena().getSpawns()) {
            spawn.setGamePlayer(null);
        }
    }

    public void onStop() { }
}