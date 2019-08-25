package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.factory.SectionFactory;
import org.bukkit.entity.Player;

public class AddSection extends ComponentCommand {

    private Translator translator;

    public AddSection(Battlegrounds plugin, Translator translator) {
        super(plugin);
        this.translator = translator;

        registerValidator(new GameModeUsageValidator(plugin, translator, GameModeType.ZOMBIES));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Arena arena = context.getArena();
        Game game = context.getGame();
        Player player = context.getPlayer();

        Zombies zombies = game.getGameMode(Zombies.class);

        if (args.length == 4) {
            player.sendMessage(translator.translate(TranslationKey.SPECIFY_SECTION_NAME));
            return;
        }

        String sectionName = args[4];

        if (zombies.getSection(sectionName) != null) {
            player.sendMessage(translator.translate(TranslationKey.SECTION_EXISTS,
                    new Placeholder("bg_arena", arena.getName()),
                    new Placeholder("bg_section", args[3])
            ));
            return;
        }

        if (args.length == 5) {
            player.sendMessage(translator.translate(TranslationKey.SPECIFY_SECTION_PRICE));
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[5]);
        } catch (Exception e) {
            player.sendMessage(translator.translate(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[5])));
            return;
        }

        boolean unlockedByDefault = zombies.getSectionContainer().getAll().size() <= 0;

        SectionFactory sectionFactory = zombies.getSectionFactory();
        Section section = sectionFactory.make(componentId, sectionName, price, unlockedByDefault);

        zombies.getSectionContainer().add(section);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".name", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".name", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".price", price);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "section");
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".unlocked", section.isUnlockedByDefault());
        game.getDataFile().save();

        player.sendMessage(translator.translate(TranslationKey.SECTION_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId),
                new Placeholder("bg_section", section.getName())
        ));
    }
}
