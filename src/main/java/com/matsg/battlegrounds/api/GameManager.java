package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface GameManager {

    boolean exists(int id);

    Iterable<GamePlayer> getAllPlayers();

    Arena getArena(Game game, String name);

    Arena getArena(Location location);

    Iterable<Arena> getArenaList();

    Game getGame(Arena arena);

    Game getGame(int id);

    Game getGame(Player player);

    GamePlayer getGamePlayer(Player player);

    Iterable<Game> getGames();

    void shutdown();
}