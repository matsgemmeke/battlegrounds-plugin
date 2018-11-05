package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.ArenaSpawn;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddSpawn extends SubCommand {

    public AddSpawn(Battlegrounds plugin) {
        super(plugin, "addspawn", Message.create(TranslationKey.DESCRIPTION_ADDSPAWN),
                "bg addspawn [id] [arena] [teamid]", "battlegrounds.addspawn", true, "as");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            player.sendMessage(Message.create(TranslationKey.SPECIFY_ID));
            return;
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            player.sendMessage(Message.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        if (!plugin.getGameManager().exists(id)) {
            player.sendMessage(Message.create(TranslationKey.GAME_NOT_EXISTS, new Placeholder("bg_game", id)));
            return;
        }

        Game game = plugin.getGameManager().getGame(id);

        if (args.length == 2) {
            player.sendMessage(Message.create(TranslationKey.SPECIFY_NAME));
            return;
        }

        String name = args[2].replaceAll("_", " ");
        Arena arena = plugin.getGameManager().getArena(game, name);

        if (arena == null) {
            player.sendMessage(Message.create(TranslationKey.ARENA_NOT_EXISTS,
                    new Placeholder("bg_game", id),
                    new Placeholder("bg_arena", name)));
            return;
        }

        boolean teamBase = false;
        int teamId = 0;

        if (args.length >= 4) {
            try {
                teamId = Integer.parseInt(args[3]);
            } catch (Exception e) {
                player.sendMessage(Message.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[3])));
                return;
            }
        }

        if (args.length >= 5) {
            teamBase = args[4].equals("-b");

            if (teamBase && hasTeamBase(arena, teamId)) {
                player.sendMessage(Message.create(TranslationKey.SPAWN_TEAMBASE_EXISTS,
                        new Placeholder("bg_arena", arena.getName()),
                        new Placeholder("bg_team", teamId)));
                return;
            }
        }

        Spawn spawn = new ArenaSpawn(getSpawnIndex(arena), player.getLocation(), teamId);
        spawn.setTeamBase(teamBase);

        arena.getSpawns().add(spawn);

        game.getDataFile().set("arena." + name + ".spawn." + spawn.getIndex() + ".base", teamBase);
        game.getDataFile().setLocation("arena." + name + ".spawn." + spawn.getIndex() + ".location", spawn.getLocation(), true);
        game.getDataFile().set("arena." + name + ".spawn." + spawn.getIndex() + ".team", teamId);
        game.getDataFile().save();

        player.sendMessage(Message.create(TranslationKey.SPAWN_ADD,
                new Placeholder("bg_arena", name),
                new Placeholder("bg_index", spawn.getIndex())));
    }

    private int getSpawnIndex(Arena arena) {
        int i = 1;
        loop: while (true) {
            for (Spawn spawn : arena.getSpawns()) {
                if (spawn.getIndex() == i) {
                    i ++;
                    continue loop;
                }
            }
            return i;
        }
    }

    private boolean hasTeamBase(Arena arena, int teamid) {
        for (Spawn spawn : arena.getSpawns()) {
            if (spawn.isTeamBase() && spawn.getTeamId() == teamid) {
                return true;
            }
        }
        return false;
    }
}