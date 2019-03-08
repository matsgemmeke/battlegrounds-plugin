package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.TranslationKey;
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
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import com.matsg.battlegrounds.item.ItemStackBuilder;
import com.matsg.battlegrounds.item.SelectLoadout;
import com.matsg.battlegrounds.player.BattleGamePlayer;
import com.matsg.battlegrounds.player.BattleSavedInventory;
import com.matsg.battlegrounds.util.ActionBar;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.*;
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
    private MessageHelper messageHelper;
    private PlayerStorage playerStorage;

    public BattlePlayerManager(Game game, LevelConfig levelConfig, PlayerStorage playerStorage) {
        this.game = game;
        this.levelConfig = levelConfig;
        this.messageHelper = new MessageHelper();
        this.players = new ArrayList<>();
        this.playerStorage = playerStorage;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    private void addLoadout(GamePlayer gamePlayer, Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null) {
                game.getItemRegistry().addItem(weapon);
                weapon.setGame(game);
                weapon.setGamePlayer(gamePlayer);
                weapon.resetState();
                weapon.update();
            }
        }
    }

    public GamePlayer addPlayer(Player player) {
        GamePlayer gamePlayer = new BattleGamePlayer(player, new BattleSavedInventory(player));
        Location lobby = game.getDataFile().getLocation("lobby");

        players.add(gamePlayer);

        broadcastMessage(messageHelper.create(TranslationKey.PLAYER_JOIN,
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

    public void broadcastMessage(String message) {
        for (GamePlayer gamePlayer : players) {
            gamePlayer.sendMessage(message);
        }
    }

    public void changeLoadout(GamePlayer gamePlayer, Loadout loadout, boolean apply) {
        Loadout current = gamePlayer.getLoadout();
        if (!apply) {
            ActionBar.CHANGE_LOADOUT.send(gamePlayer.getPlayer());
            return;
        }
        if (current != null && current.equals(loadout)) {
            clearLoadout(current);
        }
        addLoadout(gamePlayer, loadout);
        gamePlayer.setLoadout(loadout);
    }

    private void clearLoadout(Loadout loadout) {
        for (Weapon weapon : loadout.getWeapons()) {
            if (weapon != null) {
                game.getItemRegistry().removeItem(game.getItemRegistry().getItem(weapon.getItemStack()));
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
        gamePlayer.getPlayer().setHealth(finalHealth > 0.0 ? finalHealth : 0); // Set the health to 0 if the damage is greater than the health
        gamePlayer.getPlayer().setLastDamageCause(null);
    }

    public void damagePlayer(GamePlayer gamePlayer, double damage, boolean effect) {
        if (effect) {
            gamePlayer.getLocation().getWorld().playEffect(gamePlayer.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        }
        damagePlayer(gamePlayer, damage);
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
                        .setDisplayName(ChatColor.WHITE + messageHelper.create(TranslationKey.ARMOR_VEST))
                        .setUnbreakable(true)
                        .build(),
                new ItemStackBuilder(Material.LEATHER_HELMET)
                        .addItemFlags(ItemFlag.values())
                        .setColor(game.getGameMode().getTeam(gamePlayer).getColor())
                        .setDisplayName(ChatColor.WHITE + messageHelper.create(TranslationKey.ARMOR_HELMET))
                        .setUnbreakable(true)
                        .build()
        });

        Item selectLoadout = new SelectLoadout(game);
        game.getItemRegistry().addItem(selectLoadout);
        gamePlayer.getHeldItems().add(selectLoadout);
        player.getInventory().setItem(ItemSlot.MISCELLANEOUS.getSlot(), selectLoadout.getItemStack());
    }

    public boolean removePlayer(GamePlayer gamePlayer) {
        players.remove(gamePlayer);

        broadcastMessage(messageHelper.create(TranslationKey.PLAYER_LEAVE,
                new Placeholder("player_name", gamePlayer.getName()),
                new Placeholder("bg_players", players.size()),
                new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers())));

        gamePlayer.getSavedInventory().restore(gamePlayer.getPlayer());
        gamePlayer.setStatus(PlayerStatus.ACTIVE).apply(game, gamePlayer);
        gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        gamePlayer.getPlayer().teleport(game.getSpawnPoint());

        if (game.getState().isInProgress()) {
            playerStorage.addPlayerAttributes(gamePlayer);
        }

        if (game.getPlayerManager().getPlayers().size() < game.getConfiguration().getMinPlayers()) {
            game.stop();
        }

        game.getGameMode().removePlayer(gamePlayer);
        game.updateSign();
        return !players.contains(gamePlayer);
    }

    public void respawnPlayer(GamePlayer gamePlayer, Spawn spawn) {
        changeLoadout(gamePlayer, gamePlayer.getSelectedLoadout().clone(), true);
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
