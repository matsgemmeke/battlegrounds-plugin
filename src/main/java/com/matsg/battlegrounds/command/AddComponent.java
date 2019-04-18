package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.component.*;
import com.matsg.battlegrounds.command.validate.ArenaNameValidator;
import com.matsg.battlegrounds.command.validate.GameIdValidator;
import com.matsg.battlegrounds.item.ItemFinder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddComponent extends SubCommand {

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
        commands.put("itemchest", new AddItemChest(new ItemFinder(plugin)));
        commands.put("mobspawn", new AddMobSpawn());
//        commands.put("mysterybox", new AddMysteryBox(plugin));
//        commands.put("perkmachine", new AddPerkMachine(plugin));
        commands.put("section", new AddSection());
        commands.put("spawn", new AddSpawn());

        registerValidator(new GameIdValidator(plugin));
        registerValidator(new ArenaNameValidator(plugin));
    }

    public void executeSubCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Game game = plugin.getGameManager().getGame(Integer.parseInt(args[1]));
        Arena arena = plugin.getGameManager().getArena(game, args[2].replaceAll("_", " "));

        if (args.length == 3) {
            player.sendMessage(createMessage(TranslationKey.SPECIFY_COMPONENT_TYPE));
            return;
        }

        Section section = arena.getSection(args[3]);
        int typePos = section != null ? 4 : 3;

        if (args.length == typePos) {
            player.sendMessage(createMessage(TranslationKey.SPECIFY_COMPONENT_TYPE));
            return;
        }

        ComponentCommand command = commands.get(args[typePos]);

        if (command == null) {
            player.sendMessage(createMessage(TranslationKey.INVALID_COMPONENT_TYPE, new Placeholder("bg_component", args[typePos])));
            return;
        }

        String[] splitArgs = Arrays.copyOfRange(args, typePos + 1, args.length);

        ComponentContext context = new ComponentContext();
        context.setArena(arena);
        context.setGame(game);
        context.setPlayer(player);
        context.setSection(section);

        command.execute(context, getFirstAvailableId(arena), splitArgs);
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
