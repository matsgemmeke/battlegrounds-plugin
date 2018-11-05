package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleArena;
import com.matsg.battlegrounds.util.Message;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArena extends SubCommand {

    public CreateArena(Battlegrounds plugin) {
        super(plugin, "createarena", Message.create(TranslationKey.DESCRIPTION_CREATEARENA),
                "bg createarena [id] [arena]", "battlegrounds.createarena", true, "ca");
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

        if (plugin.getGameManager().getArena(game, name) != null) {
            player.sendMessage(Message.create(TranslationKey.ARENA_EXISTS,
                    new Placeholder("bg_game", id),
                    new Placeholder("bg_arena", name)));
            return;
        }

        LocalSession session = BattlegroundsPlugin.getWorldEditPlugin().getSession(player);
        Region selection;

        try {
            selection = session.getSelection(session.getSelectionWorld());
        } catch (Exception e) {
            return;
        }

        if (selection == null) {
            player.sendMessage(Message.create(TranslationKey.NO_SELECTION));
            return;
        }

        Arena arena = new BattleArena(
                name,
                new Location(player.getWorld(), selection.getMaximumPoint().getX(), selection.getMaximumPoint().getY(), selection.getMaximumPoint().getZ()),
                new Location(player.getWorld(), selection.getMinimumPoint().getX(), selection.getMinimumPoint().getY(), selection.getMinimumPoint().getZ()),
                player.getWorld());

        game.getArenaList().add(arena);
        game.getDataFile().setLocation("arena." + name + ".max", arena.getMax(), false);
        game.getDataFile().setLocation("arena." + name + ".min", arena.getMin(), false);
        game.getDataFile().save();

        if (game.getArena() == null) {
            arena.setActive(true);
        }

        player.sendMessage(Message.create(TranslationKey.ARENA_CREATE,
                new Placeholder("bg_game", id),
                new Placeholder("bg_arena", name)));
    }
}