package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.*;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.command.Command;
import com.matsg.battlegrounds.mode.zombies.component.Door;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validator.SectionNameValidator;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import com.matsg.battlegrounds.mode.zombies.component.factory.DoorFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class AddDoor extends ComponentCommand {

    private int sectionPos;
    private SelectionManager selectionManager;

    public AddDoor(Translator translator, GameManager gameManager, SelectionManager selectionManager) {
        super(translator);
        this.selectionManager = selectionManager;
        this.sectionPos = 4;

        registerValidator(new GameModeUsageValidator(gameManager, translator, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(gameManager, translator, sectionPos));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        Game game = context.getGame();
        Arena arena = context.getArena();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[sectionPos]);
        Selection selection = selectionManager.getSelection(player);

        if (selection == null || !selection.isComplete()) {
            player.sendMessage(translator.translate(TranslationKey.NO_SELECTION.getPath()));
            return;
        }

        Location maximumPoint = selection.getMaximumPoint();
        Location minimumPoint = selection.getMinimumPoint();
        World world = selection.getWorld();
        Material material = selection.getCenter().getBlock().getType();

        DoorFactory doorFactory = zombies.getDoorFactory();
        Door door = doorFactory.make(componentId, section, world, maximumPoint, minimumPoint, material);

        section.getDoorContainer().add(door);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".material", material.toString());
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".max", selection.getMaximumPoint(), true);
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".min", selection.getMinimumPoint(), true);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "door");
        game.getDataFile().save();

        player.sendMessage(translator.translate(TranslationKey.DOOR_ADD.getPath(),
                new Placeholder("bg_component_id", componentId),
                new Placeholder("bg_section", section.getName())
        ));
    }
}
