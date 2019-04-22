package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validate.GameModeUsageValidator;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.game.mode.zombies.Section;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.entity.Player;

public class AddSection extends ComponentCommand {

    private MessageHelper messageHelper;

    public AddSection(Battlegrounds plugin) {
        super(plugin);
        this.messageHelper = new MessageHelper();

        registerValidator(new GameModeUsageValidator(plugin, GameModeType.ZOMBIES));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Arena arena = context.getArena();
        Game game = context.getGame();
        Player player = context.getPlayer();

        Zombies zombies = game.getGameMode(Zombies.class);

        if (args.length == 3) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_SECTION_NAME));
            return;
        }

        if (zombies.getSection(args[3]) != null) {
            player.sendMessage(messageHelper.create(TranslationKey.SECTION_EXISTS,
                    new Placeholder("bg_arena", arena.getName()),
                    new Placeholder("bg_section", args[3])
            ));
            return;
        }

        if (args.length == 4) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_SECTION_PRICE));
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[4]);
        } catch (Exception e) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[4])));
            return;
        }

        Section section = new Section(componentId, args[3]);
        section.setPrice(price);

        zombies.getSectionContainer().add(section);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".name", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".price", price);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "section");
        game.getDataFile().save();

        player.sendMessage(messageHelper.create(TranslationKey.SECTION_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId),
                new Placeholder("bg_section", section.getName())
        ));
    }
}
