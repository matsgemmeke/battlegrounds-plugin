package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.ArenaSpawn;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddSpawn extends SubCommand {

    public AddSpawn(Battlegrounds plugin) {
        super(plugin, "addspawn", EnumMessage.DESCRIPTION_ADDSPAWN.getMessage(),
                "bg addspawn [id] [arena]", "battlegrounds.addspawn", true, "as");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            EnumMessage.SPECIFY_ID.send(sender);
            return;
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            EnumMessage.INVALID_ARGUMENT_TYPE.send(sender, new Placeholder("bg_arg", args[1]));
            return;
        }

        if (!plugin.getGameManager().exists(id)) {
            EnumMessage.GAME_NOT_EXISTS.send(sender, new Placeholder("bg_game", id));
            return;
        }

        Game game = plugin.getGameManager().getGame(id);

        if (args.length == 2) {
            EnumMessage.SPECIFY_NAME.send(sender);
            return;
        }

        String name = args[2].replaceAll("_", " ");
        Arena arena = plugin.getGameManager().getArena(game, name);

        if (arena == null) {
            EnumMessage.ARENA_NOT_EXISTS.send(sender, new Placeholder("bg_game", id), new Placeholder("bg_arena", name));
            return;
        }

        Spawn spawn = new ArenaSpawn(player.getLocation());

        arena.getSpawns().add(spawn);

        game.getDataFile().setLocation("arena." + name + ".spawn." + arena.getSpawns().size(), spawn.getLocation(), true);
        game.getDataFile().save();

        player.sendMessage(EnumMessage.SPAWN_ADD.getMessage(
                new Placeholder("zombies_arena", name),
                new Placeholder("zombies_id", arena.getSpawns().size())));
    }
}