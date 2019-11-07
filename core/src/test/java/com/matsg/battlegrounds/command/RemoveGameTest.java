package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RemoveGameTest {

    private CacheYaml dataFile;
    private CommandSender sender;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private TaskRunner taskRunner;
    private Translator translator;

    @Before
    public void setUp() {
        this.dataFile = mock(CacheYaml.class);
        this.sender = mock(CommandSender.class);
        this.game = mock(Game.class);
        this.taskRunner = mock(TaskRunner.class);
        this.translator = mock(Translator.class);

        this.gameId = 1;
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(game.getDataFile()).thenReturn(dataFile);
        when(game.getId()).thenReturn(gameId);
    }

    @Test
    public void removeGameWhenSenderHasNotConfirmed() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.GAME_CONFIRM_REMOVE;

        String[] args = new String[] { "command", String.valueOf(gameId) };

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        RemoveGame command = new RemoveGame(translator, gameManager, taskRunner);
        command.execute(sender, args);

        verify(dataFile, never()).removeFile();
        verify(sender, times(1)).sendMessage(responseMessage);
        verify(taskRunner, times(1)).runTaskLater(any(Runnable.class), anyLong());
    }

    @Test
    public void removeGameWhenSenderHasConfirmed() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.GAME_REMOVE;

        String[] args = new String[] { "command", String.valueOf(gameId) };

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        RemoveGame command = new RemoveGame(translator, gameManager, taskRunner);
        command.execute(sender, args);
        command.execute(sender, args); // Execute twice to confirm the removal

        verify(dataFile, times(1)).removeFile();
        verify(sender, times(1)).sendMessage(responseMessage);

        assertEquals(0, gameManager.getGames().size());
        assertNull(gameManager.getGame(gameId));
    }
}
