package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.Title;

public class GameCountdown extends BattleRunnable {

    private Game game;
    private int countdown;

    public GameCountdown(Game game, int countdown) {
        this.game = game;
        this.countdown = countdown;

        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            if (gamePlayer.getLoadoutClass() == null) {
                //gamePlayer.getPlayer().openInventory(null);
            }
        }
    }

    public void run() {
        if (countdown <= 10) {
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                Title.COUNTDOWN.send(gamePlayer.getPlayer(), new Placeholder("bg_countdown", countdown));
            }
            BattleSound.COUNTDOWN_NOTE.play(game);
        }
        countdown --;
    }
}