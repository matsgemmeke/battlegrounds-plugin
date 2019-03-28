package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.EventChannel;
import com.matsg.battlegrounds.event.handler.*;
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

    private EventDispatcher eventDispatcher;

    public EventListener(Battlegrounds plugin) {
        this.eventDispatcher = plugin.getEventDispatcher();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        eventDispatcher.registerEventChannel(AsyncPlayerChatEvent.class, new EventChannel<>(
                new AsyncPlayerChatEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(BlockBreakEvent.class, new EventChannel<>(
                new BlockBreakEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(BlockPhysicsEvent.class, new EventChannel<>(
                new BlockPhysicsEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(BlockPlaceEvent.class, new EventChannel<>(
                new BlockPlaceEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(EntityDamageByEntityEvent.class, new EventChannel<>(
                new EntityDamageByEntityEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(FoodLevelChangeEvent.class, new EventChannel<>(
                new FoodLevelChangeEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(InventoryClickEvent.class, new EventChannel<>(
                new InventoryClickEventHandler()
        ));
        eventDispatcher.registerEventChannel(InventoryCloseEvent.class, new EventChannel<>(
                new InventoryCloseEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerCommandPreprocessEvent.class, new EventChannel<>(
                new PlayerCommandPreprocessEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerDeathEvent.class, new EventChannel<>(
                new PlayerDeathEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerDropItemEvent.class, new EventChannel<>(
                new PlayerDropItemEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerInteractEvent.class, new EventChannel<>(
                new PlayerInteractEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerItemHeldEvent.class, new EventChannel<>(
                new PlayerItemHeldEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerJoinEvent.class, new EventChannel<>(
                new PlayerJoinEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerKickEvent.class, new EventChannel<>(
                new PlayerKickEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerMoveEvent.class, new EventChannel<>(
                new PlayerMoveEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerPickupItemEvent.class, new EventChannel<>(
                new PlayerPickupItemEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerQuitEvent.class, new EventChannel<>(
                new PlayerQuitEventHandler(plugin)
        ));
        eventDispatcher.registerEventChannel(PlayerRespawnEvent.class, new EventChannel<>(
                new PlayerRespawnEventHandler(plugin)
        ));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onBlockUpdate(BlockPhysicsEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerFoodLevelChange(FoodLevelChangeEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerItemPickUp(PlayerPickupItemEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onViewItemClick(InventoryClickEvent event) {
        dispatchEvent(event);
    }

    @EventHandler
    public void onViewItemClose(InventoryCloseEvent event) {
        dispatchEvent(event);
    }

    private void dispatchEvent(Event event) {
        eventDispatcher.dispatchEvent(event);
    }
}
