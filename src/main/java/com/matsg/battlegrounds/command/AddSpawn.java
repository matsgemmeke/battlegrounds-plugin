package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validate.ArenaNameValidator;
import com.matsg.battlegrounds.command.validate.GameIdValidator;
import com.matsg.battlegrounds.game.ArenaSpawn;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddSpawn extends SubCommand {

    public AddSpawn(Battlegrounds plugin) {
        super(plugin);
        setAliases("as");
        setDescription(createMessage(TranslationKey.DESCRIPTION_ADDSPAWN));
        setName("addspawn");
        setPermissionNode("battlegrounds.addspawn");
        setPlayerOnly(true);
        setUsage("bg addspawn [id] [arena] [teamid]");

        registerValidator(new GameIdValidator(plugin));
        registerValidator(new ArenaNameValidator(plugin));
    }

    public void executeSubCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        int id = Integer.parseInt(args[1]);

        Game game = plugin.getGameManager().getGame(id);
        String arenaName = args[2].replaceAll("_", " ");
        Arena arena = plugin.getGameManager().getArena(game, arenaName);

        boolean teamBase = false;
        int teamId = 0;

        if (args.length >= 4) {
            try {
                teamId = Integer.parseInt(args[3]);
            } catch (Exception e) {
                player.sendMessage(createMessage(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[3])));
                return;
            }
        }

        if (args.length >= 5) {
            teamBase = args[4].equals("-b");

            if (teamBase && arena.getTeamBase(teamId) != null) {
                player.sendMessage(createMessage(TranslationKey.SPAWN_TEAMBASE_EXISTS,
                        new Placeholder("bg_arena", arena.getName()),
                        new Placeholder("bg_team", teamId)));
                return;
            }
        }

        Spawn spawn = new ArenaSpawn(getFirstAvailableIndex(arena), player.getLocation(), teamId);
        spawn.setTeamBase(teamBase);

        arena.getSpawns().add(spawn);

        game.getDataFile().set("arena." + arenaName + ".spawn." + spawn.getIndex() + ".base", teamBase);
        game.getDataFile().setLocation("arena." + arenaName + ".spawn." + spawn.getIndex() + ".location", spawn.getLocation(), true);
        game.getDataFile().set("arena." + arenaName + ".spawn." + spawn.getIndex() + ".team", teamId);
        game.getDataFile().save();

        player.sendMessage(createMessage(TranslationKey.SPAWN_ADD,
                new Placeholder("bg_arena", arenaName),
                new Placeholder("bg_index", spawn.getIndex())
        ));
    }

    private int getFirstAvailableIndex(Arena arena) {
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
}
