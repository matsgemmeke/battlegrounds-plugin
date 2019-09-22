package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LeaveTest {

    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private String responseMessage;
    private Translator translator;

    @Before
    public void setUp() {
        this.game = mock(Game.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.translator = mock(Translator.class);

        this.gameManager = new BattleGameManager();
        this.responseMessage = "Response";

        gameManager.getGames().add(game);

        when(game.getPlayerManager()).thenReturn(playerManager);
    }

    @Test
    public void leaveWhenNotPlaying() {
        TranslationKey key = TranslationKey.NOT_PLAYING;

        when(playerManager.getGamePlayer(player)).thenReturn(null);
        when(translator.translate(key)).thenReturn(responseMessage);

        Leave command = new Leave(translator, gameManager);
        command.execute(player, new String[0]);

        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void leaveWhenPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        Leave command = new Leave(translator, gameManager);
        command.execute(player, new String[0]);

        verify(playerManager, times(1)).removePlayer(gamePlayer);
    }
}
