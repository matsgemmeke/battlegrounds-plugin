package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.MessageHelper;

public class GameIdValidator implements CommandValidator {

    private Battlegrounds plugin;
    private MessageHelper messageHelper;

    public GameIdValidator(Battlegrounds plugin) {
        this.plugin = plugin;
        this.messageHelper = new MessageHelper();
    }

    public ValidationResponse validate(String[] args) {
        if (args.length < 2) {
            return new ValidationResponse(messageHelper.create(TranslationKey.SPECIFY_GAME_ID));
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            return new ValidationResponse(messageHelper.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
        }

        if (!plugin.getGameManager().exists(id)) {
            return new ValidationResponse(messageHelper.create(TranslationKey.GAME_NOT_EXISTS, new Placeholder("bg_game", id)));
        }

        return ValidationResponse.PASSED;
    }
}
