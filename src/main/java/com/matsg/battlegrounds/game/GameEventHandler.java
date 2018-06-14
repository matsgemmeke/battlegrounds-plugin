package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.item.Item;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.api.item.Knife;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class GameEventHandler implements EventHandler {

    private Battlegrounds plugin;
    private Game game;

    public GameEventHandler(Battlegrounds plugin, Game game) {
        this.plugin = plugin;
        this.game = game;

        plugin.getEventManager().registerEventHandler(this);
    }

    private boolean isPlaying(Player player) {
        Game game = plugin.getGameManager().getGame(player);
        return game != null && game == this.game;
    }

    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player)) {
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                event.getRecipients().remove(gamePlayer.getPlayer());
            }
            return;
        }

        event.setCancelled(true);
        game.getPlayerManager().receivePlayerChat(player, event.getMessage());

        if (plugin.getBattlegroundsConfig().broadcastChat) {
            plugin.getLogger().info("[Game " + game.getId() + "] " + player.getName() + ": " + event.getMessage());
        }
    }

    public void onGamePlayerDeath(GamePlayerDeathEvent event) {
        event.getGame().broadcastMessage(event.getDeathCause().getDeathMessage());
    }

    public void onGamePlayerKill(GamePlayerKillPlayerEvent event) {
        event.getGame().getGameMode().onKill(event.getGamePlayer(), event.getKiller(), event.getWeapon(), event.getHitbox());
    }

    public void onItemSwitch(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player) || game == null || !game.getState().isInProgress()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Weapon weapon = gamePlayer.getLoadout().getWeapon(player.getInventory().getItemInMainHand());

        if (weapon == null) {
            return;
        }

        weapon.onSwitch();
    }

    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        event.setCancelled(isPlaying((Player) event.getEntity()) && !game.getState().isInProgress());
    }

    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        boolean differentGame = plugin.getGameManager().getGame((Player) event.getDamager()) != game;

        if (!isPlaying(player) || game == null || differentGame) {
            event.setCancelled(differentGame);
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player), damager = game.getPlayerManager().getGamePlayer((Player) event.getDamager());

        if (gamePlayer == null || damager == null || damager.getLoadout() == null) {
            event.setCancelled(true);
            return;
        }

        Knife knife = (Knife) damager.getLoadout().getWeapon(ItemSlot.KNIFE);
        Team team = game.getGameMode().getTeam(gamePlayer);

        event.setCancelled(team != null && team == game.getGameMode().getTeam(damager)
                || !(damager.getLoadout().getWeapon(damager.getPlayer().getInventory().getItemInMainHand()) instanceof Knife));
        event.setDamage(0.0);

        knife.damage(gamePlayer);
    }

    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!isPlaying(player) || game == null) {
            return;
        }

        event.setDeathMessage(null);
        event.setKeepInventory(true);

        DeathCause deathCause = DeathCause.fromDamageCause(player.getLastDamageCause().getCause());

        if (deathCause == null) {
            return; // Only notify the game of death events the game should handle
        }

        plugin.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(game, game.getPlayerManager().getGamePlayer(player), deathCause));
    }

    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(isPlaying((Player) event.getEntity()));
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player) || game == null || game.getArena() == null || !game.getState().isAllowItems()) {
            return;
        }

        event.setCancelled(true);

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Item item = game.getItemRegistry().getItemIgnoreMetadata(gamePlayer, player.getInventory().getItemInMainHand());

        game.getItemRegistry().interact(item, event.getAction());
    }

    public void onPlayerItemPickUp(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player) || game == null) {
            return;
        }

        game.getPlayerManager().onPlayerMove(player, event.getFrom(), event.getTo());
    }

    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (!isPlaying(player) || game == null || !game.getState().isInProgress()) {
            return;
        }

        GamePlayer gamePlayer = game.getPlayerManager().getGamePlayer(player);
        Spawn spawn = game.getGameMode().getRespawnPoint(gamePlayer);

        game.getPlayerManager().respawnPlayer(gamePlayer, spawn);
        event.setRespawnLocation(spawn.getLocation());
    }
}