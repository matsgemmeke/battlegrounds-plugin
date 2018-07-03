package com.matsg.battlegrounds.api.event;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class GameEndEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private Game game;
    private List<Team> sortedTeams;
    private Team winner;

    public GameEndEvent(Game game, Team winner, List<Team> sortedTeams) {
        this.game = game;
        this.winner = winner;
        this.sortedTeams = sortedTeams;
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

    public List<Team> getSortedTeams() {
        return sortedTeams;
    }

    public Team getWinner() {
        return winner;
    }
}