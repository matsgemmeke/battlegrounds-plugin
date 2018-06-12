package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.PlayerStatus;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import com.matsg.battlegrounds.player.BattleGamePlayer;
import com.matsg.battlegrounds.util.ActionBar;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BattlePlayerManager implements PlayerManager {

    private Game game;
    private List<GamePlayer> players;

    public BattlePlayerManager(Game game) {
        this.game = game;
        this.players = new ArrayList<>();
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public GamePlayer addPlayer(Player player) {
        GamePlayer gamePlayer = new BattleGamePlayer(player);
        Location lobby = game.getDataFile().getLocation("lobby");

        players.add(gamePlayer);

        game.broadcastMessage(EnumMessage.PLAYER_JOIN.getMessage(
                new Placeholder("player_name", player.getName()),
                new Placeholder("bg_players", players.size()),
                new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers())));
        game.getGameMode().addPlayer(gamePlayer);
        game.updateSign();

        gamePlayer.getPlayer().setScoreboard(new LobbyScoreboard(game).createScoreboard());
        gamePlayer.setStatus(PlayerStatus.ACTIVE).apply(game, gamePlayer);

        if (lobby != null) {
            player.teleport(lobby);
        }
        if (players.size() == game.getConfiguration().getMinPlayers()) {
            new LobbyCountdown(game, game.getConfiguration().getLobbyCountdown(), 60, 45, 30, 15, 10, 5).run();
        }
        return gamePlayer;
    }

    public void changeLoadout(GamePlayer gamePlayer, Loadout loadout, boolean apply) {
        gamePlayer.setLoadout(loadout);
        if (!apply) {
            gamePlayer.sendMessage(ActionBar.CHANGE_LOADOUT);
            return;
        }
        clearLoadout(gamePlayer.getLoadout());
        for (Weapon weapon : loadout.getWeapons()) {
            game.getItemRegistry().addItem(weapon);
            weapon.refillAmmo();
            weapon.setGame(game);
            weapon.setGamePlayer(gamePlayer);
            weapon.update();
        }
    }

    private void clearLoadout(Loadout loadout) {
        if (loadout == null) {
            return;
        }
        for (Weapon weapon : loadout.getWeapons()) {
            game.getItemRegistry().removeItem(weapon);
            weapon.remove();
            weapon.setGame(null);
            weapon.setGamePlayer(null);
        }
    }

    public void damagePlayer(GamePlayer gamePlayer, double damage) {
        if (gamePlayer == null || !gamePlayer.getStatus().isAlive() || gamePlayer.getPlayer().isDead()) {
            return;
        }
        double finalHealth = gamePlayer.getPlayer().getHealth() - damage;
        gamePlayer.getPlayer().damage(0.01); // Create a fake damage animation
        gamePlayer.getPlayer().setHealth(finalHealth > 0.0 ? finalHealth : 0); // It needs to set the health to 0 if the damage is greater than the health, else the api will complain
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

    public GamePlayer[] getNearbyEnemyPlayers(Game game, GamePlayer gamePlayer, double range) {
        List<GamePlayer> list = new ArrayList<>();
        Team team = game.getGameMode().getTeam(gamePlayer);
        for (Entity entity : game.getArena().getWorld().getNearbyEntities(gamePlayer.getLocation(), range, range, range)) {
            if (entity instanceof Player) {
                GamePlayer other = getGamePlayer((Player) entity);
                if (other != null && game.getGameMode().getTeam(other) == team) {
                    list.add(gamePlayer);
                }
            }
        }
        return list.toArray(new GamePlayer[list.size()]);
    }

    public GamePlayer[] getNearbyPlayers(Game game, Location location, double range) {
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

    public GamePlayer[] getNearbyPlayers(Location location, double range) {
        List<GamePlayer> list = new ArrayList<>();
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer != null && location.distanceSquared(gamePlayer.getLocation()) <= range) {
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

        Location location = game.getArena().getSpawn(game.getPlayerManager().getGamePlayer(player)).getLocation();
        location.setPitch(player.getLocation().getPitch());
        location.setYaw(player.getLocation().getYaw());
        player.teleport(location);
    }

    public void removePlayer(GamePlayer gamePlayer) {
        players.remove(gamePlayer);

        game.broadcastMessage(EnumMessage.PREFIX.getMessage() + " " + EnumMessage.PLAYER_LEAVE.getMessage(
                new Placeholder("player_name", gamePlayer.getName()),
                new Placeholder("bg_players", players.size()),
                new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers())));
        game.getGameMode().addPlayer(gamePlayer);
        game.updateSign();

        gamePlayer.getPlayer().teleport(game.getSpawnPoint());
        gamePlayer.getSavedInventory().restore(gamePlayer.getPlayer());
        gamePlayer.setStatus(PlayerStatus.ACTIVE).apply(game, gamePlayer);

        if (getLivingPlayers().length <= 0) {
            game.stop();
        }
    }

    public void respawnPlayer(GamePlayer gamePlayer, Spawn spawn) {
        game.getPlayerManager().changeLoadout(gamePlayer, gamePlayer.getLoadout(), true);

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
}