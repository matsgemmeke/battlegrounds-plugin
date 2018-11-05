package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.gui.View;
import org.bukkit.Server;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class InventoryCloseEventHandlerTest {

    private Battlegrounds plugin;
    private BukkitScheduler scheduler;
    private Inventory inventory;
    private InventoryCloseEvent event;
    private Server server;
    private View view;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.scheduler = mock(BukkitScheduler.class);
        this.inventory = mock(Inventory.class);
        this.event = mock(InventoryCloseEvent.class);
        this.server = mock(Server.class);
        this.view = mock(View.class);

        when(event.getInventory()).thenReturn(inventory);
        when(plugin.getServer()).thenReturn(server);
        when(server.getScheduler()).thenReturn(scheduler);
    }

    @Test
    public void testInventoryCloseNoView() {
        InventoryCloseEventHandler eventHandler = new InventoryCloseEventHandler(plugin);
        eventHandler.handle(event);

        verify(scheduler, times(0)).scheduleSyncDelayedTask(any(Battlegrounds.class), any(Runnable.class), anyLong());
    }

    @Test
    public void testInventoryCloseWithViewAllowClose() {
        when(inventory.getHolder()).thenReturn(view);
        when(view.onClose()).thenReturn(true);

        InventoryCloseEventHandler eventHandler = new InventoryCloseEventHandler(plugin);
        eventHandler.handle(event);

        verify(scheduler, times(0)).scheduleSyncDelayedTask(any(Battlegrounds.class), any(Runnable.class), anyLong());
    }

    @Test
    public void testInventoryCloseWithViewDisallowClose() {
        when(inventory.getHolder()).thenReturn(view);
        when(view.onClose()).thenReturn(false);

        InventoryCloseEventHandler eventHandler = new InventoryCloseEventHandler(plugin);
        eventHandler.handle(event);

        // verify(player, times(1)).openInventory(inventory);
        verify(scheduler, times(1)).scheduleSyncDelayedTask(any(Battlegrounds.class), any(Runnable.class), anyLong());
    }
}
