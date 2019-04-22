package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validate.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validate.SectionNameValidator;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.game.mode.zombies.ItemChest;
import com.matsg.battlegrounds.game.mode.zombies.Section;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.Set;

public class AddItemChest extends ComponentCommand {

    private ItemFinder itemFinder;
    private MessageHelper messageHelper;

    public AddItemChest(Battlegrounds plugin) {
        super(plugin);
        this.itemFinder = new ItemFinder(plugin);
        this.messageHelper = new MessageHelper();

        registerValidator(new GameModeUsageValidator(plugin, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        BlockState blockState = player.getTargetBlock((Set<Material>) null, 5).getState();

        if (!(blockState instanceof Chest)) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_BLOCK));
            return;
        }

        Game game = context.getGame();
        Arena arena = context.getArena();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[3]);

        if (args.length == 4) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_WEAPON_ID));
            return;
        }

        Weapon weapon = itemFinder.findWeapon(args[4]);

        if (weapon == null) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_WEAPON, new Placeholder("bg_weapon", args[1])));
            return;
        }

        if (args.length == 5) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_ITEM_PRICE));
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[5]);
        } catch (Exception e) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[5])));
            return;
        }

        ItemChest itemChest = new ItemChest(
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

        player.sendMessage(messageHelper.create(TranslationKey.ITEMCHEST_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId)
        ));
    }
}
