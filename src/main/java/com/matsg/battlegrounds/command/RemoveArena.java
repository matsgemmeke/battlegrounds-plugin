package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RemoveArena extends SubCommand {

    private List<CommandSender> senders;

    public RemoveArena(Battlegrounds plugin) {
        super(plugin,"removearena", EnumMessage.DESCRIPTION_REMOVEARENA.getMessage(),
                "bg removearena [id] [arena]", "battlegrounds.removearena", false, "ra");
        this.senders = new ArrayList<>();
    }

    public void execute(final CommandSender sender, String[] args) {
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

        if (!senders.contains(sender)) {
            EnumMessage.ARENA_CONFIRM_REMOVE.send(sender, new Placeholder("bg_arena", args[2]));

            senders.add(sender);

            new BattleRunnable() {
                public void run() {
                    senders.remove(sender);
                }
            }.runTaskLater(200);
            return;
        }

        game.getArenaList().remove(arena);
        game.getDataFile().set("arena." + args[2], null);
        game.getDataFile().save();

        sender.sendMessage(EnumMessage.ARENA_REMOVE.getMessage(new Placeholder("bg_arena", args[2]), new Placeholder("bg_game", id)));
    }
}