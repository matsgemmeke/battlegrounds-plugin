package com.matsg.battlegrounds.mode;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import org.junit.Before;

import java.io.File;

import static org.mockito.Mockito.*;

public class GameModeFactoryTest {

    private Battlegrounds plugin;
    private Game game;
    private File directory;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.translator = mock(Translator.class);

        this.directory = new File("test");

        when(plugin.getDataFolder()).thenReturn(directory);
    }

//    @Test
//    public void makeFreeForAllGameMode() {
//        GameModeFactory factory = new GameModeFactory(plugin, translator);
//        GameMode gameMode = factory.make(game, GameModeType.FREE_FOR_ALL);
//
//
//    }
}
