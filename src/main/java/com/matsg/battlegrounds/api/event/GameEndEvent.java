package com.matsg.battlegrounds.api.event;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.gamemode.Objective;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class GameEndEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private Game game;
    private List<Team> standings;
    private Objective objective;
    private Team winner;

    public GameEndEvent(Game game, Objective objective, Team winner, List<Team> standings) {
        this.game = game;
        this.objective = objective;
        this.standings = standings;
        this.winner = winner;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Game getGame() {
        return game;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public Objective getObjective() {
        return objective;
    }

    public List<Team> getStandings() {
        return standings;
    }

    public Team getWinner() {
        return winner;
    }
}