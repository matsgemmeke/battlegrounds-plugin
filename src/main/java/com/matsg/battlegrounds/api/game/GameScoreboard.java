package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.World;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;
import java.util.Set;

public interface GameScoreboard {

    Map<String, String> getLayout();

    void setLayout(Map<String, String> layout);

    String getScoreboardId();

    Set<World> getWorlds();

    Scoreboard createScoreboard();

    void display(Game game);

    void display(GamePlayer gamePlayer);
}
