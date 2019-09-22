package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.Placeholder;

public class ArenaNameValidator implements CommandValidator {

    private boolean shouldExist; // Boolean setting for checking whether an arena name exists
    private GameManager gameManager;
    private Translator translator;

    public ArenaNameValidator(GameManager gameManager, Translator translator, boolean shouldExist) {
        this.gameManager = gameManager;
        this.translator = translator;
        this.shouldExist = shouldExist;
    }

    public ValidationResponse validate(String[] args) {
        if (args.length < 3) {
            return new ValidationResponse(translator.translate(TranslationKey.SPECIFY_ARENA_NAME));
        }

        int id = Integer.parseInt(args[1]);

        Game game = gameManager.getGame(id);
        String name = args[2].replaceAll("_", " ");
        Arena arena = gameManager.getArena(game, name);

        if (arena == null && shouldExist) {
            return new ValidationResponse(translator.translate(TranslationKey.ARENA_NOT_EXISTS,
                    new Placeholder("bg_arena", name),
                    new Placeholder("bg_game", id))
            );
        }

        if (arena != null && !shouldExist) {
            return new ValidationResponse(translator.translate(TranslationKey.ARENA_EXISTS,
                    new Placeholder("bg_arena", name),
                    new Placeholder("bg_game", id))
            );
        }

        return ValidationResponse.PASSED;
    }
}
