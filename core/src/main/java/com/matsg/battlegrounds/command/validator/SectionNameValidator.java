package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.mode.zombies.Zombies;

public class SectionNameValidator implements CommandValidator {

    private GameManager gameManager;
    private int sectionPos; // The position in the input args where the section should be specified
    private Translator translator;

    public SectionNameValidator(GameManager gameManager, Translator translator, int sectionPos) {
        this.gameManager = gameManager;
        this.translator = translator;
        this.sectionPos = sectionPos;
    }

    public ValidationResponse validate(String[] args) {
        int id = Integer.parseInt(args[1]);

        Game game = gameManager.getGame(id);
        Arena arena = game.getArena();

        if (args.length <= sectionPos) {
            return new ValidationResponse(translator.translate(TranslationKey.SPECIFY_SECTION_NAME.getPath()));
        }

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[sectionPos]);

        if (section == null) {
            return new ValidationResponse(translator.translate(TranslationKey.SECTION_NOT_EXISTS.getPath(),
                    new Placeholder("bg_arena", arena.getName()),
                    new Placeholder("bg_section", args[sectionPos])
            ));
        }

        return ValidationResponse.PASSED;
    }
}
