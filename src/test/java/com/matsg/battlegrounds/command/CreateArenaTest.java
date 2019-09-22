package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CreateArenaTest {

    private CacheYaml dataFile;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private Player player;
    private SelectionManager selectionManager;
    private String arenaName;
    private String responseMessage;
    private Translator translator;
    private World world;

    @Before
    public void setUp() {
        this.dataFile = mock(CacheYaml.class);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);
        this.player = mock(Player.class);
        this.selectionManager = mock(SelectionManager.class);
        this.translator = mock(Translator.class);
        this.world = mock(World.class);

        this.arenaName = "Arena";
        this.gameId = 1;
        this.responseMessage = "Response";

        when(game.getDataFile()).thenReturn(dataFile);
        when(gameManager.exists(gameId)).thenReturn(true);
        when(gameManager.getGame(gameId)).thenReturn(game);
        when(player.getWorld()).thenReturn(world);
    }

    @Test
    public void createArenaWithoutSelection() {
        String[] input = new String[] { "command", String.valueOf(gameId), arenaName };
        TranslationKey key = TranslationKey.NO_SELECTION;

        when(selectionManager.getSelection(player)).thenReturn(null);
        when(translator.translate(key)).thenReturn(responseMessage);

        CreateArena command = new CreateArena(translator, gameManager, selectionManager);
        command.execute(player, input);

        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void createArenaWithoutBounds() {
        Selection selection = mock(Selection.class);
        String[] input = new String[] { "command", String.valueOf(gameId), arenaName };
        TranslationKey key = TranslationKey.ARENA_CREATE;

        when(selectionManager.getSelection(player)).thenReturn(selection);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        CreateArena command = new CreateArena(translator, gameManager, selectionManager);
        command.execute(player, input);

        verify(dataFile, times(0)).setLocation(anyString(), any(Location.class), anyBoolean());
        verify(player, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void createArenaWithBounds() {
        Location location = new Location(world, 0, 0, 0);
        Selection selection = mock(Selection.class);
        String[] input = new String[] { "command", String.valueOf(gameId), arenaName };
        TranslationKey key = TranslationKey.ARENA_CREATE;

        when(selection.getMaximumPoint()).thenReturn(location);
        when(selection.getMinimumPoint()).thenReturn(location);
        when(selectionManager.getSelection(player)).thenReturn(selection);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        CreateArena command = new CreateArena(translator, gameManager, selectionManager);
        command.execute(player, input);

        verify(dataFile, times(2)).setLocation(anyString(), any(Location.class), anyBoolean());
        verify(player, times(1)).sendMessage(responseMessage);
    }
}
