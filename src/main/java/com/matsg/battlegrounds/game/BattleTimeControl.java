package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.TimeControl;
import com.matsg.battlegrounds.util.BattleRunnable;

public class BattleTimeControl extends BattleRunnable implements TimeControl {

    private Game game;
    private int time;

    public BattleTimeControl(Game game) {
        this.game = game;
        this.time = 0;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getFormatTime() {
        return time / 60 + ":" + time % 60;
    }

    public void run() {
        game.getGameMode().getScoreboard().display(game);
        time ++;
    }

    public void stop() {
        cancel();
    }
}