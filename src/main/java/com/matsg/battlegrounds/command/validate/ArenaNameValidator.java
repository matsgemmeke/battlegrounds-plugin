package com.matsg.battlegrounds.command.validate;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.MessageHelper;

public class ArenaNameValidator implements CommandValidator {

    private Battlegrounds plugin;
    private MessageHelper messageHelper;

    public ArenaNameValidator(Battlegrounds plugin) {
        this.plugin = plugin;
        this.messageHelper = new MessageHelper();
    }

    public ValidationResponse validate(String[] args) {
        if (args.length < 3) {
            return new ValidationResponse(messageHelper.create(TranslationKey.SPECIFY_NAME));
        }

        int id = Integer.parseInt(args[1]);

        Game game = plugin.getGameManager().getGame(id);
        String name = args[2].replaceAll("_", " ");
        Arena arena = plugin.getGameManager().getArena(game, name);

        if (arena == null) {
            return new ValidationResponse(messageHelper.create(TranslationKey.ARENA_NOT_EXISTS,
                    new Placeholder("bg_arena", name),
                    new Placeholder("bg_game", id))
            );
        }

        return ValidationResponse.PASSED;
    }
}
