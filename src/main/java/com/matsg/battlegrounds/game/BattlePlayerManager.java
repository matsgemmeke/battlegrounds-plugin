package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.config.LevelConfig;
import com.matsg.battlegrounds.api.config.StoredPlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.PlayerStatus;
import com.matsg.battlegrounds.api.player.PlayerStorage;
import com.matsg.battlegrounds.api.util.Message;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import com.matsg.battlegrounds.item.misc.SelectLoadout;
import com.matsg.battlegrounds.player.BattleGamePlayer;
import com.matsg.battlegrounds.util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BattlePlayerManager implements PlayerManager {

    private Game game;
    private LevelConfig levelConfig;
    private List<GamePlayer> players;
    private PlayerStorage playerStorage;

    public BattlePlayerManager(Game game, LevelConfig levelConfig, PlayerStorage playerStorage) {
        this.game = game;
        this.levelConfig = levelConfig;
        this.players = new ArrayList<>();
        this.playerStorage = playerStorage;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    private void addLoadout(GamePlayer gamePlayer, Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            game.getItemRegistry().addItem(weapon);
            weapon.setGame(game);
            weapon.setGamePlayer(gamePlayer);
            weapon.resetState();
            weapon.update();
        }
    }

    public GamePlayer addPlayer(Player player) {
        GamePlayer gamePlayer = new BattleGamePlayer(player);
        Location lobby = game.getDataFile().getLocation("lobby");

        players.add(gamePlayer);

        broadcastMessage(EnumMessage.PLAYER_JOIN.getMessage(
                new Placeholder("player_name", player.getName()),
                new Placeholder("bg_players", players.size()),
                new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers())));

        game.getGameMode().addPlayer(gamePlayer);
        game.updateSign();

        gamePlayer.getPlayer().setScoreboard(new LobbyScoreboard(game).createScoreboard());
        gamePlayer.setStatus(PlayerStatus.ACTIVE).apply(game, gamePlayer);

        updateExpBar(gamePlayer);

        if (lobby != null) {
            player.teleport(lobby);
        }
        if (game.getArena() != null && players.size() == game.getConfiguration().getMinPlayers()) {
            Countdown countdown = new LobbyCountdown(game, game.getConfiguration().getLobbyCountdown(), 60, 45, 30, 15, 10, 5);
            game.setCountdown(countdown);
            countdown.run();
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

    public void changeLoadout(GamePlayer gamePlayer, Loadout loadout, boolean apply) {
        Loadout old = gamePlayer.getLoadout();
        gamePlayer.setLoadout(loadout);
        if (!apply) {
            gamePlayer.sendMessage(ActionBar.CHANGE_LOADOUT);
            return;
        }
        if (old != null) {
            clearLoadout(old);
        }
        addLoadout(gamePlayer, loadout);
        loadout.updateInventory();
    }

    private void clearLoadout(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null) {
                weapon.remove();
                weapon.setGame(null);
                weapon.setGamePlayer(null);
            }
        }
    }

    public void clearPlayer(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        player.closeInventory();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        if (gamePlayer.getLoadout() != null) {
            clearLoadout(gamePlayer.getLoadout());
        }
    }

    public void damagePlayer(GamePlayer gamePlayer, double damage) {
        if (gamePlayer == null || !gamePlayer.getStatus().isAlive() || gamePlayer.getPlayer().isDead()) {
            return;
        }
        double finalHealth = gamePlayer.getPlayer().getHealth() - damage;
        gamePlayer.getPlayer().damage(0.01); // Create a fake damage animation
        gamePlayer.getPlayer().setHealth(finalHealth > 0.0 ? finalHealth : 0); // It needs to set the health to 0 if the damage is greater than the health, else the api will complain
        gamePlayer.getPlayer().setLastDamageCause(null);
    }

    public GamePlayer getGamePlayer(Player player) {
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getPlayer() == player) {
                return gamePlayer;
            }
        }
        return null;
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

    public GamePlayer[] getLivingPlayers(Team team) {
        List<GamePlayer> list = new ArrayList<>();
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getStatus().canInteract() && game.getGameMode().getTeam(gamePlayer) == team) {
                list.add(gamePlayer);
            }
        }
        return list.toArray(new GamePlayer[list.size()]);
    }

    public GamePlayer[] getNearbyEnemyPlayers(Team team, Location location, double range) {
        List<GamePlayer> list = new ArrayList<>();
        for (Entity entity : game.getArena().getWorld().getNearbyEntities(location, range, range, range)) {
            if (entity instanceof Player) {
                GamePlayer other = getGamePlayer((Player) entity);
                if (other != null && game.getGameMode().getTeam(other) != team) {
                    list.add(other);
                }
            }
        }
        return list.toArray(new GamePlayer[list.size()]);
    }

    public GamePlayer[] getNearbyPlayers(Location location, double range) {
        List<GamePlayer> list = new ArrayList<>();
        for (Entity entity : game.getArena().getWorld().getNearbyEntities(location, range, range, range)) {
            if (entity instanceof Player) {
                GamePlayer gamePlayer = getGamePlayer((Player) entity);
                if (gamePlayer != null) {
                    list.add(gamePlayer);
                }
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
            if (gamePlayer != null
                    && gamePlayer.getStatus().canInteract()
                    && location.getWorld() == gamePlayer.getPlayer().getWorld()
                    && location.distanceSquared(gamePlayer.getPlayer().getLocation()) < distance) {
                distance = location.distanceSquared(gamePlayer.getPlayer().getLocation());
                nearestPlayer = gamePlayer;
            }
        }
        return nearestPlayer;
    }

    public GamePlayer getNearestPlayer(Location location, Team team) {
        return getNearestPlayer(location, team, Double.MAX_VALUE);
    }

    public GamePlayer getNearestPlayer(Location location, Team team, double range) {
        double distance = range;
        GamePlayer nearestPlayer = null;
        for (GamePlayer gamePlayer : team.getPlayers()) {
            if (gamePlayer != null
                    && gamePlayer.getStatus().isAlive()
                    && game.getGameMode().getTeam(gamePlayer) == team
                    && location.getWorld() == gamePlayer.getPlayer().getWorld()
                    && location.distanceSquared(gamePlayer.getPlayer().getLocation()) < distance) {
                distance = location.distanceSquared(gamePlayer.getPlayer().getLocation());
                nearestPlayer = gamePlayer;
            }
        }
        return nearestPlayer;
    }

    public void onPlayerMove(Player player, Location from, Location to) {
        Arena arena = game.getArena();

        if (!arena.contains(player.getLocation())) {
            player.teleport(player.getLocation().add(from.toVector().subtract(to.toVector()).normalize()));
            ActionBar.LEAVE_ARENA.send(player);
        }

        if (game.getState().isAllowMove() || from.getX() == to.getX() && from.getZ() == to.getZ()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Spawn spawn = arena.getSpawn(gamePlayer) != null ? arena.getSpawn(gamePlayer) : arena.getTeamBase(game.getGameMode().getTeam(gamePlayer));

        if (spawn == null) {
            return;
        }

        Location location = spawn.getLocation();
        location.setPitch(player.getLocation().getPitch());
        location.setYaw(player.getLocation().getYaw());
        player.teleport(location);
    }

    public void preparePlayer(GamePlayer gamePlayer) {
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
                        .setColor(game.getGameMode().getTeam(gamePlayer).getColor())
                        .setDisplayName(ChatColor.WHITE + EnumMessage.ARMOR_VEST.getMessage())
                        .setUnbreakable(true)
                        .build(),
                new ItemStackBuilder(Material.LEATHER_HELMET)
                        .addItemFlags(ItemFlag.values())
                        .setColor(game.getGameMode().getTeam(gamePlayer).getColor())
                        .setDisplayName(ChatColor.WHITE + EnumMessage.ARMOR_HELMET.getMessage())
                        .setUnbreakable(true)
                        .build()
        });

        Item selectLoadout = new SelectLoadout(game);
        game.getItemRegistry().addItem(selectLoadout);
        gamePlayer.getHeldItems().add(selectLoadout);
        player.getInventory().setItem(ItemSlot.MISCELLANEOUS.getSlot(), selectLoadout.getItemStack());
    }

    public void receivePlayerChat(Player player, String message) {
        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Team team = game.getGameMode().getTeam(gamePlayer);

        broadcastMessage(EnumMessage.PLAYER_MESSAGE.getMessage(
                new Placeholder("bg_message", message),
                new Placeholder("player_name", team.getChatColor() + player.getName() + ChatColor.WHITE)));
    }

    public boolean removePlayer(GamePlayer gamePlayer) {
        players.remove(gamePlayer);

        broadcastMessage(EnumMessage.PLAYER_LEAVE.getMessage(
                new Placeholder("player_name", gamePlayer.getName()),
                new Placeholder("bg_players", players.size()),
                new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers())));

        gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        gamePlayer.getPlayer().teleport(game.getSpawnPoint());
        gamePlayer.getSavedInventory().restore(gamePlayer.getPlayer());
        gamePlayer.setStatus(PlayerStatus.ACTIVE).apply(game, gamePlayer);

        if (game.getState().isInProgress()) {
            playerStorage.addPlayerAttributes(gamePlayer);
        }

        game.getGameMode().removePlayer(gamePlayer);
        game.updateSign();
        return !players.contains(gamePlayer);
    }

    public void respawnPlayer(GamePlayer gamePlayer, Spawn spawn) {
        changeLoadout(gamePlayer, gamePlayer.getLoadout(), true);
        spawn.setGamePlayer(gamePlayer);

        new BattleRunnable() {
            public void run() {
                spawn.setGamePlayer(null); // Wait 5 seconds before resetting the spawn state
            }
        }.runTaskLater(100);
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

    public void setVisible(GamePlayer gamePlayer, Team team, boolean visible) {
        for (GamePlayer other : team.getPlayers()) {
            if (visible) {
                other.getPlayer().showPlayer(gamePlayer.getPlayer());
            } else {
                other.getPlayer().hidePlayer(gamePlayer.getPlayer());
            }
        }
    }

    public void updateExpBar(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        StoredPlayer storedPlayer = playerStorage.getStoredPlayer(player.getUniqueId());

        int exp = storedPlayer.getExp() + gamePlayer.getExp(), level = levelConfig.getLevel(exp);

        player.setExp(levelConfig.getExpBar(exp));
        player.setLevel(level);
    }
}