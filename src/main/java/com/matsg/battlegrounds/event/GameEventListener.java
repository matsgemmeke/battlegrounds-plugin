package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

public class GameEventListener implements Listener {

    private Battlegrounds plugin;
    private Game game;

    public GameEventListener(Battlegrounds plugin, Game game) {
        this.plugin = plugin;
        this.game = game;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private boolean isPlaying(Player player) {
        Game game = plugin.getGameManager().getGame(player);
        return game != null && game == this.game;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!isPlaying(event.getPlayer())) {
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                event.getRecipients().remove(gamePlayer.getPlayer());
            }
            return;
        }
        event.setCancelled(game.getEventHandler().handleEvent(event));
    }

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        if (!isPlaying(event.getPlayer())) {
            return;
        }
        event.setCancelled(game.getEventHandler().handleEvent(event));
    }

    @EventHandler
    public void onGamePlayerDeath(GamePlayerDeathEvent event) {
        if (event.getGame() != game) {
            return;
        }
        game.getGameMode().onDeath(event.getGamePlayer(), event.getDeathCause());
    }

    @EventHandler
    public void onGamePlayerKill(GamePlayerKillPlayerEvent event) {
        if (event.getGame() != game) {
            return;
        }
        game.getGameMode().onKill(event.getGamePlayer(), event.getKiller(), event.getWeapon(), event.getHitbox());
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {
        if (!isPlaying(event.getPlayer())) {
            return;
        }
        event.setCancelled(game.getEventHandler().handleEvent(event));
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        event.setCancelled(isPlaying((Player) event.getEntity()) && !game.getState().isInProgress());
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!isPlaying((Player) event.getEntity())) {
            return;
        }
        event.setCancelled(game.getEventHandler().handleEvent(event));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!isPlaying(event.getEntity())) {
            return;
        }
        game.getEventHandler().handleEvent(event);
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(isPlaying((Player) event.getEntity()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isPlaying(event.getPlayer())) {
            return;
        }
        event.setCancelled(game.getEventHandler().handleEvent(event));
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        if (!isPlaying(event.getPlayer())) {
            return;
        }
        event.setCancelled(game.getEventHandler().handleEvent(event));
    }

    @EventHandler
    public void onPlayerItemPickUp(PlayerPickupItemEvent event) {
        if (!isPlaying(event.getPlayer())) {
            return;
        }
        event.setCancelled(game.getEventHandler().handleEvent(event));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isPlaying(event.getPlayer())) {
            return;
        }
        event.setCancelled(game.getEventHandler().handleEvent(event));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (!isPlaying(event.getPlayer()) || !game.getState().isInProgress()) {
            return;
        }
        game.getEventHandler().handleEvent(event);
    }
}