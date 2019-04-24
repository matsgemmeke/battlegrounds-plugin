package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.Translator;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
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
public class SectionNameValidatorTest {

    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private SectionNameValidator validator;
    private String name;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);

        this.gameId = 1;
        this.name = "Section";
        this.validator = new SectionNameValidator(plugin);

        PowerMockito.mockStatic(Translator.class);

        when(gameManager.getGame(gameId)).thenReturn(game);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void testValidationNoNameSpecified() {
        String responseMessage = "Test", responsePath = TranslationKey.SPECIFY_SECTION_NAME.getPath();
        String[] input = new String[] { "command", String.valueOf(gameId), "arena" };

        when(Translator.translate(responsePath)).thenReturn(responseMessage);

        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationSectionWithSpecifiedNameDoesNotExist() {
        Zombies zombies = mock(Zombies.class);

        String responseMessage = "Test", responsePath = TranslationKey.SECTION_NOT_EXISTS.getPath();
        String[] input = new String[] { "command", String.valueOf(gameId), "arena", name };

        when(game.getArena()).thenReturn(mock(Arena.class));
        when(game.getGameMode(Zombies.class)).thenReturn(zombies);
        when(zombies.getSection(name)).thenReturn(null);
        when(Translator.translate(responsePath)).thenReturn(responseMessage);

        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void testValidationSectionWithSpecifiedNameExists() {
        Zombies zombies = mock(Zombies.class);

        String[] input = new String[] { "command", String.valueOf(gameId), "arena", name };

        when(game.getGameMode(Zombies.class)).thenReturn(zombies);
        when(zombies.getSection(name)).thenReturn(mock(Section.class));

        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
