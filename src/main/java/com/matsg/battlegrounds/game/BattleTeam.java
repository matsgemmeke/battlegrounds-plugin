package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class BattleTeam implements Team {

    private final int id;
    private ChatColor chatColor;
    private Color armorColor;
    private int score;
    private List<GamePlayer> players;
    private String name;

    public BattleTeam(int id, String name, Color armorColor, ChatColor chatColor) {
        this.id = id;
        this.name = name;
        this.armorColor = armorColor;
        this.chatColor = chatColor;
        this.players = new ArrayList<>();
        this.score = 0;
    }

    public Color getArmorColor() {
        return armorColor;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GamePlayer[] getPlayers() {
        return players.toArray(new GamePlayer[players.size()]);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPlayer(GamePlayer gamePlayer) {
        if (players.contains(gamePlayer)) {
            return;
        }
        players.add(gamePlayer);
        gamePlayer.setTeam(this);
    }

    public int getKills() {
        int kills = 0;
        for (GamePlayer gamePlayer : players) {
            kills += gamePlayer.getKills();
        }
        return kills;
    }

    public GamePlayer[] getLivingPlayers() {
        List<GamePlayer> list = new ArrayList<>();
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getState().isAlive()) {
                list.add(gamePlayer);
            }
        }
        return list.toArray(new GamePlayer[list.size()]);
    }

    public int getTeamSize() {
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
        gamePlayer.setTeam(null);
    }
}
