package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class GameEventListener implements Listener {

    private Battlegrounds plugin;

    public GameEventListener(Battlegrounds plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void handleEvent(Event event) {
        plugin.getEventManager().handleEvent(event);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        plugin.getEventManager().handleEvent(event);
    }

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onGamePlayerDeath(GamePlayerDeathEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onGamePlayerKill(GamePlayerKillPlayerEvent event) {
        event.getGame().getGameMode().onKill(event.getGamePlayer(), event.getKiller(), event.getWeapon(), event.getHitbox());
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerItemPickUp(PlayerPickupItemEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        handleEvent(event);
    }
}
