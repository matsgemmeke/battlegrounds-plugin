package com.matsg.battlegrounds.command;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameConfiguration;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.command.validator.GameIdValidator;
import com.matsg.battlegrounds.game.BattleGameConfiguration;
import com.matsg.battlegrounds.game.GameFactory;
import com.matsg.battlegrounds.mode.GameModeFactory;
import com.matsg.battlegrounds.mode.GameModeType;
import org.bukkit.command.CommandSender;

public class CreateGame extends Command {

    private GameFactory gameFactory;
    private GameManager gameManager;
    private GameModeFactory gameModeFactory;

    public CreateGame(Translator translator, GameFactory gameFactory, GameManager gameManager, GameModeFactory gameModeFactory) {
        super(translator);
        this.gameFactory = gameFactory;
        this.gameManager = gameManager;
        this.gameModeFactory = gameModeFactory;

        setAliases("cg");
        setDescription(createMessage(TranslationKey.DESCRIPTION_CREATEGAME));
        setName("creategame");
        setPermissionNode("battlegrounds.creategame");
        setUsage("bg creategame [id]");

        registerValidator(new GameIdValidator(gameManager, translator, false));
    }

    public void execute(CommandSender sender, String[] args) {
        int id = Integer.parseInt(args[1]);

        Game game = gameFactory.make(id);
        GameConfiguration configuration = BattleGameConfiguration.getDefaultConfiguration();

        configuration.saveConfiguration(game.getDataFile());
        game.getDataFile().save();
        game.setConfiguration(configuration);

        GameModeType gameModeType = GameModeType.valueOf(configuration.getGameModeTypes().get(0));
        GameMode gameMode = gameModeFactory.make(game, gameModeType);

        game.setGameMode(gameMode);
        gameMode.onCreate();

        gameManager.getGames().add(game);

        sender.sendMessage(createMessage(TranslationKey.GAME_CREATE, new Placeholder("bg_game", id)));
    }
}
