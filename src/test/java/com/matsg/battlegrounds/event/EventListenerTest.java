package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import org.bukkit.Server;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.PluginManager;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class EventListenerTest {

    @Test
    public void testEventDispatching() {
        // Create dependency mocks
        Battlegrounds plugin = mock(Battlegrounds.class);
        EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        PluginManager pluginManager = mock(PluginManager.class);
        Server server = mock(Server.class);

        when(plugin.getEventDispatcher()).thenReturn(eventDispatcher);
        when(plugin.getServer()).thenReturn(server);
        when(server.getPluginManager()).thenReturn(pluginManager);

        EventListener listener = new EventListener(plugin);

        // Test AsyncPlayerChatEvent dispatching
        AsyncPlayerChatEvent asyncPlayerChatEvent = mock(AsyncPlayerChatEvent.class);
        listener.onChat(asyncPlayerChatEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(asyncPlayerChatEvent);

        // Test BlockBreakEvent dispatching
        BlockBreakEvent blockBreakEvent = mock(BlockBreakEvent.class);
        listener.onBlockBreak(blockBreakEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(blockBreakEvent);

        // Test BlockPlaceEvent dispatching
        BlockPlaceEvent blockPlaceEvent = mock(BlockPlaceEvent.class);
        listener.onBlockPlace(blockPlaceEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(blockPlaceEvent);

        // Test BlockPhysicsEvent dispatching
        BlockPhysicsEvent blockPhysicsEvent = mock(BlockPhysicsEvent.class);
        listener.onBlockUpdate(blockPhysicsEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(blockPhysicsEvent);

        // Test EntityDamageByEntityEvent dispatching
        EntityDamageByEntityEvent entityDamageByEntityEvent = mock(EntityDamageByEntityEvent.class);
        listener.onPlayerDamageByPlayer(entityDamageByEntityEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(entityDamageByEntityEvent);

        // Test FoodLevelChangeEvent dispatching
        FoodLevelChangeEvent foodLevelChangeEvent = mock(FoodLevelChangeEvent.class);
        listener.onPlayerFoodLevelChange(foodLevelChangeEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(foodLevelChangeEvent);

        // Test InventoryClickEvent dispatching
        InventoryClickEvent inventoryClickEvent = mock(InventoryClickEvent.class);
        listener.onViewItemClick(inventoryClickEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(inventoryClickEvent);

        // Test InventoryCloseEvent dispatching
        InventoryCloseEvent inventoryCloseEvent = mock(InventoryCloseEvent.class);
        listener.onViewItemClose(inventoryCloseEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(inventoryCloseEvent);

        // Test PlayerCommandPreprocessEvent dispatching
        PlayerCommandPreprocessEvent playerCommandPreprocessEvent = mock(PlayerCommandPreprocessEvent.class);
        listener.onCommandSend(playerCommandPreprocessEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerCommandPreprocessEvent);

        // Test PlayerDeathEvent dispatching
        PlayerDeathEvent playerDeathEvent = mock(PlayerDeathEvent.class);
        listener.onPlayerDeath(playerDeathEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerDeathEvent);

        // Test PlayerDropItemEvent dispatching
        PlayerDropItemEvent playerDropItemEvent = mock(PlayerDropItemEvent.class);
        listener.onPlayerItemDrop(playerDropItemEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerDropItemEvent);

        // Test PlayerInteractEvent dispatching
        PlayerInteractEvent playerInteractEvent = mock(PlayerInteractEvent.class);
        listener.onPlayerInteract(playerInteractEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerInteractEvent);

        // Test PlayerItemHeldEvent dispatching
        PlayerItemHeldEvent playerItemHeldEvent = mock(PlayerItemHeldEvent.class);
        listener.onItemSwitch(playerItemHeldEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerItemHeldEvent);

        // Test PlayerJoinEvent dispatching
        PlayerJoinEvent playerJoinEvent = mock(PlayerJoinEvent.class);
        listener.onPlayerJoin(playerJoinEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerJoinEvent);

        // Test PlayerKickEvent dispatching
        PlayerKickEvent playerKickEvent = mock(PlayerKickEvent.class);
        listener.onPlayerKick(playerKickEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerKickEvent);

        // Test PlayerMoveEvent dispatching
        PlayerMoveEvent playerMoveEvent = mock(PlayerMoveEvent.class);
        listener.onPlayerMove(playerMoveEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerMoveEvent);

        // Test PlayerPickupItemEvent dispatching
        PlayerPickupItemEvent playerPickupItemEvent = mock(PlayerPickupItemEvent.class);
        listener.onPlayerItemPickUp(playerPickupItemEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerPickupItemEvent);

        // Test PlayerQuitEvent dispatching
        PlayerQuitEvent playerQuitEvent = mock(PlayerQuitEvent.class);
        listener.onPlayerQuit(playerQuitEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerQuitEvent);

        // Test PlayerRespawnEvent dispatching
        PlayerRespawnEvent playerRespawnEvent = mock(PlayerRespawnEvent.class);
        listener.onRespawn(playerRespawnEvent);
        verify(eventDispatcher, times(1)).dispatchEvent(playerRespawnEvent);
    }
}
