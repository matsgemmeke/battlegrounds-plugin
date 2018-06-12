package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.CacheYaml;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.PlayerStatus;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.config.BattleCacheYaml;
import com.matsg.battlegrounds.item.misc.SelectLoadout;
import com.matsg.battlegrounds.util.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

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
    private PlayerManager playerManager;
    private TimeControl timeControl;

    public BattleGame(Battlegrounds plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.arenaList = new ArrayList<>();
        this.eventHandler = new GameEventHandler(plugin);
        this.itemRegistry = new BattleItemRegistry();
        this.playerManager = new BattlePlayerManager(this);
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
                if (arenaList.size() >= 2) {
                    Random random = new Random();
                    Arena arena, activeArena = getActiveArena();
                    do {
                        arena = arenaList.get(random.nextInt(arenaList.size()));
                    } while (activeArena == arena);
                    setArena(arena);
                }
                rollback();

                itemRegistry.clear();
                playerManager.getPlayers().clear();

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

    public Location getSpawnPoint() {
        Location mainLobby = plugin.getBattlegroundsCache().getLocation("mainlobby");
        return mainLobby != null ? mainLobby : plugin.getServer().getWorlds().get(0).getSpawnLocation(); // Return the main lobby, otherwise the world spawn
    }

    private void preparePlayer(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        player.setFoodLevel(20);
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.setHealth(20.0);
        player.setSaturation((float) 10);

        player.getInventory().setArmorContents(new ItemStack[] {
                null,
                null,
                new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                        .addItemFlags(ItemFlag.values())
                        .setColor(gameMode.getTeam(gamePlayer).getColor())
                        .setDisplayName(ChatColor.WHITE + EnumMessage.ARMOR_VEST.getMessage())
                        .build(),
                new ItemStackBuilder(Material.LEATHER_HELMET)
                        .addItemFlags(ItemFlag.values())
                        .setColor(gameMode.getTeam(gamePlayer).getColor())
                        .setDisplayName(ChatColor.WHITE + EnumMessage.ARMOR_HELMET.getMessage())
                        .build()
        });

        Item selectLoadout = new SelectLoadout(this, gamePlayer);
        itemRegistry.addItem(selectLoadout);
        player.getInventory().setItem(ItemSlot.MISCELLANEOUS.getSlot(), selectLoadout.getItemStack());
    }

    public void rollback() {
        Arena arena = getArena();
        if (arena == null) {
            return;
        }
        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            Player player = gamePlayer.getPlayer();

            gamePlayer.getPlayer().teleport(getSpawnPoint());
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
            preparePlayer(gamePlayer);
        }
        gameMode.spawnPlayers(playerManager.getPlayers().toArray(new GamePlayer[playerManager.getPlayers().size()]));
        setState(GameState.STARTING);
        updateSign();
        new GameCountdown(this, configuration.getGameCountdown()).runTaskTimer(20, 20);
    }

    public void startGame() {
        timeControl = new BattleTimeControl(this);
        setState(GameState.IN_GAME);
        updateSign();

        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            if (gamePlayer.getLoadout() != null) {
                gamePlayer.getLoadout().updateInventory();
            }
        }
    }

    public void stop() {
        if (!state.isInProgress()) {
            state = GameState.WAITING;
            updateSign();
            return;
        }

        for (GamePlayer gamePlayer : playerManager.getPlayers()) {
            Player player = gamePlayer.getPlayer();
            player.setAllowFlight(true);
            player.setGameMode(org.bukkit.GameMode.CREATIVE);
            player.setHealth(20.0);

            if (gamePlayer.getLoadout().getPrimary() != null) {
                gamePlayer.getLoadout().getPrimary().setReloadCancelled(true);
            }
            if (gamePlayer.getLoadout().getSecondary() != null) {
                gamePlayer.getLoadout().getSecondary().setReloadCancelled(true);
            }
            playerManager.setVisible(gamePlayer, true);
            plugin.getPlayerStorage().addPlayerAttributes(gamePlayer);
        }

        state = GameState.RESETTING;
        timeControl.stop();

        clearGameData();
        updateSign();
    }

    public boolean updateSign() {
        return gameSign != null && gameSign.update();
    }
}