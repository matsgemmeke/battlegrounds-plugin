package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.EventManager;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.PluginManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class PlayerSwapItemListenerTest {

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

        PlayerSwapItemListener listener = new PlayerSwapItemListener(plugin);

        // Test PlayerSwapHandItemsEvent handling
        PlayerSwapHandItemsEvent playerSwapHandItemsEvent = mock(PlayerSwapHandItemsEvent.class);
        listener.onPlayerItemSwap(playerSwapHandItemsEvent);
        verify(eventManager, times(1)).handleEvent(playerSwapHandItemsEvent);
    }
}
