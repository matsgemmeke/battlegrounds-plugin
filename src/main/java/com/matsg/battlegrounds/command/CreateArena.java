package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.BattlegroundsPlugin;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleArena;
import com.matsg.battlegrounds.util.EnumMessage;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArena extends SubCommand {

    public CreateArena(Battlegrounds plugin) {
        super(plugin, "createarena", EnumMessage.DESCRIPTION_CREATEARENA.getMessage(),
                "bg createarena [id] [arena]", "battlegrounds.createarena", true, "ca");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 1) {
            EnumMessage.SPECIFY_ID.send(player);
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
            EnumMessage.SPECIFY_NAME.send(player);
            return;
        }

        String name = args[2].replaceAll("_", " ");

        if (plugin.getGameManager().getArena(game, name) != null) {
            EnumMessage.ARENA_EXISTS.send(player, new Placeholder("bg_game", id), new Placeholder("bg_arena", name));
            return;
        }

        Selection selection = BattlegroundsPlugin.getWorldEditPlugin().getSelection(player);

        if (selection == null) {
            EnumMessage.NO_SELECTION.send(player);
            return;
        }

        Arena arena = new BattleArena(name, selection.getMaximumPoint(), selection.getMinimumPoint(), selection.getWorld());

        game.getArenaList().add(arena);
        game.getDataFile().setLocation("arena." + name + ".max", arena.getMax(), false);
        game.getDataFile().setLocation("arena." + name + ".min", arena.getMin(), false);
        game.getDataFile().save();

        if (game.getArena() == null) {
            arena.setActive(true);
        }

        player.sendMessage(EnumMessage.ARENA_CREATE.getMessage(new Placeholder("bg_game", id), new Placeholder("bg_arena", name)));
    }
}