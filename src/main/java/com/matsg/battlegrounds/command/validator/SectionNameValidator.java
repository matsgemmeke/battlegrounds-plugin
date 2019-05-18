package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import com.matsg.battlegrounds.util.MessageHelper;

public class SectionNameValidator implements CommandValidator {

    private Battlegrounds plugin;
    private int pos;
    private MessageHelper messageHelper;

    public SectionNameValidator(Battlegrounds plugin, int pos) {
        this.plugin = plugin;
        this.pos = pos;
        this.messageHelper = new MessageHelper();
    }

    public ValidationResponse validate(String[] args) {
        int id = Integer.parseInt(args[1]);

        Game game = plugin.getGameManager().getGame(id);
        Arena arena = game.getArena();

        if (args.length <= pos) {
            return new ValidationResponse(messageHelper.create(TranslationKey.SPECIFY_SECTION_NAME));
        }

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[pos]);

        if (section == null) {
            return new ValidationResponse(messageHelper.create(TranslationKey.SECTION_NOT_EXISTS,
                    new Placeholder("bg_arena", arena.getName()),
                    new Placeholder("bg_section", args[pos])
            ));
        }

        return ValidationResponse.PASSED;
    }
}