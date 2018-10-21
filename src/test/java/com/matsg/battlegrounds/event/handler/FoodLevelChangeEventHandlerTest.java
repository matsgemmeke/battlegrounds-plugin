package com.matsg.battlegrounds.event.handler;

import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class FoodLevelChangeEventHandlerTest {

    private Battlegrounds plugin;
    private FoodLevelChangeEvent event;
    private GameManager gameManager;
    private Player player;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.event = mock(FoodLevelChangeEvent.class);
        this.gameManager = mock(GameManager.class);
        this.player = mock(Player.class);

        when(plugin.getGameManager()).thenReturn(gameManager);
        when(event.getEntity()).thenReturn(player);
    }

    @Test
    public void testEventWhenPlayerIsInGame() {
        Game game = mock(Game.class);

        when(gameManager.getGame(player)).thenReturn(game);

        FoodLevelChangeEventHandler eventHandler = new FoodLevelChangeEventHandler(plugin);
        eventHandler.handle(event);

        verify(event, times(1)).setCancelled(true);
    }

    @Test
    public void testEventWhenPlayerIsNotInGame() {
        when(gameManager.getGame(player)).thenReturn(null);

        FoodLevelChangeEventHandler eventHandler = new FoodLevelChangeEventHandler(plugin);
        eventHandler.handle(event);

        verify(event, times(0)).setCancelled(true);
    }
}
