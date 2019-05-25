package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.Placeholder;

public class ArenaNameValidator implements CommandValidator {

    private Battlegrounds plugin;
    private boolean shouldExist; // Boolean setting for checking whether an arena name exists
    private Translator translator;

    public ArenaNameValidator(Battlegrounds plugin, Translator translator, boolean shouldExist) {
        this.plugin = plugin;
        this.translator = translator;
        this.shouldExist = shouldExist;
    }

    public ValidationResponse validate(String[] args) {
        if (args.length < 3) {
            return new ValidationResponse(translator.translate(TranslationKey.SPECIFY_ARENA_NAME));
        }

        int id = Integer.parseInt(args[1]);

        Game game = plugin.getGameManager().getGame(id);
        String name = args[2].replaceAll("_", " ");
        Arena arena = plugin.getGameManager().getArena(game, name);

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
