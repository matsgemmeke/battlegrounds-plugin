package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.ItemChest;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validator.SectionNameValidator;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesItemChest;
import com.matsg.battlegrounds.item.ItemFinder;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.Set;

public class AddItemChest extends ComponentCommand {

    private ItemFinder itemFinder;
    private Translator translator;

    public AddItemChest(Battlegrounds plugin, Translator translator) {
        super(plugin);
        this.translator = translator;
        this.itemFinder = new ItemFinder(plugin);

        registerValidator(new GameModeUsageValidator(plugin, translator, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin, translator, 4));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        BlockState blockState = player.getTargetBlock((Set<Material>) null, 5).getState();

        if (!(blockState instanceof Chest)) {
            player.sendMessage(translator.translate(TranslationKey.INVALID_BLOCK));
            return;
        }

        Game game = context.getGame();
        Arena arena = context.getArena();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[4]);

        if (args.length == 5) {
            player.sendMessage(translator.translate(TranslationKey.SPECIFY_WEAPON_ID));
            return;
        }

        Weapon weapon = itemFinder.findWeapon(args[5]);

        if (weapon == null) {
            player.sendMessage(translator.translate(TranslationKey.INVALID_WEAPON, new Placeholder("bg_weapon", args[5])));
            return;
        }

        if (args.length == 6) {
            player.sendMessage(translator.translate(TranslationKey.SPECIFY_ITEM_PRICE));
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[6]);
        } catch (Exception e) {
            player.sendMessage(translator.translate(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[6])));
            return;
        }

        ItemChest itemChest = new ZombiesItemChest(
                componentId,
                (Chest) blockState,
                weapon,
                weapon.getName(),
                weapon.getItemStack(),
                price
        );

        section.getItemChestContainer().add(itemChest);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".item", weapon.getName());
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", blockState.getLocation(), true);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".price", price);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "itemchest");
        game.getDataFile().save();

        player.sendMessage(translator.translate(TranslationKey.ITEMCHEST_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId)
        ));
    }
}
