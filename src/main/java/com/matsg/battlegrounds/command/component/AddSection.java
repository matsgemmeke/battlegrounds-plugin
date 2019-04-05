package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.component.ArenaSection;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.entity.Player;

public class AddSection implements ComponentCommand {

    private MessageHelper messageHelper;

    public AddSection() {
        this.messageHelper = new MessageHelper();
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Arena arena = context.getArena();
        Game game = context.getGame();
        Player player = context.getPlayer();

        if (args.length == 0) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_NAME));
            return;
        }

        if (arena.getSection(args[3]) != null) {
            player.sendMessage(messageHelper.create(TranslationKey.SECTION_EXISTS,
                    new Placeholder("bg_arena", arena.getName()),
                    new Placeholder("bg_section", args[0])
            ));
            return;
        }

        if (args.length == 1) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_NUMBER));
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        Section section = new ArenaSection(price, args[0]);

        arena.getSections().add(section);

        game.getDataFile().set("arena." + arena.getName() + ".section." + args[0] + ".price", price);
        game.getDataFile().save();

        player.sendMessage(messageHelper.create(TranslationKey.SECTION_CREATE,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_section", section.getName())
        ));
    }
}
