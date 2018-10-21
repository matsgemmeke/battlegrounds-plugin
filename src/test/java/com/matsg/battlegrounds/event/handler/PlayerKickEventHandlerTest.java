package com.matsg.battlegrounds.event.handler;

import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerKickEvent.class)
public class PlayerKickEventHandlerTest {

    private Battlegrounds plugin;
    private GameManager gameManager;
    private Player player;
    private PlayerKickEvent event;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.gameManager = mock(GameManager.class);
        this.player = mock(Player.class);
        this.event = PowerMockito.mock(PlayerKickEvent.class);

        when(event.getPlayer()).thenReturn(player);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testPlayerKickWhileInGame() {
        Game game = mock(Game.class);
        GamePlayer gamePlayer = mock(GamePlayer.class);
        PlayerManager playerManager = mock(PlayerManager.class);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(gameManager.getGame(player)).thenReturn(game);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerKickEventHandler eventHandler = new PlayerKickEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).removePlayer(gamePlayer);
    }

    @Test
    public void testPlayerKickWhileNotInGame() {
        when(gameManager.getGame(player)).thenReturn(null);

        PlayerKickEventHandler eventHandler = new PlayerKickEventHandler(plugin);
        eventHandler.handle(event);
    }
}
