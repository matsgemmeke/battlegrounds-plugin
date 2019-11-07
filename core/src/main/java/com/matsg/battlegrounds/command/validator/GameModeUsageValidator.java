package com.matsg.battlegrounds.command.validator;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.GameManager;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameMode;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.mode.GameModeType;

public class GameModeUsageValidator implements CommandValidator {

    private GameManager gameManager;
    private GameModeType gameModeType;
    private Translator translator;

    public GameModeUsageValidator(GameManager gameManager, Translator translator, GameModeType gameModeType) {
        this.gameManager = gameManager;
        this.translator = translator;
        this.gameModeType = gameModeType;
    }

    public ValidationResponse validate(String[] args) {
        int id = Integer.parseInt(args[1]);

        Game game = gameManager.getGame(id);

        for (GameMode gameMode : game.getGameModeList()) {
            if (gameMode.getId().equals(gameModeType.toString())) {
                return ValidationResponse.PASSED;
            }
        }

        String gameModeName = translator.translate(gameModeType.getNamePath());

        return new ValidationResponse(translator.translate(TranslationKey.GAMEMODE_NOT_USED.getPath(),
                new Placeholder("bg_game", game.getId()),
                new Placeholder("bg_gamemode", gameModeName)
        ));
    }
}
