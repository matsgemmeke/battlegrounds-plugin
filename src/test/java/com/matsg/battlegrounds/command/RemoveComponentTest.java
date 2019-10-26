package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.ArenaComponent;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class RemoveComponentTest {

    private Arena arena;
    private CommandSender sender;
    private Game game;
    private GameManager gameManager;
    private GameMode gameMode;
    private int gameId;
    private String arenaName;
    private Translator translator;

    @Before
    public void setUp() {
        this.arena = mock(Arena.class);
        this.sender = mock(CommandSender.class);
        this.game = mock(Game.class);
        this.gameMode = mock(GameMode.class);
        this.translator = mock(Translator.class);

        this.arenaName = "Arena";
        this.gameId = 1;
        this.gameManager = new BattleGameManager();

        gameManager.getGames().add(game);

        when(arena.getName()).thenReturn(arenaName);
        when(game.getArena(arenaName)).thenReturn(arena);
        when(game.getGameMode()).thenReturn(gameMode);
        when(game.getId()).thenReturn(gameId);
    }

    @Test
    public void executeCommandWithUnspecifiedComponentId() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.SPECIFY_COMPONENT_ID;

        String[] args = new String[] { "command", String.valueOf(gameId), arenaName };

        when(translator.translate(key.getPath())).thenReturn(responseMessage);

        RemoveComponent command = new RemoveComponent(translator, gameManager);
        command.execute(sender, args);

        verify(sender, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void executeCommandWithInvalidComponentId() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.INVALID_ARGUMENT_TYPE;

        String[] args = new String[] { "command", String.valueOf(gameId), arenaName, "fail" };

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        RemoveComponent command = new RemoveComponent(translator, gameManager);
        command.execute(sender, args);

        verify(sender, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void executeCommandWithUnavailableComponentId() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.COMPONENT_NOT_EXISTS;

        int componentId = 15;
        String[] args = new String[] { "command", String.valueOf(gameId), arenaName, String.valueOf(componentId) };

        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        RemoveComponent command = new RemoveComponent(translator, gameManager);
        command.execute(sender, args);

        verify(sender, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void executeCommandRemoveFromArena() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.COMPONENT_REMOVE;

        ArenaComponent component = mock(ArenaComponent.class);
        CacheYaml dataFile = mock(CacheYaml.class);
        int componentId = 15;
        String arenaName = "Arena";
        String[] args = new String[] { "command", String.valueOf(gameId), arenaName, String.valueOf(componentId) };

        when(arena.getComponent(componentId)).thenReturn(component);
        when(game.getDataFile()).thenReturn(dataFile);
        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        RemoveComponent command = new RemoveComponent(translator, gameManager);
        command.execute(sender, args);

        verify(arena, times(1)).removeComponent(component);
        verify(dataFile, times(1)).set("arena." + arenaName + ".component." + componentId, null);
        verify(dataFile, times(1)).save();
        verify(sender, times(1)).sendMessage(responseMessage);
    }

    @Test
    public void executeCommandRemoveFromGameMode() {
        String responseMessage = "Response";
        TranslationKey key = TranslationKey.COMPONENT_REMOVE;

        ArenaComponent component = mock(ArenaComponent.class);
        CacheYaml dataFile = mock(CacheYaml.class);
        int componentId = 15;
        String[] args = new String[] { "command", String.valueOf(gameId), arenaName, String.valueOf(componentId) };

        when(gameMode.getComponent(componentId)).thenReturn(component);
        when(game.getDataFile()).thenReturn(dataFile);
        when(game.getGameModeList()).thenReturn(Collections.singletonList(gameMode));
        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        RemoveComponent command = new RemoveComponent(translator, gameManager);
        command.execute(sender, args);

        verify(dataFile, times(1)).set("arena." + arenaName + ".component." + componentId, null);
        verify(dataFile, times(1)).save();
        verify(gameMode, times(1)).removeComponent(component);
        verify(sender, times(1)).sendMessage(responseMessage);
    }
}
