package com.matsg.battlegrounds.api.game;

import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.util.Message;
import org.bukkit.Location;

import java.util.List;

public interface Game {

    void broadcastMessage(Message message);

    void broadcastMessage(String message);

    Arena getArena();

    List<Arena> getArenaList();

    GameConfiguration getConfiguration();

    Countdown getCountdown();

    CacheYaml getDataFile();

    GameMode getGameMode();

    GameSign getGameSign();

    int getId();

    ItemRegistry getItemRegistry();

    Location getLobby();

    PlayerManager getPlayerManager();

    Location getSpawnPoint();

    GameState getState();

    TimeControl getTimeControl();

    void rollback();

    void setArena(Arena arena);

    void setConfiguration(GameConfiguration configuration);

    void setCountdown(Countdown countdown);

    void setGameMode(GameMode gameMode);

    void setGameSign(GameSign gameSign);

    void setState(GameState state);

    void startCountdown();

    void startGame();

    void stop();

    boolean updateSign();
}