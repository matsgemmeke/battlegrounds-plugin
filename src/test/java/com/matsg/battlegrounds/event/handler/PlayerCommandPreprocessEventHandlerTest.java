package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.player.BattleGamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BattlegroundsConfig.class, BattlegroundsPlugin.class, Translator.class})
public class PlayerCommandPreprocessEventHandlerTest {

    private Battlegrounds plugin;
    private BattlegroundsConfig config;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerCommandPreprocessEvent event;
    private PlayerManager playerManager;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.config = mock(BattlegroundsConfig.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Translator.class);

        this.event = new PlayerCommandPreprocessEvent(player, null, null);
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);

        List<String> commands = new ArrayList<>();
        commands.add("cmd");

        config.allowedCommands = commands;

        gameManager.getGames().add(game);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testCommandDispatchWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        PlayerCommandPreprocessEventHandler eventHandler = new PlayerCommandPreprocessEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(0)).sendMessage(anyString());

        assertFalse(event.isCancelled());
    }

    @Test
    public void testCommandDispatchAllCommandsAllowed() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        config.allowedCommands.add("*");

        PlayerCommandPreprocessEventHandler eventHandler = new PlayerCommandPreprocessEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(0)).sendMessage(anyString());

        assertFalse(event.isCancelled());
    }

    @Test
    public void testCommandDispatchAllowedCommand() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        event.setMessage("/cmd");

        PlayerCommandPreprocessEventHandler eventHandler = new PlayerCommandPreprocessEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(0)).sendMessage(anyString());

        assertFalse(event.isCancelled());
    }

    @Test
    public void testCommandDispatchDisallowedCommand() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        event.setMessage("/test");

        PlayerCommandPreprocessEventHandler eventHandler = new PlayerCommandPreprocessEventHandler(plugin);
        eventHandler.handle(event);

        verify(player, times(1)).sendMessage(anyString());

        assertTrue(event.isCancelled());
    }
}
