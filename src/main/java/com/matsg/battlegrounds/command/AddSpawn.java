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
                "bg addspawn [id] [arena] [teamid]", "battlegrounds.addspawn", true, "as");
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

        boolean teamBase = false;
        int teamId = 0;

        if (args.length >= 4) {
            try {
                teamId = Integer.parseInt(args[3]);
            } catch (Exception e) {
                EnumMessage.INVALID_ARGUMENT_TYPE.send(sender, new Placeholder("bg_arg", args[3]));
                return;
            }
        }

        if (args.length >= 5) {
            teamBase = args[4].equals("-b");
        }

        Spawn spawn = new ArenaSpawn(getSpawnIndex(arena), player.getLocation(), teamId);
        spawn.setTeamBase(teamBase);

        arena.getSpawns().add(spawn);

        game.getDataFile().set("arena." + name + ".spawn." + spawn.getIndex() + ".base", teamBase);
        game.getDataFile().setLocation("arena." + name + ".spawn." + spawn.getIndex() + ".location", spawn.getLocation(), true);
        game.getDataFile().set("arena." + name + ".spawn." + spawn.getIndex() + ".team", teamId);
        game.getDataFile().save();

        player.sendMessage(EnumMessage.SPAWN_ADD.getMessage(
                new Placeholder("bg_arena", name),
                new Placeholder("bg_index", spawn.getIndex())));
    }

    private int getSpawnIndex(Arena arena) {
        int i = 1;
        loop: while (true) {
            for (Spawn spawn : arena.getSpawns()) {
                if (spawn.getIndex() == i) {
                    continue loop;
                }
            }
            return i;
        }
    }

    private boolean hasTeamBase(Arena arena) {
        for (Spawn spawn : arena.getSpawns()) {
            if (spawn.isTeamBase()) {
                return true;
            }
        }
        return false;
    }
}