package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.entity.SavedInventory;
import com.matsg.battlegrounds.api.storage.LevelConfig;
import com.matsg.battlegrounds.api.storage.StatisticContext;
import com.matsg.battlegrounds.api.storage.StoredPlayer;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import com.matsg.battlegrounds.entity.BattleSavedInventory;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class BattlePlayerManager implements PlayerManager {

    private Game game;
    private LevelConfig levelConfig;
    private List<GamePlayer> players;
    private PlayerStorage playerStorage;
    private TaskRunner taskRunner;
    private Translator translator;

    public BattlePlayerManager(Game game, LevelConfig levelConfig, PlayerStorage playerStorage, TaskRunner taskRunner, Translator translator) {
        this.game = game;
        this.levelConfig = levelConfig;
        this.playerStorage = playerStorage;
        this.taskRunner = taskRunner;
        this.translator = translator;
        this.players = new ArrayList<>();
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
        SavedInventory savedInventory = new BattleSavedInventory(player, player.getInventory());
        GamePlayer gamePlayer = new BattleGamePlayer(player, savedInventory);
        Location lobby = game.getLobby();

        players.add(gamePlayer);

        broadcastMessage(translator.translate(TranslationKey.PLAYER_JOIN.getPath(),
                new Placeholder("player_name", player.getName()),
                new Placeholder("bg_players", players.size()),
                new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers())));

        game.getGameMode().addPlayer(gamePlayer);
        game.updateSign();

        gamePlayer.getPlayer().setScoreboard(new LobbyScoreboard(game).createScoreboard());
        gamePlayer.setReturnLocation(player.getLocation());
        gamePlayer.setState(PlayerState.ACTIVE);
        gamePlayer.getState().apply(game, gamePlayer);

        updateExpBar(gamePlayer);

        if (lobby != null) {
            player.teleport(lobby);
        }

        if (game.getArena() != null && players.size() == game.getConfiguration().getMinPlayers()) {
            int countdownLength = game.getConfiguration().getLobbyCountdown();
            int[] displayNumbers = new int[] { 60, 45, 30, 15, 10, 5 };

            Countdown countdown = new LobbyCountdown(game, translator, countdownLength, displayNumbers);

            taskRunner.runTaskTimer(countdown, 0, 20);

            game.setCountdown(countdown);
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
            if (gamePlayer.getState().canInteract()) {
                list.add(gamePlayer);
            }
        }
        return list.toArray(new GamePlayer[list.size()]);
    }

    public GamePlayer[] getLivingPlayers(Team team) {
        List<GamePlayer> list = new ArrayList<>();
        for (GamePlayer gamePlayer : players) {
            if (gamePlayer.getState().canInteract() && game.getGameMode().getTeam(gamePlayer) == team) {
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
                    && gamePlayer.getState().canInteract()
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
                    && gamePlayer.getState().isAlive()
                    && game.getGameMode().getTeam(gamePlayer) == team
                    && location.getWorld() == gamePlayer.getPlayer().getWorld()
                    && location.distanceSquared(gamePlayer.getPlayer().getLocation()) < distance) {
                distance = location.distanceSquared(gamePlayer.getPlayer().getLocation());
                nearestPlayer = gamePlayer;
            }
        }
        return nearestPlayer;
    }

    public void givePoints(int points) {
        for (GamePlayer gamePlayer : players) {
            givePoints(gamePlayer, points);
        }
    }

    public void givePoints(GamePlayer gamePlayer, int points) {
        ActionBar actionBar = points >= 0 ? ActionBar.POINTS_INCREASE : ActionBar.POINTS_DEDUCT;
        actionBar.send(gamePlayer.getPlayer(), new Placeholder("bg_points", points));

        gamePlayer.setPoints(gamePlayer.getPoints() + points);
    }

    public boolean removePlayer(GamePlayer gamePlayer) {
        players.remove(gamePlayer);

        broadcastMessage(translator.translate(TranslationKey.PLAYER_LEAVE.getPath(),
                new Placeholder("player_name", gamePlayer.getName()),
                new Placeholder("bg_players", players.size()),
                new Placeholder("bg_maxplayers", game.getConfiguration().getMaxPlayers())));

        gamePlayer.getSavedInventory().restore(gamePlayer.getPlayer());
        gamePlayer.setState(PlayerState.ACTIVE);
        gamePlayer.getState().apply(game, gamePlayer);
        gamePlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        gamePlayer.getPlayer().teleport(gamePlayer.getReturnLocation());

        if (game.getState().isInProgress()) {
            StatisticContext context = new StatisticContext();
            context.setGame(game);
            context.setPlayer(gamePlayer);

            playerStorage.getStoredPlayer(gamePlayer.getUUID()).addStatisticAttributes(context);
        }

        if (game.getPlayerManager().getPlayers().size() < game.getConfiguration().getMinPlayers()) {
            game.stop();
        }

        game.getGameMode().removePlayer(gamePlayer);
        game.updateSign();
        return !players.contains(gamePlayer);
    }

    public void respawnPlayer(GamePlayer gamePlayer, Spawn spawn) {
        if (!gamePlayer.getLoadout().equals(gamePlayer.getSelectedLoadout())) {
            changeLoadout(gamePlayer, gamePlayer.getSelectedLoadout().clone(), true);
        }

        spawn.setOccupant(gamePlayer);

        // Wait 5 seconds before resetting the spawn state
        taskRunner.runTaskLater(() -> spawn.setOccupant(null), 100);
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
