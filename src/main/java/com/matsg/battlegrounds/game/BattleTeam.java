package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class BattleTeam implements Team {

    private final int id;
    private ChatColor chatColor;
    private Color color;
    private List<GamePlayer> players;
    private String name;

    public BattleTeam(int id, String name, Color color, ChatColor chatColor) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.chatColor = chatColor;
        this.players = new ArrayList<>();
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public void addPlayer(GamePlayer gamePlayer) {
        if (players.contains(gamePlayer)) {
            return;
        }
        players.add(gamePlayer);
    }

    public int getKills() {
        int kills = 0;
        for (GamePlayer gamePlayer : players) {
            kills += gamePlayer.getKills();
        }
        return kills;
    }

    public int getScore() {
        int score = 0;
        for (GamePlayer gamePlayer : players) {
            score += gamePlayer.getScore();
        }
        return score;
    }

    public int getTotalPlayers() {
        return players.size();
    }

    public boolean hasPlayer(GamePlayer gamePlayer) {
        return players.contains(gamePlayer);
    }

    public void removePlayer(GamePlayer gamePlayer) {
        if (!players.contains(gamePlayer)) {
            return;
        }
        players.remove(gamePlayer);
    }
}