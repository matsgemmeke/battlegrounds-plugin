package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobby extends SubCommand {

    public SetLobby(Battlegrounds plugin) {
        super(plugin, "setlobby", EnumMessage.DESCRIPTION_SETLOBBY.getMessage(),
                "bg setlobby [id]", "battlegrounds.setlobby", true, "sl");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

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
        game.getDataFile().setLocation("lobby", player.getLocation(), true);
        game.getDataFile().save();

        player.sendMessage(EnumMessage.LOBBY_SET.getMessage(new Placeholder("bg_game", id)));
    }
}