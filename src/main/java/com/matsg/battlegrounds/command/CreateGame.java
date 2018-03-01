package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleGame;
import com.matsg.battlegrounds.game.BattleGameConfiguration;
import com.matsg.battlegrounds.util.EnumMessage;
import org.bukkit.command.CommandSender;

public class CreateGame extends SubCommand {

    public CreateGame(Battlegrounds plugin) {
        super(plugin, "creategame", EnumMessage.DESCRIPTION_CREATEGAME.getMessage(),
                "bg creategame [id]", "battlegrounds.creategame",false, "cg");
    }

    public void execute(CommandSender sender, String[] args) {
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

        if (plugin.getGameManager().exists(id)) {
            EnumMessage.GAME_EXISTS.send(sender, new Placeholder("bg_game", id));
            return;
        }

        Game game = new BattleGame(plugin, id);
        GameConfiguration configuration = BattleGameConfiguration.DEFAULT;

        configuration.saveConfiguration(game.getDataFile());
        game.getDataFile().save();
        game.setConfiguration(configuration);

        plugin.getGameManager().getGames().add(game);

        sender.sendMessage(EnumMessage.GAME_CREATE.getMessage(new Placeholder("bg_game", id)));
    }
}