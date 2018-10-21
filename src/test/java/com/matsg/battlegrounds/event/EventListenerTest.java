package com.matsg.battlegrounds.event;

import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.EventManager;
import com.matsg.battlegrounds.game.BattleTeam;
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
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class EventListenerTest {

    @Test
    public void testEventHandling() {
        // Create dependency mocks
        Battlegrounds plugin = mock(Battlegrounds.class);
        EventManager eventManager = mock(EventManager.class);
        PluginManager pluginManager = mock(PluginManager.class);
        Server server = mock(Server.class);

        when(plugin.getEventManager()).thenReturn(eventManager);
        when(plugin.getServer()).thenReturn(server);
        when(server.getPluginManager()).thenReturn(pluginManager);

        EventListener listener = new EventListener(plugin);

        // Test AsyncPlayerChatEvent handling
        AsyncPlayerChatEvent asyncPlayerChatEvent = mock(AsyncPlayerChatEvent.class);
        listener.onChat(asyncPlayerChatEvent);
        verify(eventManager, times(1)).handleEvent(asyncPlayerChatEvent);

        // Test BlockBreakEvent handling
        BlockBreakEvent blockBreakEvent = mock(BlockBreakEvent.class);
        listener.onBlockBreak(blockBreakEvent);
        verify(eventManager, times(1)).handleEvent(blockBreakEvent);

        // Test BlockPlaceEvent handling
        BlockPlaceEvent blockPlaceEvent = mock(BlockPlaceEvent.class);
        listener.onBlockPlace(blockPlaceEvent);
        verify(eventManager, times(1)).handleEvent(blockPlaceEvent);

        // Test BlockPhysicsEvent handling
        BlockPhysicsEvent blockPhysicsEvent = mock(BlockPhysicsEvent.class);
        listener.onBlockUpdate(blockPhysicsEvent);
        verify(eventManager, times(1)).handleEvent(blockPhysicsEvent);

        // Test EntityDamageByEntityEvent handling
        EntityDamageByEntityEvent entityDamageByEntityEvent = mock(EntityDamageByEntityEvent.class);
        listener.onPlayerDamageByPlayer(entityDamageByEntityEvent);
        verify(eventManager, times(1)).handleEvent(entityDamageByEntityEvent);

        // Test FoodLevelChangeEvent handling
        FoodLevelChangeEvent foodLevelChangeEvent = mock(FoodLevelChangeEvent.class);
        listener.onPlayerFoodLevelChange(foodLevelChangeEvent);
        verify(eventManager, times(1)).handleEvent(foodLevelChangeEvent);

        // Test InventoryClickEvent handling
        InventoryClickEvent inventoryClickEvent = mock(InventoryClickEvent.class);
        listener.onViewItemClick(inventoryClickEvent);
        verify(eventManager, times(1)).handleEvent(inventoryClickEvent);

        // Test InventoryCloseEvent handling
        InventoryCloseEvent inventoryCloseEvent = mock(InventoryCloseEvent.class);
        listener.onViewItemClose(inventoryCloseEvent);
        verify(eventManager, times(1)).handleEvent(inventoryCloseEvent);

        // Test PlayerCommandPreprocessEvent handling
        PlayerCommandPreprocessEvent playerCommandPreprocessEvent = mock(PlayerCommandPreprocessEvent.class);
        listener.onCommandSend(playerCommandPreprocessEvent);
        verify(eventManager, times(1)).handleEvent(playerCommandPreprocessEvent);

        // Test PlayerDeathEvent handling
        PlayerDeathEvent playerDeathEvent = mock(PlayerDeathEvent.class);
        listener.onPlayerDeath(playerDeathEvent);
        verify(eventManager, times(1)).handleEvent(playerDeathEvent);

        // Test PlayerDropItemEvent handling
        PlayerDropItemEvent playerDropItemEvent = mock(PlayerDropItemEvent.class);
        listener.onPlayerItemDrop(playerDropItemEvent);
        verify(eventManager, times(1)).handleEvent(playerDropItemEvent);

        // Test PlayerInteractEvent handling
        PlayerInteractEvent playerInteractEvent = mock(PlayerInteractEvent.class);
        listener.onPlayerInteract(playerInteractEvent);
        verify(eventManager, times(1)).handleEvent(playerInteractEvent);

        // Test PlayerItemHeldEvent handling
        PlayerItemHeldEvent playerItemHeldEvent = mock(PlayerItemHeldEvent.class);
        listener.onItemSwitch(playerItemHeldEvent);
        verify(eventManager, times(1)).handleEvent(playerItemHeldEvent);

        // Test PlayerJoinEvent handling
        PlayerJoinEvent playerJoinEvent = mock(PlayerJoinEvent.class);
        listener.onPlayerJoin(playerJoinEvent);
        verify(eventManager, times(1)).handleEvent(playerJoinEvent);

        // Test PlayerKickEvent handling
        PlayerKickEvent playerKickEvent = mock(PlayerKickEvent.class);
        listener.onPlayerKick(playerKickEvent);
        verify(eventManager, times(1)).handleEvent(playerKickEvent);

        // Test PlayerMoveEvent handling
        PlayerMoveEvent playerMoveEvent = mock(PlayerMoveEvent.class);
        listener.onPlayerMove(playerMoveEvent);
        verify(eventManager, times(1)).handleEvent(playerMoveEvent);

        // Test PlayerPickupItemEvent handling
        PlayerPickupItemEvent playerPickupItemEvent = mock(PlayerPickupItemEvent.class);
        listener.onPlayerItemPickUp(playerPickupItemEvent);
        verify(eventManager, times(1)).handleEvent(playerPickupItemEvent);

        // Test PlayerQuitEvent handling
        PlayerQuitEvent playerQuitEvent = mock(PlayerQuitEvent.class);
        listener.onPlayerQuit(playerQuitEvent);
        verify(eventManager, times(1)).handleEvent(playerQuitEvent);

        // Test PlayerRespawnEvent handling
        PlayerRespawnEvent playerRespawnEvent = mock(PlayerRespawnEvent.class);
        listener.onRespawn(playerRespawnEvent);
        verify(eventManager, times(1)).handleEvent(playerRespawnEvent);
    }
}
