package com.matsg.battlegrounds.event;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.PluginManager;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PlayerSwapItemListenerTest {

    @Test
    public void eventHandling() {
        // Create dependency mocks
        Battlegrounds plugin = mock(Battlegrounds.class);
        EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        PluginManager pluginManager = mock(PluginManager.class);
        Server server = mock(Server.class);

        when(plugin.getEventDispatcher()).thenReturn(eventDispatcher);
        when(plugin.getServer()).thenReturn(server);
        when(server.getPluginManager()).thenReturn(pluginManager);

        PlayerSwapItemListener listener = new PlayerSwapItemListener(plugin);

        // Test PlayerSwapHandItemsEvent dispatching
        PlayerSwapHandItemsEvent playerSwapHandItemsEvent = mock(PlayerSwapHandItemsEvent.class);
        listener.onPlayerItemSwap(playerSwapHandItemsEvent);
        verify(eventDispatcher, times(1)).dispatchInternalEvent(playerSwapHandItemsEvent);
    }
}
