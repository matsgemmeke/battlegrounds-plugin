package com.matsg.battlegrounds.api.game;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Set;

public interface GameScoreboard {

    Scoreboard createScoreboard();

    void display(Game game);

    void display(Player player);

    String getScoreboardId();

    Set<World> getWorlds();
}