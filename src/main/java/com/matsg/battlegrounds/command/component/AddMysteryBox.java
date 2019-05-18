package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.MysteryBox;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validator.SectionNameValidator;
import com.matsg.battlegrounds.game.mode.GameModeType;
import com.matsg.battlegrounds.game.mode.zombies.Zombies;
import com.matsg.battlegrounds.game.mode.zombies.ZombiesMysteryBox;
import com.matsg.battlegrounds.util.MessageHelper;
import com.matsg.battlegrounds.util.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;

import java.util.Set;

public class AddMysteryBox extends ComponentCommand {

    private MessageHelper messageHelper;

    public AddMysteryBox(Battlegrounds plugin) {
        super(plugin);
        this.messageHelper = new MessageHelper();

        registerValidator(new GameModeUsageValidator(plugin, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin, 4));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        BlockState blockState = player.getTargetBlock((Set<Material>) null, 5).getState();

        if (!(blockState instanceof Chest) || !(((Chest) blockState).getInventory().getHolder() instanceof DoubleChest)) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_BLOCK));
            return;
        }

        Block attachedBlock = null;
        DoubleChest doubleChest = (DoubleChest) ((Chest) blockState).getInventory().getHolder();

        for (BlockFace face : BlockFace.values()) {
            if (doubleChest.getLocation().getBlock().getRelative(face).getType() == Material.CHEST) {
                attachedBlock = doubleChest.getLocation().getBlock().getRelative(face);
                break;
            }
        }

        if (attachedBlock == null) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_BLOCK));
            return;
        }

        Game game = context.getGame();
        Arena arena = context.getArena();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[4]);

        if (args.length == 5) {
            player.sendMessage(messageHelper.create(TranslationKey.SPECIFY_MYSTERYBOX_PRICE));
            return;
        }

        int price;

        try {
            price = Integer.parseInt(args[5]);
        } catch (Exception e) {
            player.sendMessage(messageHelper.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[5])));
            return;
        }

        Location max = new Location(
                doubleChest.getWorld(),
                Math.max(doubleChest.getX(), attachedBlock.getX()),
                Math.max(doubleChest.getY(), attachedBlock.getY()),
                Math.max(doubleChest.getZ(), attachedBlock.getZ())
        );

        // The location of the double chest always refers to the block with the lowest coordinates.
        Pair<Block, Block> blocks = new Pair<>(doubleChest.getLocation().getBlock(), max.getBlock());

        MysteryBox mysteryBox = new ZombiesMysteryBox(componentId, game, price, blocks);
        mysteryBox.setActive(false);

        section.getMysteryBoxContainer().add(mysteryBox);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".direction", blockState.getData().getData());
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".leftside", doubleChest.getLocation(), true);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".price", price);
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".rightside", max, true);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "mysterybox");
        game.getDataFile().save();

        player.sendMessage(messageHelper.create(TranslationKey.MYSTERYBOX_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId)
        ));
    }
}