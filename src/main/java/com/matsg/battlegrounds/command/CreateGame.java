package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import com.matsg.battlegrounds.game.BattleGame;
import com.matsg.battlegrounds.game.BattleGameConfiguration;
import org.bukkit.command.CommandSender;

public class CreateGame extends Command {

    private GameManager gameManager;

    public CreateGame(Translator translator, GameManager gameManager) {
        super(translator);
        this.gameManager = gameManager;

        setAliases("cg");
        setDescription(createMessage(TranslationKey.DESCRIPTION_CREATEGAME));
        setName("creategame");
        setPermissionNode("battlegrounds.creategame");
        setUsage("bg creategame [id]");

        registerValidator(new GameIdValidator(gameManager, translator, false));
    }

    public void execute(CommandSender sender, String[] args) {
        int id = Integer.parseInt(args[1]);

        Game game = new BattleGame(plugin, id);
        GameConfiguration configuration = BattleGameConfiguration.getDefaultConfiguration(game);

        configuration.saveConfiguration(game.getDataFile());
        game.getDataFile().save();
        game.setConfiguration(configuration);

        GameMode[] gameModes = configuration.getGameModes();

        if (gameModes.length > 0) {
            game.setGameMode(gameModes[0]);
            gameModes[0].onCreate();
        }

        gameManager.getGames().add(game);

        sender.sendMessage(createMessage(TranslationKey.GAME_CREATE, new Placeholder("bg_game", id)));
    }
}
