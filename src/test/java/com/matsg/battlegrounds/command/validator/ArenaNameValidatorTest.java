package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ArenaNameValidatorTest {

    private Arena arena;
    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private String arenaName;
    private String responseMessage;
    private Translator translator;

    @Before
    public void setUp() {
        this.arena = mock(Arena.class);
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);
        this.translator = mock(Translator.class);

        this.arenaName = "Arena";
        this.gameId = 1;
        this.responseMessage = "Response";

        when(gameManager.getGame(gameId)).thenReturn(game);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testValidationNoNameSpecified() {
        String[] input = new String[] { "command", String.valueOf(gameId) };
        TranslationKey key = TranslationKey.SPECIFY_ARENA_NAME;

        when(translator.translate(key)).thenReturn(responseMessage);

        ArenaNameValidator validator = new ArenaNameValidator(plugin, translator, true);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationArenaWithSpecifiedNameDoesNotExist() {
        String[] input = new String[] { "command", String.valueOf(gameId), arenaName };
        TranslationKey key = TranslationKey.ARENA_NOT_EXISTS;

        when(gameManager.getArena(game, arenaName)).thenReturn(null);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        ArenaNameValidator validator = new ArenaNameValidator(plugin, translator, true);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationArenaWithSpecifiedNameExists() {
        String[] input = new String[] { "command", String.valueOf(gameId), arenaName };
        TranslationKey key = TranslationKey.ARENA_EXISTS;

        when(gameManager.getArena(game, arenaName)).thenReturn(arena);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        ArenaNameValidator validator = new ArenaNameValidator(plugin, translator, false);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationPasses() {
        String[] input = new String[] { "command", String.valueOf(gameId), arenaName };

        when(gameManager.getArena(game, arenaName)).thenReturn(arena);

        ArenaNameValidator validator = new ArenaNameValidator(plugin, translator, true);
        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
