package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class BattleTeam implements Team {

    private Color color;
    private List<GamePlayer> players;
    private String name;

    public BattleTeam(String name, Color color) {
        this.name = name;
        this.color = color;
        this.players = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public int getScore() {
        int score = 0;
        for (GamePlayer gamePlayer : players) {
            score += gamePlayer.getScore();
        }
        return score;
    }
}