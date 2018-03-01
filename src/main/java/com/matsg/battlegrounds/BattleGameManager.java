package com.matsg.battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BattleGameManager implements GameManager {

    private List<Game> games;

    public BattleGameManager() {
        this.games = new ArrayList<>();
    }

    public boolean exists(int id) {
        return getGame(id) != null;
    }

    public List<GamePlayer> getAllPlayers() {
        List<GamePlayer> list = new ArrayList<>();
        for (Game game : games) {
            list.addAll(game.getPlayerManager().getPlayers());
        }
        return list;
    }

    public Arena getArena(Game game, String name) {
        for (Arena arena : game.getArenaList()) {
            if (arena.getName().equals(name)) {
                return arena;
            }
        }
        return null;
    }

    public Arena getArena(Location location) {
        for (Arena arena : getArenaList()) {
            if (arena.contains(location)) {
                return arena;
            }
        }
        return null;
    }

    public List<Arena> getArenaList() {
        List<Arena> list = new ArrayList<>();
        for (Game game : games) {
            list.addAll(game.getArenaList());
        }
        return list;
    }

    public Game getGame(Arena arena) {
        for (Game game : games) {
            for (Arena other : game.getArenaList()) {
                if (other == arena) {
                    return game;
                }
            }
        }
        return null;
    }

    public Game getGame(int id) {
        for (Game game : games) {
            if (game.getId() == id) {
                return game;
            }
        }
        return null;
    }

    public Game getGame(Player player) {
        for (Game game : games) {
            if (game.getPlayerManager().getGamePlayer(player) != null) {
                return game;
            }
        }
        return null;
    }

    public GamePlayer getGamePlayer(Player player) {
        for (Game game : games) {
            GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
            if (gamePlayer != null) {
                return gamePlayer;
            }
        }
        return null;
    }

    public List<Game> getGames() {
        return games;
    }

    public void shutdown() {
        for (Game game : games) {
            game.rollback();
        }
    }
}