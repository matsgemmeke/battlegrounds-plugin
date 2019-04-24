package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Selection;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import com.matsg.battlegrounds.game.BattleArena;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArena extends Command {

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

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        int id = Integer.parseInt(args[1]);

        Game game = plugin.getGameManager().getGame(id);

        if (args.length == 2) {
            player.sendMessage(createMessage(TranslationKey.SPECIFY_ARENA_NAME));
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

        Selection selection = plugin.getSelectionManager().getSelection(player);
        Location max = selection.getMaximumPoint();
        Location min = selection.getMinimumPoint();

        World world = player.getWorld();
        Arena arena = new BattleArena(arenaName, world, max, min);

        game.getArenaList().add(arena);
        game.getDataFile().set("arena." + arenaName + ".world", world.getName());

        if (max != null && min != null) {
            game.getDataFile().setLocation("arena." + arenaName + ".max", max, true);
            game.getDataFile().setLocation("arena." + arenaName + ".min", min, true);
        }

        game.getDataFile().save();

        arena.setActive(game.getArena() == null);

        player.sendMessage(createMessage(TranslationKey.ARENA_CREATE,
                new Placeholder("bg_game", id),
                new Placeholder("bg_arena", arenaName)
        ));
    }
}
