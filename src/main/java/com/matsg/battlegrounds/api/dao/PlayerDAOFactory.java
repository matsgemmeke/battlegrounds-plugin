package com.matsg.battlegrounds.api.dao;

import com.matsg.battlegrounds.api.game.GamePlayer;

import java.util.List;
import java.util.UUID;

public interface PlayerDAOFactory {

    boolean contains(UUID uuid);

    boolean contains(UUID uuid, int gameId);

    PlayerDAO convertToPlayerDAO(GamePlayer gamePlayer);

    List<PlayerDAO> getList();

    PlayerDAO getPlayerDAO(UUID uuid);

    PlayerDAO getPlayerDAO(UUID uuid, int gameId);

    void register(UUID uuid, String name);

    void save();

    void updateGameStats(PlayerDAO playerDAO, int gameId, int round);

    void updatePlayerName(UUID uuid, String name);
}