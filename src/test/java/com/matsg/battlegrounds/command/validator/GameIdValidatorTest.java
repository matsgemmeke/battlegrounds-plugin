package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameIdValidatorTest {

    private Battlegrounds plugin;
    private GameManager gameManager;
    private int gameId;
    private String responseMessage;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.gameManager = mock(GameManager.class);
        this.translator = mock(Translator.class);

        this.gameId = 1;
        this.responseMessage = "Response";

        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testValidationNoIdSpecified() {
        String[] input = new String[] { "command" };
        TranslationKey key = TranslationKey.SPECIFY_GAME_ID;

        when(translator.translate(key)).thenReturn(responseMessage);

        GameIdValidator validator = new GameIdValidator(plugin, translator, true);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationInvalidId() {
        String[] input = new String[] { "command", "id" };
        TranslationKey key = TranslationKey.INVALID_ARGUMENT_TYPE;

        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        GameIdValidator validator = new GameIdValidator(plugin, translator, true);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationGameWithSpecifiedIdDoesNotExist() {
        String[] input = new String[] { "command", String.valueOf(gameId) };
        TranslationKey key = TranslationKey.GAME_NOT_EXISTS;

        when(gameManager.exists(gameId)).thenReturn(false);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        GameIdValidator validator = new GameIdValidator(plugin, translator, true);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationGameWithSpecifiedIdExists() {
        String[] input = new String[] { "command", String.valueOf(gameId) };
        TranslationKey key = TranslationKey.GAME_EXISTS;

        when(gameManager.exists(gameId)).thenReturn(true);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        GameIdValidator validator = new GameIdValidator(plugin, translator, false);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationPasses() {
        String[] input = new String[] { "command", String.valueOf(gameId) };

        when(gameManager.exists(gameId)).thenReturn(true);

        GameIdValidator validator = new GameIdValidator(plugin, translator, true);
        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
