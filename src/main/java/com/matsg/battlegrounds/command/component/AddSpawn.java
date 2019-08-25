package com.matsg.battlegrounds.command.component;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.game.ArenaSpawn;
import org.bukkit.entity.Player;

public class AddSpawn extends ComponentCommand {

    private Translator translator;

    public AddSpawn(Battlegrounds plugin, Translator translator) {
        super(plugin);
        this.translator = translator;
    }

    public void execute(ComponentContext context, int componentId, String[] args) {
        Arena arena = context.getArena();
        Game game = context.getGame();
        Player player = context.getPlayer();

        boolean teamBase = false;
        int teamId = 0;

        if (args.length >= 5) {
            try {
                teamId = Integer.parseInt(args[4]);
            } catch (Exception e) {
                player.sendMessage(translator.translate(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[4])));
                return;
            }
        }

        if (args.length >= 6) {
            teamBase = args[5].equals("-b");

            if (teamBase && arena.getTeamBase(teamId) != null) {
                player.sendMessage(translator.translate(TranslationKey.SPAWN_TEAMBASE_EXISTS,
                        new Placeholder("bg_arena", arena.getName()),
                        new Placeholder("bg_team", teamId)
                ));
                return;
            }
        }

        // Perhaps create a factory around the creation of spawns abstractions
        Spawn spawn = new ArenaSpawn(componentId, player.getLocation(), teamId);
        spawn.setTeamBase(teamBase);

        arena.getSpawnContainer().add(spawn);

        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".base", teamBase);
        game.getDataFile().setLocation("arena." + arena.getName() + ".component." + componentId + ".location", spawn.getLocation(), false);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".team", teamId);
        game.getDataFile().set("arena." + arena.getName() + ".component." + componentId + ".type", "spawn");
        game.getDataFile().save();

        player.sendMessage(translator.translate(TranslationKey.SPAWN_ADD,
                new Placeholder("bg_arena", arena.getName()),
                new Placeholder("bg_component_id", componentId)
        ));
    }
}
