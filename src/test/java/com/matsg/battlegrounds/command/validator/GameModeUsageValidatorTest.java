package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.BattleGameManager;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.mode.GameModeType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameModeUsageValidatorTest {

    private Game game;
    private GameConfiguration configuration;
    private GameManager gameManager;
    private GameMode gameMode;
    private int gameId;
    private String responseMessage;
    private Translator translator;

    @Before
    public void setUp() {
        this.game = mock(Game.class);
        this.configuration = mock(GameConfiguration.class);
        this.gameMode = mock(GameMode.class);
        this.translator = mock(Translator.class);

        this.gameManager = new BattleGameManager();

        this.gameId = 1;
        this.responseMessage = "Response";

        gameManager.getGames().add(game);

        when(game.getConfiguration()).thenReturn(configuration);
        when(game.getId()).thenReturn(gameId);
    }

    @Test
    public void validationGameModeTypeIsNotUsedByGame() {
        GameModeType gameModeType = GameModeType.FREE_FOR_ALL;
        String[] input = new String[] { "command", String.valueOf(gameId) };
        TranslationKey key = TranslationKey.GAMEMODE_NOT_USED;

        when(game.getGameModeList()).thenReturn(Arrays.asList(gameMode));
        when(gameMode.getId()).thenReturn("fail");
        when(translator.translate(eq(key.getPath()), anyVararg())).thenReturn(responseMessage);

        GameModeUsageValidator validator = new GameModeUsageValidator(gameManager, translator, gameModeType);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void validationGameModeTypeIsUsedByGame() {
        GameModeType gameModeType = GameModeType.FREE_FOR_ALL;
        String[] input = new String[] { "command", String.valueOf(gameId) };

        when(game.getGameModeList()).thenReturn(Arrays.asList(gameMode));
        when(gameMode.getId()).thenReturn(gameModeType.toString());

        GameModeUsageValidator validator = new GameModeUsageValidator(gameManager, translator, gameModeType);
        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
