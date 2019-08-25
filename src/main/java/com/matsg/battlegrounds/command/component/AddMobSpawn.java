package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Selection;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Barricade;
import com.matsg.battlegrounds.mode.zombies.component.MobSpawn;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validator.SectionNameValidator;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.factory.BarricadeFactory;
import com.matsg.battlegrounds.mode.zombies.component.factory.MobSpawnFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class AddMobSpawn extends ComponentCommand {

    private int sectionPos;
    private Translator translator;

    public AddMobSpawn(Battlegrounds plugin, Translator translator) {
        super(plugin);
        this.translator = translator;
        this.sectionPos = 4;

        registerValidator(new GameModeUsageValidator(plugin, translator, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin, translator, sectionPos));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        Arena arena = context.getArena();
        Game game = context.getGame();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[sectionPos]);

        Location location = player.getLocation();

        MobSpawnFactory mobSpawnFactory = zombies.getMobSpawnFactory();
        MobSpawn mobSpawn = mobSpawnFactory.make(componentId, location);

        section.getMobSpawnContainer().add(mobSpawn);

        Selection selection = plugin.getSelectionManager().getSelection(player);
        TranslationKey key = TranslationKey.MOBSPAWN_ADD_NO_BARRICADE;

        // Save the mob spawn before optionally adding a barricade
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", player.getLocation(), false);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "mobspawn");

        // Check whether the player has made selection and wants to add a barricade
        if (args.length >= 5 && args[5].equalsIgnoreCase("-b") && selection != null && selection.isComplete()) {
            int barricadeId = game.findAvailableComponentId();
            Location maximumPoint = selection.getMaximumPoint();
            Location minimumPoint = selection.getMinimumPoint();
            World world = selection.getWorld();
            Material material = selection.getCenter().getBlock().getType();

            BarricadeFactory barricadeFactory = zombies.getBarricadeFactory();
            Barricade barricade = barricadeFactory.make(barricadeId, mobSpawn, maximumPoint, minimumPoint, world, material);

            section.getBarricadeContainer().add(barricade);

            // Save other barricade attributes as an individual component
            game.getDataFile().set("arena." + arena.getName() + ".component." + barricadeId + ".material", material.toString());
            game.getDataFile().setLocation("arena." + arena.getName() + ".component." + barricadeId + ".max", maximumPoint, false);
            game.getDataFile().setLocation("arena." + arena.getName() + ".component." + barricadeId + ".min", minimumPoint, false);
            game.getDataFile().set("arena." + arena.getName() + ".component." + barricadeId + ".mobspawn", componentId);
            game.getDataFile().set("arena." + arena.getName() + ".component." + barricadeId + ".section", section.getName());
            game.getDataFile().set("arena." + arena.getName() + ".component." + barricadeId + ".type", "barricade");

            key = TranslationKey.MOBSPAWN_ADD_BARRICADE;
        }

        game.getDataFile().save();

        player.sendMessage(translator.translate(key,
                new Placeholder("bg_id", componentId),
                new Placeholder("bg_section", section.getName())
        ));
    }
}
