package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.game.mode.GameModeType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameModeUsageValidatorTest {

    private Battlegrounds plugin;
    private Game game;
    private GameConfiguration configuration;
    private GameManager gameManager;
    private int gameId;
    private String responseMessage;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.configuration = mock(GameConfiguration.class);
        this.gameManager = mock(GameManager.class);
        this.translator = mock(Translator.class);

        this.gameId = 1;
        this.responseMessage = "Response";

        when(game.getConfiguration()).thenReturn(configuration);
        when(gameManager.getGame(gameId)).thenReturn(game);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testValidationGameModeTypeIsNotUsedByGame() {
        String[] input = new String[] { "command", String.valueOf(gameId) };
        TranslationKey key = TranslationKey.GAMEMODE_NOT_USED;

        when(configuration.getGameModes()).thenReturn(new GameMode[0]);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);

        GameModeUsageValidator validator = new GameModeUsageValidator(plugin, translator, GameModeType.FREE_FOR_ALL);
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

        GameModeUsageValidator validator = new GameModeUsageValidator(plugin, translator, GameModeType.FREE_FOR_ALL);
        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
