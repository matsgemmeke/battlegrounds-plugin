package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.storage.*;
import com.matsg.battlegrounds.api.event.GameStartEvent;
import com.matsg.battlegrounds.api.event.GameStateChangeEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.entity.state.ActivePlayerState;
import com.matsg.battlegrounds.game.state.WaitingState;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleGame implements Game {

    private final int id;
    private Arena arena;
    private CacheYaml dataFile;
    private Countdown countdown;
    private EventDispatcher eventDispatcher;
    private GameConfiguration configuration;
    private GameMode gameMode;
    private GameSign gameSign;
    private GameState state;
    private ItemRegistry itemRegistry;
    private List<Arena> arenaList;
    private List<GameMode> gameModeList;
    private Location lobby;
    private MobManager mobManager;
    private PlayerManager playerManager;
    private PlayerStorage playerStorage;
    private TaskRunner taskRunner;
    private TimeControl timeControl;
    private Translator translator;

    public BattleGame(
            int id,
            CacheYaml cache,
            CacheYaml dataFile,
            EventDispatcher eventDispatcher,
            GameState state,
            ItemRegistry itemRegistry,
            LevelConfig levelConfig,
            MobManager mobManager,
            PlayerStorage playerStorage,
            TaskRunner taskRunner,
            Translator translator
    ) {
        this.id = id;
        this.dataFile = dataFile;
        this.eventDispatcher = eventDispatcher;
        this.state = state;
        this.itemRegistry = itemRegistry;
        this.mobManager = mobManager;
        this.playerStorage = playerStorage;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.arenaList = new ArrayList<>();
        this.gameModeList = new ArrayList<>();
        this.playerManager = new BattlePlayerManager(this, cache, levelConfig, playerStorage, translator);
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
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

    public List<GameMode> getGameModeList() {
        return gameModeList;
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

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
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
        eventDispatcher.dispatchExternalEvent(new GameStateChangeEvent(this, this.state, state));
        this.state = state;
    }

    public TimeControl getTimeControl() {
        return timeControl;
    }

    public void activateGameMode(GameMode gameMode) {
        if (this.gameMode != null) {
            this.gameMode.setActive(false);
            this.gameMode.onDisable();
        }
        this.gameMode = gameMode;
        this.gameMode.setActive(true);
        this.gameMode.onEnable();
    }

    public int findAvailableComponentId() {
        int i = 1;

        while (true) {
            boolean available = true;

            // Check if the gamemodes contain components with the same id.
            for (GameMode gameMode : gameModeList) {
                if (gameMode.getComponent(i) != null) {
                    available = false;
                    break;
                }
            }

            // Check if the arenas contain components with the same id.
            for (Arena arena : arenaList) {
                if (available && arena.getComponent(i) == null) {
                    return i;
                }
            }

            i++;
        }
    }

    public Arena getArena(String name) {
        for (Arena arena : arenaList) {
            if (arena.getName().equals(name)) {
                return arena;
            }
        }
        return null;
    }

    public ComponentWrapper[] getComponentWrappers() {
        return new ComponentWrapper[] { arena, gameMode };
    }

    public <T extends GameMode> T getGameMode(Class<T> gameModeClass) {
        for (GameMode gameMode : gameModeList) {
            if (gameMode.getClass() == gameModeClass) {
                return (T) gameMode;
            }
        }
        return null;
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
            player.teleport(gamePlayer.getReturnLocation());

            gamePlayer.getSavedInventory().restore(player);
            gamePlayer.getState().remove();

            if (gamePlayer.getDownState() != null) {
                gamePlayer.getDownState().dispose();
            }
        }
    }

    public void startGameModeCountdown() {
        // Check whether the player spawning logic was executed correctly. If not, cancel the countdown
        if (!gameMode.spawnPlayers(playerManager.getPlayers())) {
            return;
        }

        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            gameMode.preparePlayer(gamePlayer);
        }

        timeControl = new BattleTimeControl(this, taskRunner);

        taskRunner.runTaskTimer(gameMode.startCountdown(), 0, 20);

        updateScoreboard();
    }

    public void startGame() {
        countdown = null;
        setState(state.next());
        updateSign();
        gameMode.start();
        timeControl.start();
        eventDispatcher.dispatchExternalEvent(new GameStartEvent(this));
    }

    public void startLobbyCountdown() {
        int countdownLength = configuration.getLobbyCountdown();
        int[] displayNumbers = new int[] { 60, 45, 30, 15, 10, 5 };
        int delay = 0;
        int period = 20;

        Countdown countdown = new LobbyCountdown(this, translator, countdownLength, displayNumbers);

        taskRunner.runTaskTimer(countdown, delay, period);
    }

    public void stop() {
        if (!state.isInProgress()) {
            if (countdown != null) {
                countdown.cancel();
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

            playerStorage.getStoredPlayer(gamePlayer.getUUID()).addStatisticAttributes(context);
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
        taskRunner.runTaskLater(new BukkitRunnable() {
            public void run() {
                Random random = new Random();

                if (arenaList.size() >= 2) {
                    Arena arena;

                    do {
                        arena = arenaList.get(random.nextInt(arenaList.size()));
                    } while (BattleGame.this.arena == arena);

                    setArena(arena);
                }
                if (gameModeList.size() >= 2) {
                    GameMode gameMode;

                    do {
                        gameMode = gameModeList.get(random.nextInt(gameModeList.size()));
                    } while (BattleGame.this.gameMode == gameMode);

                    activateGameMode(gameMode);
                }

                rollback();

                itemRegistry.clear();
                playerManager.getPlayers().clear();

                resetState();
                updateSign();
            }
        }, 200);
    }
}
