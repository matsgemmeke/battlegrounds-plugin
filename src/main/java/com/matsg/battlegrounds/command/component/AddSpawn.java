package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.component.ArenaSpawn;
import com.matsg.battlegrounds.util.MessageHelper;
import org.bukkit.entity.Player;

public class AddSpawn implements ComponentCommand {

    private MessageHelper messageHelper;

    public AddSpawn() {
        this.messageHelper = new MessageHelper();
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Arena arena = context.getArena();
        Game game = context.getGame();
        Player player = context.getPlayer();

        boolean teamBase = false;
        int teamId = 0;

        if (args.length >= 0) {
            try {
                teamId = Integer.parseInt(args[0]);
            } catch (Exception e) {
                player.sendMessage(messageHelper.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[3])));
                return;
            }
        }

        if (args.length >= 1) {
            teamBase = args[1].equals("-b");

            if (teamBase && arena.getTeamBase(teamId) != null) {
                player.sendMessage(messageHelper.create(TranslationKey.SPAWN_TEAMBASE_EXISTS,
                        new Placeholder("bg_arena", arena.getName()),
                        new Placeholder("bg_team", teamId)));
                return;
            }
        }

        Spawn spawn = new ArenaSpawn(componentId, player.getLocation(), teamId);
        spawn.setTeamBase(teamBase);

        arena.getSpawns().add(spawn);

        game.getDataFile().set("arena." + arena.getName() + ".spawn." + spawn.getId() + ".base", teamBase);
        game.getDataFile().setLocation("arena." + arena.getName() + ".spawn." + spawn.getId() + ".location", spawn.getLocation(), true);
        game.getDataFile().set("arena." + arena.getName() + ".spawn." + spawn.getId() + ".team", teamId);
        game.getDataFile().save();

        player.sendMessage(messageHelper.create(TranslationKey.SPAWN_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_index", spawn.getId())
        ));
    }
}
