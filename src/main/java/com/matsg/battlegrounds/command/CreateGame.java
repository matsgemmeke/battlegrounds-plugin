package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.game.BattleGame;
import com.matsg.battlegrounds.game.BattleGameConfiguration;
import com.matsg.battlegrounds.util.Message;
import org.bukkit.command.CommandSender;

public class CreateGame extends SubCommand {

    public CreateGame(Battlegrounds plugin) {
        super(plugin, "creategame", Message.create(TranslationKey.DESCRIPTION_CREATEGAME),
                "bg creategame [id]", "battlegrounds.creategame",false, "cg");
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(Message.create(TranslationKey.SPECIFY_ID));
            return;
        }

        int id;

        try {
            id = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(Message.create(TranslationKey.INVALID_ARGUMENT_TYPE, new Placeholder("bg_arg", args[1])));
            return;
        }

        if (plugin.getGameManager().exists(id)) {
            sender.sendMessage(Message.create(TranslationKey.GAME_EXISTS, new Placeholder("bg_game", id)));
            return;
        }

        Game game = new BattleGame(plugin, id);
        GameConfiguration configuration = BattleGameConfiguration.getDefaultConfiguration(game);

        configuration.saveConfiguration(game.getDataFile());
        game.getDataFile().save();
        game.setConfiguration(configuration);

        plugin.getGameManager().getGames().add(game);

        sender.sendMessage(Message.create(TranslationKey.GAME_CREATE, new Placeholder("bg_game", id)));
    }
}