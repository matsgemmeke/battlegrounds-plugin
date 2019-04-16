package com.matsg.battlegrounds.command.validate;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
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
public class GameIdValidatorTest {

    private Battlegrounds plugin;
    private GameIdValidator validator;
    private GameManager gameManager;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.validator = new GameIdValidator(plugin);
        this.gameManager = mock(GameManager.class);

        PowerMockito.mockStatic(BattlegroundsPlugin.class);
        PowerMockito.mockStatic(Translator.class);

        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testValidationNoIdSpecified() {
        String responseMessage = "Test", responsePath = TranslationKey.SPECIFY_GAME_ID.getPath();
        String[] input = new String[] { "command" };

        when(Translator.translate(responsePath)).thenReturn(responseMessage);

        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationInvalidId() {
        String responseMessage = "Test", responsePath = TranslationKey.INVALID_ARGUMENT_TYPE.getPath();
        String[] input = new String[] { "command", "id" };

        when(Translator.translate(responsePath)).thenReturn(responseMessage);

        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationGameWithSpecifiedIdDoesNotExist() {
        int id = 1;
        String responseMessage = "Test", responsePath = TranslationKey.GAME_NOT_EXISTS.getPath();
        String[] input = new String[] { "command", String.valueOf(id) };

        when(gameManager.exists(id)).thenReturn(false);
        when(Translator.translate(responsePath)).thenReturn(responseMessage);

        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationGameWithSpecifiedIdExists() {
        int id = 1;
        String[] input = new String[] { "command", String.valueOf(id) };

        when(gameManager.exists(id)).thenReturn(true);

        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
