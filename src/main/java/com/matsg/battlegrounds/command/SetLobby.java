package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobby extends SubCommand {

    public SetLobby(Battlegrounds plugin) {
        super(plugin, "setlobby", Message.create(TranslationKey.DESCRIPTION_SETLOBBY),
                "bg setlobby [id]", "battlegrounds.setlobby", true, "sl");
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
        game.getDataFile().setLocation("lobby", player.getLocation(), true);
        game.getDataFile().save();

        player.sendMessage(Message.create(TranslationKey.LOBBY_SET, new Placeholder("bg_game", id)));
    }
}