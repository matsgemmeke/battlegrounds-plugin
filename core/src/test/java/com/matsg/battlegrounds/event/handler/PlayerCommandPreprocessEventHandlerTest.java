package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerCommandPreprocessEventHandlerTest {

    private Battlegrounds plugin;
    private BattlegroundsConfig config;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerCommandPreprocessEvent event;
    private PlayerManager playerManager;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.config = mock(BattlegroundsConfig.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.translator = mock(Translator.class);

        this.event = new PlayerCommandPreprocessEvent(player, null, null);
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);

        List<String> commands = new ArrayList<>();
        commands.add("cmd");

        config.allowedCommands = commands;

        gameManager.getGames().add(game);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void commandDispatchWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        PlayerCommandPreprocessEventHandler eventHandler = new PlayerCommandPreprocessEventHandler(plugin, translator);
        eventHandler.handle(event);

        verify(player, times(0)).sendMessage(anyString());

        assertFalse(event.isCancelled());
    }

    @Test
    public void commandDispatchAllCommandsAllowed() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        config.allowedCommands.add("*");

        PlayerCommandPreprocessEventHandler eventHandler = new PlayerCommandPreprocessEventHandler(plugin, translator);
        eventHandler.handle(event);

        verify(player, times(0)).sendMessage(anyString());

        assertFalse(event.isCancelled());
    }

    @Test
    public void commandDispatchAllowedCommand() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        event.setMessage("/cmd");

        PlayerCommandPreprocessEventHandler eventHandler = new PlayerCommandPreprocessEventHandler(plugin, translator);
        eventHandler.handle(event);

        verify(player, times(0)).sendMessage(anyString());

        assertFalse(event.isCancelled());
    }

    @Test
    public void commandDispatchDisallowedCommand() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        event.setMessage("/test");

        PlayerCommandPreprocessEventHandler eventHandler = new PlayerCommandPreprocessEventHandler(plugin, translator);
        eventHandler.handle(event);

        verify(player, times(1)).sendMessage(anyString());

        assertTrue(event.isCancelled());
    }
}
