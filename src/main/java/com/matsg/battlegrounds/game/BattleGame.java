package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.config.BattleCacheYaml;
import com.matsg.battlegrounds.di.DIFactory;
import com.matsg.battlegrounds.util.ActionBar;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleGame implements Game {

    private final int id;
    private Battlegrounds plugin;
    private CacheYaml dataFile;
    private EventHandler eventHandler;
    private GameConfiguration configuration;
    private GameMode gameMode;
    private GameSign gameSign;
    private GameState state;
    private ItemRegistry itemRegistry;
    private List<Arena> arenaList;
    private List<GamePlayer> players;

    public BattleGame(Battlegrounds plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.arenaList = new ArrayList<>();
        this.itemRegistry = (ItemRegistry) DIFactory.createInstance(ItemRegistry.class);
        this.players = new ArrayList<>();
        this.state = GameState.WAITING;

        try {
            this.dataFile = new BattleCacheYaml(plugin, plugin.getDataFolder().getPath() + "/data", "game_" + id + ".yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Arena getArena() {
        return getActiveArena();
    }

    public List<Arena> getArenaList() {
        return arenaList;
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public CacheYaml getDataFile() {
        return dataFile;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public GameSign getGameSign() {
        return gameSign;
    }

    public int getId() {
        return id;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public GameState getState() {
        return state;
    }

    public void setConfiguration(GameConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setGameSign(GameSign gameSign) {
        this.gameSign = gameSign;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GamePlayer addPlayer(Player player) {
        GamePlayer gamePlayer = (GamePlayer) DIFactory.createInstance(GamePlayer.class, player);
        Location lobby = dataFile.getLocation("lobby");

        players.add(gamePlayer);

        broadcastMessage(EnumMessage.PREFIX.getMessage() + " " + EnumMessage.PLAYER_JOIN.getMessage(
                new Placeholder("player_name", player.getName()),
                new Placeholder("zombies_players", players.size()),
                new Placeholder("zombies_maxplayers", configuration.getMaxPlayers())));
        updateSign();

        gamePlayer.setStatus(PlayerStatus.ALIVE).apply(this, gamePlayer);

        if (lobby != null) {
            player.teleport(lobby);
        }
        if (players.size() == configuration.getMinPlayers()) {
            new Countdown(this, configuration.getCountdownLength(), 60, 45, 30, 15, 10).run();
        }
        return gamePlayer;
    }

    public void broadcastMessage(Message message) {
        for (GamePlayer gamePlayer : players) {
            gamePlayer.sendMessage(message);
        }
    }

    public void broadcastMessage(String message) {
        for (GamePlayer gamePlayer : players) {
            gamePlayer.sendMessage(message);
        }
    }

    private void broadcastTitle(Title title, Placeholder... placeholders) {
        for (GamePlayer gamePlayer : players) {
            title.send(gamePlayer.getPlayer(), placeholders);
        }
    }

    private void clearGameData() {
        new BattleRunnable() {
            public void run() {
                if (arenaList.size() >= 2) {
                    Random random = new Random();
                    Arena arena;
                    do {
                        arena = arenaList.get(random.nextInt(arenaList.size()));
                    } while (getActiveArena() == arena);
                    setArena(arena);
                }
                rollback();

                itemRegistry.clear();
                players.clear();

                state = GameState.WAITING;
                updateSign();
            }
        }.runTaskLater(240);
    }

    private Arena getActiveArena() {
        for (Arena arena : arenaList) {
            if (arena.isActive()) {
                return arena;
            }
        }
        return null;
    }

    public GamePlayer getGamePlayer(Player player) {
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getPlayer() == player) {
                return gamePlayer;
            }
        }
        return null;
    }

    private Location getLeavingLocation() {
        Location mainLobby = plugin.getBattlegroundsCache().getLocation("mainlobby");
        return mainLobby != null ? mainLobby : plugin.getServer().getWorlds().get(0).getSpawnLocation(); // Return the main lobby, otherwise the world spawn
    }

    public GamePlayer[] getLivingPlayers() {
        List<GamePlayer> list = new ArrayList<>();
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getStatus().canInteract()) {
                list.add(gamePlayer);
            }
        }
        return list.toArray(new GamePlayer[list.size()]);
    }

    public GamePlayer getNearestPlayer(Location location) {
        return getNearestPlayer(location, Double.MAX_VALUE);
    }

    public GamePlayer getNearestPlayer(Location location, double range) {
        double distance = range;
        GamePlayer nearestPlayer = null;
        for (GamePlayer gamePlayer : getLivingPlayers()) {
            if (gamePlayer != null && gamePlayer.getStatus().canInteract() && location.getWorld() == gamePlayer.getPlayer().getWorld()
                    && location.distanceSquared(gamePlayer.getPlayer().getLocation()) < distance) {
                distance = location.distanceSquared(gamePlayer.getPlayer().getLocation());
                nearestPlayer = gamePlayer;
            }
        }
        return nearestPlayer;
    }

    public void givePoints(GamePlayer gamePlayer, int points) {
        ActionBar.POINTS_INCREASE.send(gamePlayer.getPlayer(), new Placeholder("bg_points", points));
        gamePlayer.setPoints(gamePlayer.getPoints() + points);
    }

    public void removePlayer(Player player) {
        GamePlayer gamePlayer = getGamePlayer(player);

        players.remove(gamePlayer);

        broadcastMessage(EnumMessage.PREFIX.getMessage() + " " + EnumMessage.PLAYER_LEAVE.getMessage(
                new Placeholder("player_name", player.getName()),
                new Placeholder("bg_players", players.size()),
                new Placeholder("bg_maxplayers", configuration.getMaxPlayers())));

        gamePlayer.getPlayer().teleport(getLeavingLocation());
        gamePlayer.getSavedInventory().restore(player);
        gamePlayer.setStatus(PlayerStatus.ALIVE).apply(this, gamePlayer);

        if (getLivingPlayers().length <= 0) {
            stop();
        }
    }

    public void rollback() {
        Arena arena = getArena();
        if (arena == null) {
            return;
        }
        for (GamePlayer gamePlayer : players) {
            Player player = gamePlayer.getPlayer();

            gamePlayer.getPlayer().teleport(getLeavingLocation());
            gamePlayer.getSavedInventory().restore(player);
            gamePlayer.setStatus(PlayerStatus.ALIVE).apply(this, gamePlayer);
        }
    }

    public void setArena(Arena arena) {
        for (Arena other : arenaList) {
            if (other.isActive()) {
                other.setActive(false);
            }
        }
        arena.setActive(true);
    }

    public void setVisible(GamePlayer gamePlayer, boolean visible) {
        for (GamePlayer other : players) {
            if (visible) {
                other.getPlayer().showPlayer(gamePlayer.getPlayer());
            } else {
                other.getPlayer().hidePlayer(gamePlayer.getPlayer());
            }
        }
    }

    public void stop() {
        if (!state.isInProgress()) {
            state = GameState.WAITING;
            updateSign();
            return;
        }

        for (GamePlayer gamePlayer : getPlayers()) {
            Player player = gamePlayer.getPlayer();
            player.setAllowFlight(true);
            player.setGameMode(org.bukkit.GameMode.CREATIVE);
            player.setMaxHealth(20.0);
            player.setHealth(20.0);

            if (gamePlayer.getPrimary() != null) {
                gamePlayer.getPrimary().setReloadCancelled(true);
            }
            if (gamePlayer.getSecondary() != null) {
                gamePlayer.getSecondary().setReloadCancelled(true);
            }
            setVisible(gamePlayer, true);
        }

        plugin.getPlayerStorage().save();
        state = GameState.RESETTING;

        clearGameData();
        updateSign();
    }

    public void updateScoreboard() {
        //new GameScoreboard(plugin.getZombiesConfig().getGameScoreboardLayout()).set(this);
    }

    public boolean updateSign() {
        return gameSign != null && gameSign.update();
    }
}