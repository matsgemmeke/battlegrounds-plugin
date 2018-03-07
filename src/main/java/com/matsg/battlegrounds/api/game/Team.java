package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Color;

import java.util.Collection;

public interface Team {

    Color getColor();

    String getName();

    Collection<GamePlayer> getPlayers();
}