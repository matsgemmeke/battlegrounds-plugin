package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Translator.class})
public class GameModeUsageValidatorTest {

    private Battlegrounds plugin;
    private Game game;
    private GameConfiguration configuration;
    private GameManager gameManager;
    private GameModeUsageValidator validator;
    private int gameId;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.configuration = mock(GameConfiguration.class);
        this.gameManager = mock(GameManager.class);

        this.gameId = 1;
        this.validator = new GameModeUsageValidator(plugin, GameModeType.FREE_FOR_ALL);

        PowerMockito.mockStatic(Translator.class);

        when(game.getConfiguration()).thenReturn(configuration);
        when(gameManager.getGame(gameId)).thenReturn(game);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testValidationGameModeTypeIsNotUsedByGame() {
        String responseMessage = "Test", responsePath = TranslationKey.GAMEMODE_NOT_USED.getPath();
        String[] input = new String[] { "command", String.valueOf(gameId) };

        when(configuration.getGameModes()).thenReturn(new GameMode[0]);
        when(Translator.translate(responsePath)).thenReturn(responseMessage);

        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationGameModeTypeIsUsedByGame() {
        GameMode gameMode = mock(GameMode.class);
        String[] input = new String[] { "command", String.valueOf(gameId) };

        when(configuration.getGameModes()).thenReturn(new GameMode[] { gameMode });
        when(gameMode.getType()).thenReturn(GameModeType.FREE_FOR_ALL);

        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
