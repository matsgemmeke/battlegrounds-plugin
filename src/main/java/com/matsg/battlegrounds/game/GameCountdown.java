package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.GameState;
import com.matsg.battlegrounds.api.item.Weapon;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.gui.SelectClassView;
import com.matsg.battlegrounds.util.*;

public class GameCountdown extends BattleRunnable {

    private Game game;
    private int countdown;

    public GameCountdown(Game game, int countdown) {
        this.game = game;
        this.countdown = countdown;

        for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
            if (gamePlayer.getLoadoutClass() == null) {
                gamePlayer.getPlayer().openInventory(new SelectClassView(plugin, game, gamePlayer).getInventory());
            }
        }
    }

    public void run() {
        if (countdown <= 0) {
            game.setState(GameState.IN_GAME);
            game.updateSign();
            game.getGameMode().onStart();

            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                if (gamePlayer.getLoadoutClass() != null) {
                    for (Weapon weapon : gamePlayer.getLoadoutClass().getWeapons()) {
                        weapon.update();
                    }
                }
            }

            cancel();
            return;
        }
        if (countdown <= 10) {
            for (GamePlayer gamePlayer : game.getPlayerManager().getPlayers()) {
                Title.COUNTDOWN.send(gamePlayer.getPlayer(), new Placeholder("bg_countdown", countdown));
            }
            BattleSound.COUNTDOWN_NOTE.play(game);
        }
        countdown --;
    }
}