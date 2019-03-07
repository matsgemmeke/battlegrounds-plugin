package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validate.GameIdValidator;
import com.matsg.battlegrounds.game.BattleArena;
import com.matsg.battlegrounds.nms.ReflectionUtils;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArena extends SubCommand {

    public CreateArena(Battlegrounds plugin) {
        super(plugin);
        setAliases("ca");
        setDescription(createMessage(TranslationKey.DESCRIPTION_CREATEARENA));
        setName("createarena");
        setPermissionNode("battlegrounds.createarena");
        setPlayerOnly(true);
        setUsage("bg createarena [id] [arena]");

        registerValidator(new GameIdValidator(plugin));
    }

    public void executeSubCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        int id = Integer.parseInt(args[1]);

        Game game = plugin.getGameManager().getGame(id);

        if (args.length == 2) {
            player.sendMessage(createMessage(TranslationKey.SPECIFY_NAME));
            return;
        }

        String arenaName = args[2].replaceAll("_", " ");

        if (plugin.getGameManager().getArena(game, arenaName) != null) {
            player.sendMessage(createMessage(TranslationKey.ARENA_EXISTS,
                    new Placeholder("bg_game", id),
                    new Placeholder("bg_arena", arenaName)
            ));
            return;
        }

        Location max = null, min = null;
        WorldEditPlugin worldEdit = BattlegroundsPlugin.getWorldEditPlugin();

        // For the moment, only set min and max ranges for server clients running version 1.12 or lower.
        // TODO: Add a functionality that accepts both old and new versions of WorldEdit.
        if (worldEdit != null && ReflectionUtils.getEnumVersion().getValue() < 13) {
            Selection selection = worldEdit.getSelection(player);
            max = selection.getMaximumPoint();
            min = selection.getMinimumPoint();
        }

        World world = player.getWorld();
        Arena arena = new BattleArena(arenaName, max, min, world);

        game.getArenaList().add(arena);
        game.getDataFile().set("arena." + arenaName + ".world", world.getName());

        if (max != null && min != null) {
            game.getDataFile().setLocation("arena." + arenaName + ".max", arena.getMax(), false);
            game.getDataFile().setLocation("arena." + arenaName + ".min", arena.getMin(), false);
        }

        game.getDataFile().save();

        arena.setActive(game.getArena() == null);

        player.sendMessage(createMessage(TranslationKey.ARENA_CREATE,
                new Placeholder("bg_game", id),
                new Placeholder("bg_arena", arenaName)
        ));
    }
}
