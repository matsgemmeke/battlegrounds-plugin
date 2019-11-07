package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameState;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.storage.BattlegroundsConfig;
import com.matsg.battlegrounds.game.BattleGameConfiguration;
import com.matsg.battlegrounds.game.state.InGameState;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class JoinTest {

    private BattlegroundsConfig config;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private Player player;
    private PlayerManager playerManager;
    private Translator translator;

    @Before
    public void setUp() {
        this.config = mock(BattlegroundsConfig.class);
        this.game = mock(Game.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.translator = mock(Translator.class);

        this.gameId = 1;
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(game.getId()).thenReturn(gameId);
        when(game.getPlayerManager()).thenReturn(playerManager);
    }

    @Test
    public void executeCommandWhilePlayerIsInGame() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.ALREADY_PLAYING;

        String[] args = new String[] { "command", String.valueOf(gameId) };

        when(playerManager.getGamePlayer(player)).thenReturn(mock(GamePlayer.class));
        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        Join command = new Join(translator, gameManager, config);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
        verify(playerManager, never()).addPlayer(player);
    }

    @Test
    public void executeCommandWhileGameIsNotJoinable() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.IN_PROGRESS;

        GameState state = new InGameState();
        String[] args = new String[] { "command", String.valueOf(gameId) };

        config.joinableGameStates = Collections.emptyList();

        when(game.getState()).thenReturn(state);
        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        Join command = new Join(translator, gameManager, config);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
        verify(playerManager, never()).addPlayer(player);
    }

    @Test
    public void executeCommandWhenGameIsFull() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.SPOTS_FULL;

        GameConfiguration configuration = new BattleGameConfiguration(null, 1, 0, 0);
        GameState state = new InGameState();
        String[] args = new String[] { "command", String.valueOf(gameId) };

        config.joinableGameStates = Collections.singletonList("Ingame");

        when(game.getConfiguration()).thenReturn(configuration);
        when(game.getState()).thenReturn(state);
        when(playerManager.getPlayers()).thenReturn(Collections.singletonList(mock(GamePlayer.class)));
        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        Join command = new Join(translator, gameManager, config);
        command.execute(player, args);

        verify(player, times(1)).sendMessage(responseMessage);
        verify(playerManager, never()).addPlayer(player);
    }

    @Test
    public void executeCommandSuccessfulJoin() {
        GameConfiguration configuration = new BattleGameConfiguration(null, 2, 0, 0);
        GameState state = new InGameState();
        String[] args = new String[] { "command", String.valueOf(gameId) };

        config.joinableGameStates = Collections.singletonList("Ingame");

        when(game.getConfiguration()).thenReturn(configuration);
        when(game.getState()).thenReturn(state);
        when(playerManager.getPlayers()).thenReturn(Collections.singletonList(mock(GamePlayer.class)));

        Join command = new Join(translator, gameManager, config);
        command.execute(player, args);

        verify(player, never()).sendMessage(anyString());
        verify(playerManager, times(1)).addPlayer(player);
    }
}
