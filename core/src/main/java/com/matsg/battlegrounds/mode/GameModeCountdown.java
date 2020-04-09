package com.matsg.battlegrounds.mode;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Countdown;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumTitle;

public class GameModeCountdown extends Countdown {

    private Game game;
    private int countdown;
    private Translator translator;

    public GameModeCountdown(Game game, Translator translator, int countdown) {
        this.game = game;
        this.translator = translator;
        this.countdown = countdown;
    }

    public void run() {
        if (game.getArena() == null || game.getPlayerManager().getPlayers().size() < game.getConfiguration().getMinPlayers()) {
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                if (game.getLobby() != null) {
                    gamePlayer.getPlayer().teleport(game.getLobby());
                }
            }

            game.getPlayerManager().broadcastMessage(translator.translate(TranslationKey.COUNTDOWN_CANCELLED.getPath()));
            game.stop();
            cancel();
            return;
        }

        if (countdown <= 0) {
            game.startGame();
            cancel();
            return;
        }

        if (countdown <= 10) {
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                EnumTitle.COUNTDOWN.send(gamePlayer.getPlayer(), new Placeholder("bg_countdown", countdown));
            }
            BattleSound.COUNTDOWN_NOTE.play(game);
        }

        countdown--;
    }
}
