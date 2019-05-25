package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.game.mode.GameModeType;

public class GameModeUsageValidator implements CommandValidator {

    private Battlegrounds plugin;
    private GameModeType gameModeType;
    private Translator translator;

    public GameModeUsageValidator(Battlegrounds plugin, Translator translator, GameModeType gameModeType) {
        this.plugin = plugin;
        this.translator = translator;
        this.gameModeType = gameModeType;
    }

    public ValidationResponse validate(String[] args) {
        int id = Integer.parseInt(args[1]);

        Game game = plugin.getGameManager().getGame(id);

        for (GameMode gameMode : game.getConfiguration().getGameModes()) {
            if (gameMode.getType() == gameModeType) {
                return ValidationResponse.PASSED;
            }
        }

        return new ValidationResponse(translator.translate(TranslationKey.GAMEMODE_NOT_USED,
                new Placeholder("bg_game", game.getId()),
                new Placeholder("bg_gamemode", gameModeType.toString())
        ));
    }
}
