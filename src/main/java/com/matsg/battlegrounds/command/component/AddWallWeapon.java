package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validator.SectionNameValidator;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.item.ItemFinder;
import com.matsg.battlegrounds.mode.zombies.component.WallWeapon;
import com.matsg.battlegrounds.mode.zombies.component.factory.WallWeaponFactory;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.Set;

public class AddWallWeapon extends ComponentCommand {

    private int sectionPos;
    private ItemFinder itemFinder;
    private Translator translator;

    public AddWallWeapon(Battlegrounds plugin, Translator translator) {
        super(plugin);
        this.translator = translator;
        this.itemFinder = new ItemFinder(plugin);
        this.sectionPos = 4;

        registerValidator(new GameModeUsageValidator(plugin, translator, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin, translator, sectionPos));
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
        Section section = zombies.getSection(args[sectionPos]);

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

        Chest chest = (Chest) blockState;

        WallWeaponFactory wallWeaponFactory = zombies.getWallWeaponFactory();
        WallWeapon wallWeapon = wallWeaponFactory.make(componentId, chest, weapon, price);

        section.getWallWeaponContainer().add(wallWeapon);

        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", blockState.getLocation(), true);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".price", price);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "wallweapon");
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".weapon", weapon.getName());
        game.getDataFile().save();

        player.sendMessage(translator.translate(TranslationKey.ITEMCHEST_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId)
        ));
    }
}
