package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.PlayerStatus;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.config.BattleCacheYaml;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import com.matsg.battlegrounds.listener.GameEventHandler;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleGame implements Game {

    private final int id;
    private Battlegrounds plugin;
    private CacheYaml dataFile;
    private Countdown countdown;
    private GameConfiguration configuration;
    private GameMode gameMode;
    private GameSign gameSign;
    private GameState state;
    private ItemRegistry itemRegistry;
    private List<Arena> arenaList;
    private PlayerManager playerManager;
    private TimeControl timeControl;

    public BattleGame(Battlegrounds plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.arenaList = new ArrayList<>();
        this.itemRegistry = new BattleItemRegistry();
        this.playerManager = new BattlePlayerManager(this, plugin.getLevelConfig(), plugin.getPlayerStorage());
        this.state = GameState.WAITING;

        new GameEventHandler(plugin, this);

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

    public Countdown getCountdown() {
        return countdown;
    }

    public CacheYaml getDataFile() {
        return dataFile;
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

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameState getState() {
        return state;
    }

    public TimeControl getTimeControl() {
        return timeControl;
    }

    public void setConfiguration(GameConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setCountdown(Countdown countdown) {
        this.countdown = countdown;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setGameSign(GameSign gameSign) {
        this.gameSign = gameSign;
    }

    public void setState(GameState state) {
        this.state = state;
        this.gameMode.onStateChange(state);
    }

    public void broadcastMessage(Message message) {
        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            gamePlayer.sendMessage(message);
        }
    }

    public void broadcastMessage(String message) {
        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            gamePlayer.sendMessage(message);
        }
    }

    private void broadcastTitle(Title title, Placeholder... placeholders) {
        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            title.send(gamePlayer.getPlayer(), placeholders);
        }
    }

    private void clearGameData() {
        new BattleRunnable() {
            public void run() {
                Random random = new Random();

                if (arenaList.size() >= 2) {
                    Arena arena, activeArena = getActiveArena();
                    do {
                        arena = arenaList.get(random.nextInt(arenaList.size()));
                    } while (activeArena == arena);
                    setArena(arena);
                }
                if (configuration.getGameModes().length >= 2) {
                    GameMode gameMode, activeGameMode = getGameMode();
                    do {
                        gameMode = configuration.getGameModes()[random.nextInt(configuration.getGameModes().length)];
                    } while (activeGameMode == gameMode);
                    setGameMode(gameMode);
                }

                rollback();

                itemRegistry.clear();
                playerManager.getPlayers().clear();

                setState(GameState.WAITING);
                updateSign();
            }
        }.runTaskLater(200);
    }

    private Arena getActiveArena() {
        for (Arena arena : arenaList) {
            if (arena.isActive()) {
                return arena;
            }
        }
        return null;
    }

    public Location getLobby() {
        Location lobby = dataFile.getLocation("lobby");
        return lobby != null ? lobby : plugin.getServer().getWorlds().get(0).getSpawnLocation(); // Return the main lobby, otherwise the world spawn
    }

    public Location getSpawnPoint() {
        Location mainLobby = plugin.getBattlegroundsCache().getLocation("mainlobby");
        return mainLobby != null ? mainLobby : plugin.getServer().getWorlds().get(0).getSpawnLocation(); // Return the main lobby, otherwise the world spawn
    }

    public void rollback() {
        Arena arena = getArena();
        if (arena == null) {
            return;
        }
        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            Player player = gamePlayer.getPlayer();
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            player.teleport(getSpawnPoint());

            gamePlayer.getSavedInventory().restore(player);
            gamePlayer.setStatus(PlayerStatus.ACTIVE).apply(this, gamePlayer);
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

    public void startCountdown() {
        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            playerManager.preparePlayer(gamePlayer);
        }
        countdown = new GameCountdown(this, configuration.getGameCountdown());
        timeControl = new BattleTimeControl(this);
        gameMode.getScoreboard().display(this);
        gameMode.spawnPlayers(playerManager.getPlayers().toArray(new GamePlayer[playerManager.getPlayers().size()]));
    }

    public void startGame() {
        timeControl.start();
        setState(GameState.IN_GAME);
        updateSign();
    }

    public void stop() {
        if (state == GameState.WAITING || state == GameState.STARTING) {
            countdown.cancelCountdown();
            setState(GameState.WAITING);
            updateSign();
            for (GamePlayer gamePlayer : playerManager.getPlayers()) {
                gamePlayer.getPlayer().setScoreboard(new LobbyScoreboard(this).createScoreboard());
                playerManager.clearPlayer(gamePlayer);
            }
            return;
        }

        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            Player player = gamePlayer.getPlayer();
            player.setAllowFlight(true);
            player.setGameMode(org.bukkit.GameMode.CREATIVE);
            player.setHealth(20.0);

            Loadout loadout = gamePlayer.getLoadout();

            if (loadout != null && loadout.getPrimary() != null) {
                loadout.getPrimary().setReloadCancelled(true);
            }
            if (loadout != null && loadout.getSecondary() != null) {
                loadout.getSecondary().setReloadCancelled(true);
            }
            playerManager.setVisible(gamePlayer, true);
            plugin.getPlayerStorage().addPlayerAttributes(gamePlayer);
        }

        timeControl.stop();

        clearGameData();
        setState(GameState.RESETTING);
        updateSign();
    }

    public boolean updateSign() {
        return gameSign != null && gameSign.update();
    }
}