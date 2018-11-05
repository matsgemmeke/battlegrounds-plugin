package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.player.BattleGamePlayer;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ BattlegroundsConfig.class, BattlegroundsPlugin.class, ChatColor.class, Message.class })
public class AsyncPlayerChatEventHandlerTest {

    private AsyncPlayerChatEvent event;
    private Battlegrounds plugin;
    private BattlegroundsConfig config;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private Team team;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.config = PowerMockito.mock(BattlegroundsConfig.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.translator = mock(Translator.class);

        this.event = new AsyncPlayerChatEvent(false, player, null, new HashSet<>());
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);
        this.team = new BattleTeam(1, "Team", null, null);

        gameManager.getGames().add(game);
        gamePlayer.setTeam(team);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(ChatColor.class);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(plugin.getGameManager()).thenReturn(gameManager);
        when(plugin.getTranslator()).thenReturn(translator);

        PowerMockito.mockStatic(Message.class);
    }

    @Test
    public void testRemoveRecipientsWhenNotPlaying() {
        List<GamePlayer> list = new ArrayList<>();

        list.add(gamePlayer);

        event.getRecipients().add(player);

        when(playerManager.getGamePlayer(player)).thenReturn(null);
        when(playerManager.getPlayers()).thenReturn(list);

        AsyncPlayerChatEventHandler eventHandler = new AsyncPlayerChatEventHandler(plugin);
        eventHandler.handle(event);

        assertEquals(0, event.getRecipients().size());
    }

    @Test
    public void testLoggerPrint() {
        config.broadcastChat = true;

        Logger logger = mock(Logger.class);

        when(plugin.getLogger()).thenReturn(logger);

        AsyncPlayerChatEventHandler eventHandler = new AsyncPlayerChatEventHandler(plugin);
        eventHandler.handle(event);

        verify(logger, times(1)).info(anyString());

        assertTrue(event.isCancelled());
    }

    @Test
    public void testNoLoggerPrint() {
        config.broadcastChat = false;

        Logger logger = mock(Logger.class);

        when(plugin.getLogger()).thenReturn(logger);

        AsyncPlayerChatEventHandler eventHandler = new AsyncPlayerChatEventHandler(plugin);
        eventHandler.handle(event);

        verify(logger, times(0)).info(anyString());

        assertTrue(event.isCancelled());
    }
}
