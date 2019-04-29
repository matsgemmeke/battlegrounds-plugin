package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.game.Countdown;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameState;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.SelectLoadoutView;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumTitle;
import com.matsg.battlegrounds.util.MessageHelper;

public class GameCountdown extends BattleRunnable implements Countdown {

    private boolean cancelled;
    private Game game;
    private int countdown;

    public GameCountdown(Game game, int countdown) {
        this.game = game;
        this.countdown = countdown;
        this.cancelled = false;

        game.setState(game.getState().next());
        game.updateSign();
        game.getGameMode().start();

        runTaskTimer(0, 20);
    }

    public void cancelCountdown() {
        cancelled = true;
        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            gamePlayer.getPlayer().teleport(game.getLobby());
        }
        MessageHelper messageHelper = new MessageHelper();
        game.getPlayerManager().broadcastMessage(messageHelper.create(TranslationKey.COUNTDOWN_CANCELLED));
    }

    public void run() {
        if (cancelled) {
            cancel();
            return;
        }
        if (game.getArena() == null || game.getPlayerManager().getPlayers().size() < game.getConfiguration().getMinPlayers()) {
            game.stop();
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
        countdown --;
    }
}
