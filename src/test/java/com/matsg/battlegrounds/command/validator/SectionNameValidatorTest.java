package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SectionNameValidatorTest {

    private Battlegrounds plugin;
    private Game game;
    private GameManager gameManager;
    private int gameId;
    private String sectionName;
    private String responseMessage;
    private Translator translator;

    @Before
    public void setUp() {
        this.plugin = mock(Battlegrounds.class);
        this.game = mock(Game.class);
        this.gameManager = mock(GameManager.class);
        this.translator = mock(Translator.class);

        this.gameId = 1;
        this.responseMessage = "Response";
        this.sectionName = "Section";

        when(gameManager.getGame(gameId)).thenReturn(game);
        when(plugin.getGameManager()).thenReturn(gameManager);
    }

    @Test
    public void validationNoNameSpecified() {
        String[] input = new String[] { "command", String.valueOf(gameId) };
        TranslationKey key = TranslationKey.SPECIFY_SECTION_NAME;

        when(translator.translate(key)).thenReturn(responseMessage);

        SectionNameValidator validator = new SectionNameValidator(plugin, translator, 3);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void validationSectionWithSpecifiedNameDoesNotExist() {
        Zombies zombies = mock(Zombies.class);

        String[] input = new String[] { "command", String.valueOf(gameId), sectionName };
        TranslationKey key = TranslationKey.SECTION_NOT_EXISTS;

        when(game.getArena()).thenReturn(mock(Arena.class));
        when(game.getGameMode(Zombies.class)).thenReturn(zombies);
        when(translator.translate(eq(key), anyVararg())).thenReturn(responseMessage);
        when(zombies.getSection(sectionName)).thenReturn(null);

        SectionNameValidator validator = new SectionNameValidator(plugin, translator, 3);
        ValidationResponse response = validator.validate(input);

        assertFalse(response.passed());
        assertEquals(responseMessage, response.getMessage());
    }

    @Test
    public void validationSectionWithSpecifiedNameExists() {
        Zombies zombies = mock(Zombies.class);

        String[] input = new String[] { "command", String.valueOf(gameId), sectionName };

        when(game.getGameMode(Zombies.class)).thenReturn(zombies);
        when(zombies.getSection(sectionName)).thenReturn(mock(Section.class));

        SectionNameValidator validator = new SectionNameValidator(plugin, translator, 3);
        ValidationResponse response = validator.validate(input);

        assertTrue(response.passed());
        assertNull(response.getMessage());
    }
}
