package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Translator;
import com.matsg.battlegrounds.api.game.Countdown;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.Placeholder;
import com.matsg.battlegrounds.gui.scoreboard.LobbyScoreboard;
import com.matsg.battlegrounds.util.BattleSound;

import java.util.ArrayList;
import java.util.List;

public class LobbyCountdown extends Countdown {

    private Game game;
    private int countdown;
    private List<Integer> display;
    private LobbyScoreboard scoreboard;
    private Translator translator;

    public LobbyCountdown(Game game, Translator translator, int countdown, int... display) {
        this.game = game;
        this.translator = translator;
        this.countdown = countdown;
        this.display = new ArrayList<>();
        this.scoreboard = new LobbyScoreboard(game);

        for (int i : display) {
            this.display.add(i);
        }
    }

    public void run() {
        if (game.getArena() == null || game.getPlayerManager().getPlayers().size() < game.getConfiguration().getMinPlayers()) {
            game.stop();
            cancel();
            return;
        }

        if (countdown <= 0) {
            game.startGameModeCountdown();
            cancel();
            return;
        }

        if (display.contains(countdown)) {
            game.getPlayerManager().broadcastMessage(translator.translate(TranslationKey.COUNTDOWN_NOTE.getPath(), new Placeholder("bg_countdown", countdown)));
            BattleSound.COUNTDOWN_NOTE.play(game);
        }

        scoreboard.setCountdown(countdown);
        scoreboard.display(game);
        countdown--;
    }
}
