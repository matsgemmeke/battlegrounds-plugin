package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import com.matsg.battlegrounds.game.BattleGame;
import com.matsg.battlegrounds.game.BattleGameConfiguration;
import org.bukkit.command.CommandSender;

public class CreateGame extends Command {

    public CreateGame(Battlegrounds plugin) {
        super(plugin);

        setAliases("cg");
        setDescription(createMessage(TranslationKey.DESCRIPTION_CREATEGAME));
        setName("creategame");
        setPermissionNode("battlegrounds.creategame");
        setUsage("bg creategame [id]");

        registerValidator(new GameIdValidator(plugin, plugin.getTranslator(), false));
    }

    public void execute(CommandSender sender, String[] args) {
        int id = Integer.parseInt(args[1]);

        Game game = new BattleGame(plugin, id);
        GameConfiguration configuration = BattleGameConfiguration.getDefaultConfiguration(game);

        configuration.saveConfiguration(game.getDataFile());
        game.getDataFile().save();
        game.setConfiguration(configuration);
        game.setGameMode(configuration.getGameModes()[0]);

        plugin.getGameManager().getGames().add(game);

        sender.sendMessage(createMessage(TranslationKey.GAME_CREATE, new Placeholder("bg_game", id)));
    }
}
