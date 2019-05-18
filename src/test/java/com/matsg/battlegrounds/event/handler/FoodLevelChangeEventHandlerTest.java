package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FoodLevelChangeEventHandlerTest {

    private Battlegrounds plugin;
    private FoodLevelChangeEvent event;
    private Game game;
    private GameManager gameManager;
    private Player player;
    private PlayerManager playerManager;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);

        this.event = new FoodLevelChangeEvent(player, 0);
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testFoodChangeWhenPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(new BattleGamePlayer(player, null));

        FoodLevelChangeEventHandler eventHandler = new FoodLevelChangeEventHandler(plugin);
        eventHandler.handle(event);

        assertTrue(event.isCancelled());
    }

    @Test
    public void testFoodChangeWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        FoodLevelChangeEventHandler eventHandler = new FoodLevelChangeEventHandler(plugin);
        eventHandler.handle(event);

        assertFalse(event.isCancelled());
    }
}
