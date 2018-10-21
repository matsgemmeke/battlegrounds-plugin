package com.matsg.battlegrounds.event.handler;

import static org.mockito.Mockito.*;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.config.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;
import java.util.logging.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AsyncPlayerChatEvent.class, BattlegroundsConfig.class, BattlegroundsPlugin.class, ChatColor.class })
public class AsyncPlayerChatEventHandlerTest {

    private AsyncPlayerChatEvent event;
    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private Team team;
    private Translator translator;

    @Before
    public void setUp() {
        this.event = PowerMockito.mock(AsyncPlayerChatEvent.class);
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.team = mock(Team.class);
        this.translator = mock(Translator.class);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(ChatColor.class);
        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);

        when(event.getPlayer()).thenReturn(player);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(gameManager.getGame(player)).thenReturn(game);
        when(gamePlayer.getTeam()).thenReturn(team);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);
        when(plugin.getGameManager()).thenReturn(gameManager);
        when(plugin.getTranslator()).thenReturn(translator);
    }

    @Test
    public void testRemoveRecipientsWhenNotPlaying() {
        GamePlayer gamePlayer = mock(GamePlayer.class);
        List<GamePlayer> list = new ArrayList<>();

        list.add(gamePlayer);

        when(event.getRecipients()).thenReturn(Collections.EMPTY_SET);
        when(gameManager.getAllPlayers()).thenReturn(list);
        when(gameManager.getGame(player)).thenReturn(null);

        AsyncPlayerChatEventHandler eventHandler = new AsyncPlayerChatEventHandler(plugin);
        eventHandler.handle(event);

        verify(event, times(1)).getRecipients();
        verify(gamePlayer, times(1)).getPlayer();
    }

    @Test
    public void testPrintLoggerChat() {
        BattlegroundsConfig config = PowerMockito.mock(BattlegroundsConfig.class);
        Logger logger = mock(Logger.class);

        when(config.broadcastChat).thenReturn(true);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(plugin.getLogger()).thenReturn(logger);

        AsyncPlayerChatEventHandler eventHandler = new AsyncPlayerChatEventHandler(plugin);
        eventHandler.handle(event);

        verify(logger, times(1)).info(anyString());
    }
}
