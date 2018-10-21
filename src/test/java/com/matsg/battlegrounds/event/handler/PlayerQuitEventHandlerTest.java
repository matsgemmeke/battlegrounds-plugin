package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerQuitEvent.class)
public class PlayerQuitEventHandlerTest {

    private Battlegrounds plugin;
    private GameManager gameManager;
    private Player player;
    private PlayerQuitEvent event;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.gameManager = mock(GameManager.class);
        this.player = mock(Player.class);
        this.event = PowerMockito.mock(PlayerQuitEvent.class);

        when(event.getPlayer()).thenReturn(player);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testPlayerQuitWhileInGame() {
        Game game = mock(Game.class);
        GamePlayer gamePlayer = mock(GamePlayer.class);
        PlayerManager playerManager = mock(PlayerManager.class);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(gameManager.getGame(player)).thenReturn(game);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerQuitEventHandler eventHandler = new PlayerQuitEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).removePlayer(gamePlayer);
    }

    @Test
    public void testPlayerQuitWhileNotInGame() {
        when(gameManager.getGame(player)).thenReturn(null);

        PlayerQuitEventHandler eventHandler = new PlayerQuitEventHandler(plugin);
        eventHandler.handle(event);
    }
}
