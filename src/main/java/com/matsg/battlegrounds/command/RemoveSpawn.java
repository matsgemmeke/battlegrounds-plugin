package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;

public class RemoveSpawn extends SubCommand {

    public RemoveSpawn(Battlegrounds plugin) {
        super(plugin, "removespawn", Message.create(TranslationKey.DESCRIPTION_REMOVESPAWN),
                "bg removespawn [id] [arena] [index]", "battlegrounds.removespawn", false, "rs");
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(Message.create(TranslationKey.SPECIFY_ID));
            return;
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(Message.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        if (!plugin.getGameManager().exists(id)) {
            sender.sendMessage(Message.create(TranslationKey.GAME_NOT_EXISTS, new Placeholder("bg_game", id)));
            return;
        }

        Game game = plugin.getGameManager().getGame(id);

        if (args.length == 2) {
            sender.sendMessage(Message.create(TranslationKey.SPECIFY_NAME));
            return;
        }

        String name = args[2].replaceAll("_", " ");
        Arena arena = plugin.getGameManager().getArena(game, name);

        if (arena == null) {
            sender.sendMessage(Message.create(TranslationKey.ARENA_NOT_EXISTS,
                    new Placeholder("bg_game", id),
                    new Placeholder("bg_arena", name)));
            return;
        }

        if (args.length == 3) {
            sender.sendMessage(Message.create(TranslationKey.SPECIFY_INDEX));
            return;
        }

        int index;

        try {
            index = Integer.parseInt(args[3]);
        } catch (Exception e) {
            sender.sendMessage(Message.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[3])));
            return;
        }

        Spawn spawn = arena.getSpawn(index);

        if (spawn == null) {
            sender.sendMessage(Message.create(TranslationKey.SPAWN_NOT_EXISTS,
                    new Placeholder("bg_arena", arena.getName()),
                    new Placeholder("bg_index", index)));
            return;
        }

        arena.getSpawns().remove(spawn);
        game.getDataFile().set("arena." + arena.getName() + ".spawn." + index, null);
        game.getDataFile().save();

        sender.sendMessage(Message.create(TranslationKey.SPAWN_REMOVE,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_index", index)));
    }
}