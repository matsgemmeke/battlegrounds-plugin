package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public interface Team {

    ChatColor getChatColor();

    Color getColor();

    int getId();

    int getKills();

    GamePlayer[] getLivingPlayers();

    String getName();

    GamePlayer[] getPlayers();

    int getScore();

    void setScore(int score);

    int getTeamSize();

    void addPlayer(GamePlayer gamePlayer);

    boolean hasPlayer(GamePlayer gamePlayer);

    void removePlayer(GamePlayer gamePlayer);
}
