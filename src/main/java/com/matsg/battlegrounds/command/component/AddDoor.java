package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Selection;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.mode.zombies.component.Door;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.Section;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameModeUsageValidator;
import com.matsg.battlegrounds.command.validator.SectionNameValidator;
import com.matsg.battlegrounds.mode.GameModeType;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesDoor;
import com.matsg.battlegrounds.mode.zombies.Zombies;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class AddDoor extends ComponentCommand {

    private int sectionPos;
    private Translator translator;

    public AddDoor(Battlegrounds plugin, Translator translator) {
        super(plugin);
        this.translator = translator;
        this.sectionPos = 4;

        registerValidator(new GameModeUsageValidator(plugin, translator, GameModeType.ZOMBIES));
        registerValidator(new SectionNameValidator(plugin, translator, sectionPos));
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Player player = context.getPlayer();
        Game game = context.getGame();
        Arena arena = context.getArena();

        Zombies zombies = game.getGameMode(Zombies.class);
        Section section = zombies.getSection(args[sectionPos]);
        Selection selection = plugin.getSelectionManager().getSelection(player);

        if (selection == null || !selection.isComplete()) {
            player.sendMessage(translator.translate(TranslationKey.NO_SELECTION));
            return;
        }

        Block block = selection.getCenter().getBlock();

        Door door = new ZombiesDoor(
                componentId,
                game,
                section,
                selection.getWorld(),
                selection.getMaximumPoint(),
                selection.getMinimumPoint(),
                block.getType()
        );

        section.getDoorContainer().add(door);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".material", block.getType().toString());
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".max", selection.getMaximumPoint(), true);
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".min", selection.getMinimumPoint(), true);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".section", section.getName());
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "door");
        game.getDataFile().save();

        player.sendMessage(translator.translate(TranslationKey.DOOR_ADD,
                new Placeholder("bg_component_id", componentId),
                new Placeholder("bg_section", section.getName())
        ));
    }
}
