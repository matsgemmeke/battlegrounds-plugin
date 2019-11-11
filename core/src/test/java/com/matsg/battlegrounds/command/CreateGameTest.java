package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.GameFactory;
import com.matsg.battlegrounds.mode.GameModeFactory;
import com.matsg.battlegrounds.mode.GameModeType;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreateGameTest {

    private Battlegrounds plugin;
    private CommandSender sender;
    private EventDispatcher eventDispatcher;
    private GameFactory gameFactory;
    private GameManager gameManager;
    private GameModeFactory gameModeFactory;
    private int gameId;
    private TaskRunner taskRunner;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.sender = mock(CommandSender.class);
        this.eventDispatcher = mock(EventDispatcher.class);
        this.gameModeFactory = mock(GameModeFactory.class);
        this.taskRunner = mock(TaskRunner.class);
        this.translator = mock(Translator.class);

        this.gameFactory = new GameFactory(plugin, taskRunner);
        this.gameId = 1;
        this.gameManager = new BattleGameManager();

        when(gameModeFactory.make(any(Game.class), any(GameModeType.class))).thenReturn(mock(GameMode.class));
        when(plugin.getDataFolder()).thenReturn(new File("test"));
        when(plugin.getEventDispatcher()).thenReturn(eventDispatcher);
    }

    @Test
    public void createGame() {
        String[] args = new String[] { "command", String.valueOf(gameId) };

        CreateGame command = new CreateGame(translator, gameFactory, gameManager, gameModeFactory);
        command.execute(sender, args);

        verify(gameModeFactory, atLeast(1)).make(any(Game.class), any(GameModeType.class));
        verify(sender, times(1)).sendMessage(anyString());

        assertEquals(1, gameManager.getGames().size());
        assertNotNull(gameManager.getGame(gameId));
    }
}
