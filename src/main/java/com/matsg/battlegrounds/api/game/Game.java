package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface Game {

    GamePlayer addPlayer(Player player);

    void broadcastMessage(Message message);

    void broadcastMessage(String message);

    Arena getArena();

    List<Arena> getArenaList();

    GameConfiguration getConfiguration();

    CacheYaml getDataFile();

    EventHandler getEventHandler();

    GameMode getGameMode();

    GamePlayer getGamePlayer(Player player);

    GameSign getGameSign();

    int getId();

    ItemRegistry getItemRegistry();

    GamePlayer[] getLivingPlayers();

    GamePlayer getNearestPlayer(Location location);

    List<GamePlayer> getPlayers();

    GameState getState();

    void removePlayer(Player player);

    void rollback();

    void setArena(Arena arena);

    void setConfiguration(GameConfiguration configuration);

    void setGameSign(GameSign gameSign);

    void setState(GameState state);

    void setVisible(GamePlayer gamePlayer, boolean visible);

    void stop();

    boolean updateSign();
}