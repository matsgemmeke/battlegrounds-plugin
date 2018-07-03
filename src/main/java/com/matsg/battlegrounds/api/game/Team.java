package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public interface Team {

    void addPlayer(GamePlayer gamePlayer);

    ChatColor getChatColor();

    Color getColor();

    int getId();

    int getKills();

    String getName();

    Iterable<GamePlayer> getPlayers();

    int getScore();

    int getTotalPlayers();

    boolean hasPlayer(GamePlayer gamePlayer);

    void removePlayer(GamePlayer gamePlayer);

    void setScore(int score);
}