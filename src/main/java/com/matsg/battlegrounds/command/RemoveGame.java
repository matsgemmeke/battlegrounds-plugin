package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RemoveGame extends SubCommand {

    private List<CommandSender> senders;

    public RemoveGame(Battlegrounds plugin) {
        super(plugin);
        this.senders = new ArrayList<>();

        setAliases("rg");
        setDescription(createMessage(TranslationKey.DESCRIPTION_REMOVEGAME));
        setName("removegame");
        setPermissionNode("battlegrounds.removegame");
        setUsage("bg removegame [id]");
    }

    public void executeSubCommand(final CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(createMessage(TranslationKey.SPECIFY_ID));
            return;
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(createMessage(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        if (!plugin.getGameManager().exists(id)) {
            sender.sendMessage(createMessage(TranslationKey.GAME_NOT_EXISTS, new Placeholder("bg_game", id)));
            return;
        }

        if (!senders.contains(sender)) {
            sender.sendMessage(createMessage(TranslationKey.GAME_CONFIRM_REMOVE, new Placeholder("bg_game", id)));

            senders.add(sender);

            new BattleRunnable() {
                public void run() {
                    senders.remove(sender);
                }
            }.runTaskLater(200);
            return;
        }

        Game game = plugin.getGameManager().getGame(id);
        game.getDataFile().removeFile();

        plugin.getGameManager().getGames().remove(game);

        sender.sendMessage(createMessage(TranslationKey.GAME_REMOVE, new Placeholder("bg_game", id)));

        senders.remove(sender);
    }
}
