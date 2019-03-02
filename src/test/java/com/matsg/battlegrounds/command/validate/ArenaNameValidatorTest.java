package com.matsg.battlegrounds.command.validate;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BattlegroundsPlugin.class, Translator.class})
public class ArenaNameValidatorTest {

    private ArenaNameValidator validator;
    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private int gameId;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.validator = new ArenaNameValidator(plugin);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);
        this.gameId = 1;

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Translator.class);

        when(gameManager.getGame(gameId)).thenReturn(game);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testValidationNoNameSpecified() {
        String responseMessage = "Test", responsePath = TranslationKey.SPECIFY_NAME.getPath();
        String[] input = new String[] { "command", String.valueOf(gameId) };

        when(Translator.translate(responsePath)).thenReturn(responseMessage);

        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationArenaWithSpecifiedNameDoesNotExist() {
        String name = "Name";
        String responseMessage = "Test", responsePath = TranslationKey.ARENA_NOT_EXISTS.getPath();
        String[] input = new String[] { "command", String.valueOf(gameId), name };

        when(gameManager.getArena(game, name)).thenReturn(null);
        when(Translator.translate(responsePath)).thenReturn(responseMessage);

        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationArenaWithSpecifiedNameExists() {
        Arena arena = mock(Arena.class);
        String name = "Name";
        String[] input = new String[] { "command", String.valueOf(gameId), name };

        when(gameManager.getArena(game, name)).thenReturn(arena);

        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
