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

        if (args.length >= 1) {
            try {
                teamId = Integer.parseInt(args[0]);
            } catch (Exception e) {
                player.sendMessage(messageHelper.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[0])));
                return;
            }
        }

        if (args.length >= 2) {
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

        arena.getSpawnContainer().add(spawn);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".base", teamBase);
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", spawn.getLocation(), false);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".team", teamId);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "spawn");
        game.getDataFile().save();

        player.sendMessage(messageHelper.create(TranslationKey.SPAWN_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId)
        ));
    }
}
