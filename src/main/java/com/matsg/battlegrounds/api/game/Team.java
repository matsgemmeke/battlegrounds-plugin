package com.matsg.battlegrounds.api.game;

import org.bukkit.Color;

import java.util.Collection;

public interface Team {

    Color getColor();

    String getName();

    Collection<GamePlayer> getPlayers();
}