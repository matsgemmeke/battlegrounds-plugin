package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join extends SubCommand {

    public Join(Battlegrounds plugin) {
        super(plugin, "join", EnumMessage.DESCRIPTION_JOIN.getMessage(), "bg join [id]", null, true, "j");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (plugin.getGameManager().getGame(player) != null) {
            EnumMessage.ALREADY_PLAYING.send(player);
            return;
        }

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

        if (!plugin.getBattlegroundsConfig().joinableGamestates.contains(game.getState().toString())) {
            EnumMessage.IN_PROGRESS.send(player);
            return;
        }

        if (game.getPlayerManager().getPlayers().size() >= game.getConfiguration().getMaxPlayers()) {
            EnumMessage.SPOTS_FULL.send(player);
            return;
        }

        game.getPlayerManager().addPlayer(player);
    }
}