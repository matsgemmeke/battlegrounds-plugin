package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Leave extends SubCommand {

    public Leave(Battlegrounds plugin) {
        super(plugin, "leave", Message.create(TranslationKey.DESCRIPTION_LEAVE), "bg leave", null, true, "l");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            player.sendMessage(Message.create(TranslationKey.NOT_PLAYING));
            return;
        }

        game.getPlayerManager().removePlayer(game.getPlayerManager().getGamePlayer(player));
    }
}