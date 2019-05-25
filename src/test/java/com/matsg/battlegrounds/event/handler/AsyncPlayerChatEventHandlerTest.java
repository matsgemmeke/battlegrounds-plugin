package com.matsg.battlegrounds.event.handler;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.game.BattleTeam;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncPlayerChatEventHandlerTest {

    private AsyncPlayerChatEvent event;
    private Battlegrounds plugin;
    private BattlegroundsConfig config;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private String responseMessage;
    private Team team;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.config = mock(BattlegroundsConfig.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.translator = mock(Translator.class);

        this.event = new AsyncPlayerChatEvent(false, player, "Message", new HashSet<>());
        this.gameManager = new BattleGameManager();
        this.gamePlayer = new BattleGamePlayer(player, null);
        this.responseMessage = "Response";
        this.team = new BattleTeam(1, "Team", null, null);

        gameManager.getGames().add(game);
        gamePlayer.setTeam(team);

        when(game.getPlayerManager()).thenReturn(playerManager);
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);
        when(plugin.getBattlegroundsConfig()).thenReturn(config);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void removeRecipientsWhenNotPlaying() {
        List<GamePlayer> list = new ArrayList<>();

        list.add(gamePlayer);

        event.getRecipients().add(player);

        when(playerManager.getGamePlayer(player)).thenReturn(null);
        when(playerManager.getPlayers()).thenReturn(list);

        AsyncPlayerChatEventHandler eventHandler = new AsyncPlayerChatEventHandler(plugin, translator);
        eventHandler.handle(event);

        assertEquals(0, event.getRecipients().size());
    }

    @Test
    public void loggerPrintWhenConfigured() {
        config.broadcastChat = true;

        Logger logger = mock(Logger.class);
        TranslationKey key = TranslationKey.PLAYER_MESSAGE;

        when(plugin.getLogger()).thenReturn(logger);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        AsyncPlayerChatEventHandler eventHandler = new AsyncPlayerChatEventHandler(plugin, translator);
        eventHandler.handle(event);

        verify(logger, times(1)).info(anyString());
        verify(playerManager, times(1)).broadcastMessage(responseMessage);

        assertTrue(event.isCancelled());
    }

    @Test
    public void loggerPrintWhenNotConfigured() {
        config.broadcastChat = false;

        Logger logger = mock(Logger.class);
        TranslationKey key = TranslationKey.PLAYER_MESSAGE;

        when(plugin.getLogger()).thenReturn(logger);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        AsyncPlayerChatEventHandler eventHandler = new AsyncPlayerChatEventHandler(plugin, translator);
        eventHandler.handle(event);

        verify(logger, times(0)).info(anyString());
        verify(playerManager, times(1)).broadcastMessage(responseMessage);

        assertTrue(event.isCancelled());
    }
}
