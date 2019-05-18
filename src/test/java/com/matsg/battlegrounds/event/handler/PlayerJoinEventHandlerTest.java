package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.storage.PlayerStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PlayerJoinEventHandlerTest {

    private Battlegrounds plugin;
    private Player player;
    private PlayerJoinEvent event;
    private PlayerStorage playerStorage;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.player = mock(Player.class);
        this.playerStorage = mock(PlayerStorage.class);

        this.event = new PlayerJoinEvent(player, null);

        when(plugin.getPlayerStorage()).thenReturn(playerStorage);
    }

    @Test
    public void testNewPlayerRegistration() {
        when(playerStorage.contains(player.getUniqueId())).thenReturn(false);

        PlayerJoinEventHandler eventHandler = new PlayerJoinEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerStorage, times(1)).registerPlayer(player);
        verify(playerStorage, times(0)).updatePlayer(player);
    }

    @Test
    public void testExistingPlayerUpdate() {
        when(playerStorage.contains(player.getUniqueId())).thenReturn(true);

        PlayerJoinEventHandler eventHandler = new PlayerJoinEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerStorage, times(0)).registerPlayer(player);
        verify(playerStorage, times(1)).updatePlayer(player);
    }
}
