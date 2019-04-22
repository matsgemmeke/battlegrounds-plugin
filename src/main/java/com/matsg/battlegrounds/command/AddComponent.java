package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.component.*;
import com.matsg.battlegrounds.command.validate.ArenaNameValidator;
import com.matsg.battlegrounds.command.validate.GameIdValidator;
import com.matsg.battlegrounds.command.validate.ValidationResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AddComponent extends Command {

    private Map<String, ComponentCommand> commands;

    public AddComponent(Battlegrounds plugin) {
        super(plugin);
        setAliases("ac", "add");
        setDescription(createMessage(TranslationKey.DESCRIPTION_ADDCOMPONENT));
        setName("addcomponent");
        setPermissionNode("bg.addcomponent");
        setPlayerOnly(true);
        setUsage("bg addcomponent [id] [arena] [section] [component]");

        commands = new HashMap<>();
        commands.put("door", new AddDoor(plugin));
        commands.put("itemchest", new AddItemChest(plugin));
        commands.put("mobspawn", new AddMobSpawn(plugin));
        commands.put("mysterybox", new AddMysteryBox(plugin));
        commands.put("perk", new AddPerkMachine(plugin));
        commands.put("section", new AddSection(plugin));
        commands.put("spawn", new AddSpawn(plugin));

        registerValidator(new GameIdValidator(plugin));
        registerValidator(new ArenaNameValidator(plugin));
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Game game = plugin.getGameManager().getGame(Integer.parseInt(args[1]));
        Arena arena = plugin.getGameManager().getArena(game, args[2].replaceAll("_", " "));

        if (args.length == 3) {
            player.sendMessage(createMessage(TranslationKey.SPECIFY_COMPONENT_TYPE));
            return;
        }

        ComponentCommand command = commands.get(args[3]);

        if (command == null) {
            player.sendMessage(createMessage(TranslationKey.INVALID_COMPONENT_TYPE, new Placeholder("bg_component", args[3])));
            return;
        }

        ComponentContext context = new ComponentContext();
        context.setArena(arena);
        context.setGame(game);
        context.setPlayer(player);

        // Validate the input for the component command
        ValidationResponse response = command.validateInput(args);

        if (!response.passed()) {
            player.sendMessage(response.getMessage());
        }

        // Order of input arguments:
        // 0: Addcomponent
        // 1: Game id
        // 2: Arena name
        // 3: Component type
        // 4: Section name if required
        // >4: Anything else
        command.execute(context, getFirstAvailableId(arena), args);
    }

    private int getFirstAvailableId(Arena arena) {
        int i = 1;
        while (true) {
            if (arena.getComponent(i) == null) {
                return i;
            }
            i ++;
        }
    }
}
