package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.ItemChest;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.component.ArenaItemChest;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.Set;

public class AddItemChest implements ComponentCommand {

    private ItemFinder itemFinder;
    private MessageHelper messageHelper;

    public AddItemChest(ItemFinder itemFinder) {
        this.itemFinder = itemFinder;
        this.messageHelper = new MessageHelper();
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
        Section section = context.getSection();

        if (section == null) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_SECTION_NAME));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_WEAPON_ID));
            return;
        }

        Weapon weapon = itemFinder.findWeapon(args[0]);

        if (weapon == null) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_WEAPON, new Placeholder("bg_weapon", args[0])));
            return;
        }

        if (args.length == 1) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_ITEM_PRICE));
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        ItemChest itemChest = new ArenaItemChest(componentId, (Chest) blockState, weapon, weapon.getItemStack(), price);

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
