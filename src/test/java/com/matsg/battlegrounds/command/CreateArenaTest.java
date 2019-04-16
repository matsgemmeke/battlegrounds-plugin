package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Translator.class})
public class CreateArenaTest {

    private Battlegrounds plugin;
    private CacheYaml dataFile;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private Player player;
    private String arenaName;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.dataFile = mock(CacheYaml.class);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);
        this.player = mock(Player.class);

        this.gameId = 1;
        this.arenaName = "Arena";

        PowerMockito.mockStatic(Translator.class);

        when(game.getDataFile()).thenReturn(dataFile);
        when(gameManager.exists(gameId)).thenReturn(true);
        when(gameManager.getGame(gameId)).thenReturn(game);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testCommandNoNameSpecified() {
        String message = "Test", messagePath = TranslationKey.SPECIFY_ARENA_NAME.getPath();

        when(Translator.translate(messagePath)).thenReturn(message);

        CreateArena command = new CreateArena(plugin);
        command.execute(player, new String[] { "createarena", String.valueOf(gameId) });

        verify(dataFile, never()).set(anyString(), anyBoolean());
        verify(player, times(1)).sendMessage(message);
    }

    @Test
    public void testCommandSpecifiedNameExists() {
        String message = "Test", messagePath = TranslationKey.ARENA_EXISTS.getPath();

        when(gameManager.getArena(game, arenaName)).thenReturn(mock(Arena.class));
        when(Translator.translate(messagePath)).thenReturn(message);

        CreateArena command = new CreateArena(plugin);
        command.execute(player, new String[] { "createarena", String.valueOf(gameId), arenaName });

        verify(dataFile, never()).set(anyString(), anyBoolean());
        verify(player, times(1)).sendMessage(message);
    }
}
