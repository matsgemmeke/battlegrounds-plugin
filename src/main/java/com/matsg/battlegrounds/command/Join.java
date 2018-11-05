package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join extends SubCommand {

    public Join(Battlegrounds plugin) {
        super(plugin, "join", Message.create(TranslationKey.DESCRIPTION_JOIN), "bg join [id]", null, true, "j");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (plugin.getGameManager().getGame(player) != null) {
            player.sendMessage(Message.create(TranslationKey.ALREADY_PLAYING));
            return;
        }

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

        if (!plugin.getBattlegroundsConfig().joinableGamestates.contains(game.getState().toString())) {
            player.sendMessage(Message.create(TranslationKey.IN_PROGRESS));
            return;
        }

        if (game.getPlayerManager().getPlayers().size() >= game.getConfiguration().getMaxPlayers()) {
            player.sendMessage(Message.create(TranslationKey.SPOTS_FULL));
            return;
        }

        game.getPlayerManager().addPlayer(player);
    }
}