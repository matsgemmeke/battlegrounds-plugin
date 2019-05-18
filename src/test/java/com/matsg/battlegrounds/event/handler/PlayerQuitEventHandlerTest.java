package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PlayerQuitEventHandlerTest {

    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerQuitEvent event;
    private PlayerManager playerManager;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);

        this.event = new PlayerQuitEvent(player, null);
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);

        gameManager.getGames().add(game);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testPlayerQuitWhenPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        PlayerQuitEventHandler eventHandler = new PlayerQuitEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(1)).removePlayer(gamePlayer);
    }

    @Test
    public void testPlayerQuitWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        PlayerQuitEventHandler eventHandler = new PlayerQuitEventHandler(plugin);
        eventHandler.handle(event);

        verify(playerManager, times(0)).removePlayer(gamePlayer);
    }
}
