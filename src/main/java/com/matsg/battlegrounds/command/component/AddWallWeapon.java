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
import com.matsg.battlegrounds.ItemFinder;
import com.matsg.battlegrounds.mode.zombies.component.WallWeapon;
import com.matsg.battlegrounds.mode.zombies.component.factory.WallWeaponFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

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
        ItemFrame itemFrame = null;

        double range = 2.0;

        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity.getType() == EntityType.ITEM_FRAME) {
                itemFrame = (ItemFrame) entity;
            }
        }

        if (itemFrame == null) {
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

        WallWeaponFactory wallWeaponFactory = zombies.getWallWeaponFactory();
        WallWeapon wallWeapon = wallWeaponFactory.make(componentId, itemFrame, weapon, price);

        section.getWallWeaponContainer().add(wallWeapon);

        itemFrame.setItem(weapon.getItemStack());

        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", itemFrame.getLocation(), true);
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
