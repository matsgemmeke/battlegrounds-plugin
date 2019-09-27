package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.mode.GameModeType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameModeUsageValidatorTest {

    private Game game;
    private GameConfiguration configuration;
    private GameManager gameManager;
    private int gameId;
    private String responseMessage;
    private Translator translator;

    @Before
    public void setUp() {
        this.game = mock(Game.class);
        this.configuration = mock(GameConfiguration.class);
        this.gameManager = mock(GameManager.class);
        this.translator = mock(Translator.class);

        this.gameId = 1;
        this.responseMessage = "Response";

        when(game.getConfiguration()).thenReturn(configuration);
        when(gameManager.getGame(gameId)).thenReturn(game);
    }

    @Test
    public void validationGameModeTypeIsNotUsedByGame() {
        String[] input = new String[] { "command", String.valueOf(gameId) };
        TranslationKey key = TranslationKey.GAMEMODE_NOT_USED;

        when(configuration.getGameModes()).thenReturn(new GameMode[0]);
        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        GameModeUsageValidator validator = new GameModeUsageValidator(gameManager, translator, GameModeType.FREE_FOR_ALL);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void validationGameModeTypeIsUsedByGame() {
        GameMode gameMode = mock(GameMode.class);
        String[] input = new String[] { "command", String.valueOf(gameId) };

        when(configuration.getGameModes()).thenReturn(new GameMode[] { gameMode });
        when(gameMode.getId()).thenReturn(GameModeType.FREE_FOR_ALL.toString());

        GameModeUsageValidator validator = new GameModeUsageValidator(gameManager, translator, GameModeType.FREE_FOR_ALL);
        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
