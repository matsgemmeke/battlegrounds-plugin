package com.matsg.battlegrounds.event.handler;

import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.player.PlayerStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerJoinEvent.class)
public class PlayerJoinEventHandlerTest {

    @Test
    public void testNewPlayerRegistration() {
        Battlegrounds plugin = mock(Battlegrounds.class);
        Player player = mock(Player.class);
        PlayerJoinEvent event = PowerMockito.mock(PlayerJoinEvent.class);
        PlayerStorage playerStorage = mock(PlayerStorage.class);

        when(event.getPlayer()).thenReturn(player);
        when(playerStorage.contains(player.getUniqueId())).thenReturn(false);
        when(plugin.getPlayerStorage()).thenReturn(playerStorage);

        PlayerJoinEventHandler eventHandler = new PlayerJoinEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerStorage, times(1)).registerPlayer(any(UUID.class), anyString());
        verify(playerStorage, times(1)).updatePlayer(player);
    }

    @Test
    public void testExistingPlayerUpdate() {
        Battlegrounds plugin = mock(Battlegrounds.class);
        Player player = mock(Player.class);
        PlayerJoinEvent event = PowerMockito.mock(PlayerJoinEvent.class);
        PlayerStorage playerStorage = mock(PlayerStorage.class);

        when(event.getPlayer()).thenReturn(player);
        when(playerStorage.contains(player.getUniqueId())).thenReturn(true);
        when(plugin.getPlayerStorage()).thenReturn(playerStorage);

        PlayerJoinEventHandler eventHandler = new PlayerJoinEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerStorage, times(0)).registerPlayer(any(UUID.class), anyString());
        verify(playerStorage, times(1)).updatePlayer(player);
    }
}
