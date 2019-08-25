package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.PerkMachine;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.mode.zombies.component.factory.PerkMachineFactory;
import com.matsg.battlegrounds.mode.zombies.item.Perk;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validator.SectionNameValidator;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.item.factory.PerkFactory;
import com.matsg.battlegrounds.mode.zombies.item.PerkEffectType;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddPerkMachine extends ComponentCommand {

    private int sectionPos;
    private PerkFactory perkFactory;
    private Translator translator;

    public AddPerkMachine(Battlegrounds plugin, Translator translator) {
        super(plugin);
        this.translator = translator;
        this.perkFactory = new PerkFactory(plugin, translator);
        this.sectionPos = 4;

        registerValidator(new GameModeUsageValidator(plugin, translator, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin, translator, sectionPos));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        BlockState blockState = player.getTargetBlock((Set<Material>) null, 5).getState();

        if (!(blockState instanceof Sign)) {
            player.sendMessage(translator.translate(TranslationKey.INVALID_BLOCK));
            return;
        }

        Game game = context.getGame();
        Arena arena = context.getArena();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[sectionPos]);

        if (args.length == 5) {
            player.sendMessage(translator.translate(TranslationKey.SPECIFY_PERK_TYPE));
            return;
        }

        PerkEffectType perkEffectType;

        try {
            perkEffectType = PerkEffectType.valueOf(args[5]);
        } catch (IllegalArgumentException e) {
            player.sendMessage(translator.translate(TranslationKey.INVALID_PERK_TYPE, new Placeholder("bg_perk", args[5])));
            return;
        }

        if (args.length == 6) {
            player.sendMessage(translator.translate(TranslationKey.SPECIFY_PERK_PRICE));
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[6]);
        } catch (Exception e) {
            player.sendMessage(translator.translate(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[6])));
            return;
        }

        int maxBuys = 3;

        if (args.length > 7) {
            try {
                maxBuys = Integer.parseInt(args[7]);
            } catch (IllegalArgumentException e) {
                player.sendMessage(translator.translate(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[7])));
                return;
            }
        }

        Perk perk = perkFactory.make(perkEffectType);
        Sign sign = (Sign) blockState;

        PerkMachineFactory perkMachineFactory = zombies.getPerkMachineFactory();
        PerkMachine perkMachine = perkMachineFactory.make(componentId, sign, perk, maxBuys, price);

        section.getPerkMachineContainer().add(perkMachine);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".effect", perkEffectType.toString());
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", blockState.getLocation(), true);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".maxbuys", maxBuys);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".price", price);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "perkmachine");
        game.getDataFile().save();

        player.sendMessage(translator.translate(TranslationKey.PERKMACHINE_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId),
                new Placeholder("bg_perk", perk.getName())
        ));
    }
}
