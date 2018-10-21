package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;

public class EventListener implements Listener {

    private Battlegrounds plugin;

    public EventListener(Battlegrounds plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void handleEvent(Event event) {
        plugin.getEventManager().handleEvent(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onBlockUpdate(BlockPhysicsEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        handleEvent(event);
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onViewItemClick(InventoryClickEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onViewItemClose(InventoryCloseEvent event) {
        handleEvent(event);
    }
}
