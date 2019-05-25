package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.Translator;

public class GameIdValidator implements CommandValidator {

    private Battlegrounds plugin;
    private boolean shouldExist; // Boolean setting for checking whether a certain game id exists
    private Translator translator;

    public GameIdValidator(Battlegrounds plugin, Translator translator, boolean shouldExist) {
        this.plugin = plugin;
        this.translator = translator;
        this.shouldExist = shouldExist;
    }

    public ValidationResponse validate(String[] args) {
        if (args.length < 2) {
            return new ValidationResponse(translator.translate(TranslationKey.SPECIFY_GAME_ID));
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            return new ValidationResponse(translator.translate(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
        }

        if (plugin.getGameManager().exists(id) != shouldExist) {
            TranslationKey key = shouldExist ? TranslationKey.GAME_NOT_EXISTS : TranslationKey.GAME_EXISTS;

            return new ValidationResponse(translator.translate(key, new Placeholder("bg_game", id)));
        }

        return ValidationResponse.PASSED;
    }
}
