package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Leave extends Command {

    private GameManager gameManager;

    public Leave(Translator translator, GameManager gameManager) {
        super(translator);
        this.gameManager = gameManager;

        setAliases("l");
        setDescription(createMessage(TranslationKey.DESCRIPTION_LEAVE));
        setName("leave");
        setPlayerOnly(true);
        setUsage("bg leave");
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Game game = gameManager.getGame(player);

        if (game == null) {
            player.sendMessage(createMessage(TranslationKey.NOT_PLAYING));
            return;
        }

        game.getPlayerManager().removePlayer(game.getPlayerManager().getGamePlayer(player));
    }
}
