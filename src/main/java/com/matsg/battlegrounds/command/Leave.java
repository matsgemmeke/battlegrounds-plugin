package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Leave extends Command {

    public Leave(Battlegrounds plugin) {
        super(plugin);
        setAliases("l");
        setDescription(createMessage(TranslationKey.DESCRIPTION_LEAVE));
        setName("leave");
        setPlayerOnly(true);
        setUsage("bg leave");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Game game = plugin.getGameManager().getGame(player);

        if (game == null) {
            player.sendMessage(createMessage(TranslationKey.NOT_PLAYING));
            return;
        }

        game.getPlayerManager().removePlayer(game.getPlayerManager().getGamePlayer(player));
    }
}
