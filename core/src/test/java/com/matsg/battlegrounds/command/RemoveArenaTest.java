package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.game.BattleArena;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RemoveArenaTest {

    private Arena arena;
    private CacheYaml dataFile;
    private CommandSender sender;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private String arenaName;
    private TaskRunner taskRunner;
    private Translator translator;

    @Before
    public void setUp() {
        this.dataFile = mock(CacheYaml.class);
        this.sender = mock(CommandSender.class);
        this.game = mock(Game.class);
        this.taskRunner = mock(TaskRunner.class);
        this.translator = mock(Translator.class);

        this.arenaName = "Arena";
        this.arena = new BattleArena(arenaName, null, null, null);
        this.gameId = 1;
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(game.getArenaList()).thenReturn(new ArrayList<>(Arrays.asList(arena)));
        when(game.getArena(arenaName)).thenReturn(arena);
        when(game.getDataFile()).thenReturn(dataFile);
        when(game.getId()).thenReturn(gameId);
    }

    @Test
    public void removeArenaWhenSenderHasNotConfirmed() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.ARENA_CONFIRM_REMOVE;

        String[] args = new String[] { "command", String.valueOf(gameId), arenaName };

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        RemoveArena command = new RemoveArena(translator, gameManager, taskRunner);
        command.execute(sender, args);

        verify(dataFile, never()).save();
        verify(sender, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void removeArenaWhenSenderHasConfirmed() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.ARENA_REMOVE;

        String[] args = new String[] { "command", String.valueOf(gameId), arenaName };

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        RemoveArena command = new RemoveArena(translator, gameManager, taskRunner);
        command.execute(sender, args);
        command.execute(sender, args); // Execute twice to confirm the removal

        verify(dataFile, times(1)).set("arena." + arenaName, null);
        verify(dataFile, times(1)).save();
        verify(sender, times(1)).sendMessage(responseMessage);

        assertEquals(0, game.getArenaList().size());
    }
}
