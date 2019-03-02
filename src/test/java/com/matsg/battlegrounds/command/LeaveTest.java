package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.PlayerManager;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BattlegroundsPlugin.class, Translator.class})
public class LeaveTest {

    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private GamePlayer gamePlayer;
    private Player player;
    private PlayerManager playerManager;
    private String translatorMessage;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gamePlayer = mock(GamePlayer.class);
        this.player = mock(Player.class);
        this.playerManager = mock(PlayerManager.class);
        this.translatorMessage = "Test";

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Translator.class);

        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(BattlegroundsPlugin.getPlugin()).thenReturn(plugin);
        when(Translator.translate(any())).thenReturn(translatorMessage);
        when(game.getPlayerManager()).thenReturn(playerManager);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testLeaveCommandWhenNotPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(null);

        Leave command = new Leave(plugin);
        command.execute(player, new String[0]);

        verify(player, times(1)).sendMessage(translatorMessage);
    }

    @Test
    public void testLeaveCommandWhenPlaying() {
        when(playerManager.getGamePlayer(player)).thenReturn(gamePlayer);

        Leave command = new Leave(plugin);
        command.execute(player, new String[0]);

        verify(playerManager, times(1)).removePlayer(gamePlayer);
    }
}
