package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.api.event.GameStartEvent;
import com.matsg.battlegrounds.api.event.GameStateChangeEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.storage.StatisticContext;
import com.matsg.battlegrounds.game.state.WaitingState;
import com.matsg.battlegrounds.storage.BattleCacheYaml;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

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
    private MobManager mobManager;
    private PlayerManager playerManager;
    private TimeControl timeControl;

    public BattleGame(Battlegrounds plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.arenaList = new ArrayList<>();
        this.itemRegistry = new BattleItemRegistry();
        this.mobManager = new BattleMobManager(plugin.getBattlegroundsConfig());
        this.playerManager = new BattlePlayerManager(this, plugin.getLevelConfig(), plugin.getPlayerStorage(), plugin.getTranslator());
        this.state = new WaitingState();

        try {
            this.dataFile = new BattleCacheYaml(plugin, plugin.getDataFolder().getPath() + "/data/game_" + id, "setup.yml");
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

    public void setConfiguration(GameConfiguration configuration) {
        this.configuration = configuration;
    }

    public Countdown getCountdown() {
        return countdown;
    }

    public void setCountdown(Countdown countdown) {
        this.countdown = countdown;
    }

    public CacheYaml getDataFile() {
        return dataFile;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        if (this.gameMode != null) {
            this.gameMode.setActive(false);
            this.gameMode.onDisable();
        }
        this.gameMode = gameMode;
        this.gameMode.setActive(true);
        this.gameMode.onEnable();
    }

    public GameSign getGameSign() {
        return gameSign;
    }

    public void setGameSign(GameSign gameSign) {
        this.gameSign = gameSign;
    }

    public int getId() {
        return id;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public MobManager getMobManager() {
        return mobManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.plugin.getServer().getPluginManager().callEvent(new GameStateChangeEvent(this, this.state, state));
        this.state = state;
    }

    public TimeControl getTimeControl() {
        return timeControl;
    }

    public void callEvent(Event event) {
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public int findAvailableComponentId() {
        int i = 1;

        while (true) {
            boolean available = false;

            // Check if the gamemodes contain components with the same id.
            for (GameMode gameMode : configuration.getGameModes()) {
                if (gameMode.getComponent(i) == null) {
                    available = true;
                }
            }

            // Check if the arena itself contains components with the same id.
            if (available && getActiveArena().getComponent(i) == null) {
                return i;
            }

            i++;
        }
    }

    public <T extends GameMode> T getGameMode(Class<T> gameModeClass) {
        for (GameMode gameMode : configuration.getGameModes()) {
            if (gameMode.getClass() == gameModeClass) {
                return (T) gameMode;
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

    private void resetState() {
        setState(new WaitingState());
    }

    public void rollback() {
        if (gameMode != null) {
            gameMode.onDisable();
        }

        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            Player player = gamePlayer.getPlayer();
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            player.teleport(getSpawnPoint());

            gamePlayer.getSavedInventory().restore(player);
            gamePlayer.setState(PlayerState.ACTIVE);
            gamePlayer.getState().apply(this, gamePlayer);
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
        // Check whether the player spawning logic was executed correctly. If not, cancel the countdown
        if (!gameMode.spawnPlayers(playerManager.getPlayers())) {
            return;
        }

        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            gameMode.preparePlayer(gamePlayer);
        }

        timeControl = new BattleTimeControl(this);
        gameMode.startCountdown();
        updateScoreboard();
    }

    public void startGame() {
        countdown = null;
        setState(state.next());
        updateSign();
        gameMode.start();
        timeControl.start();
        callEvent(new GameStartEvent(this));
    }

    public void stop() {
        if (!state.isInProgress()) {
            if (countdown != null) {
                countdown.cancelCountdown();
            }
            resetState();
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

            StatisticContext context = new StatisticContext();
            context.setGame(this);
            context.setPlayer(gamePlayer);

            plugin.getPlayerStorage().getStoredPlayer(gamePlayer.getUUID()).addStatisticAttributes(context);
        }

        gameMode.stop();
        timeControl.stop();

        clearGameData();
        setState(state.next());
        updateSign();
    }

    public boolean updateScoreboard() {
        boolean scoreboard = gameMode.getScoreboard() != null;
        if (scoreboard) {
            gameMode.getScoreboard().display(this);
        }
        return scoreboard;
    }

    public boolean updateSign() {
        return gameSign != null && gameSign.update();
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

                gameMode.loadData(getActiveArena());
                itemRegistry.clear();
                playerManager.getPlayers().clear();

                resetState();
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
}
