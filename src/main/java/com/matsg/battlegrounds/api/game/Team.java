package com.matsg.battlegrounds.api.game;

import org.bukkit.Color;

public interface Team {

    Color getColor();

    String getName();

    Iterable<GamePlayer> getPlayers();
}