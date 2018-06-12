package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.TimeControl;
import com.matsg.battlegrounds.util.BattleRunnable;

import java.util.HashMap;
import java.util.Map;

public class BattleTimeControl extends BattleRunnable implements TimeControl {

    private Game game;
    private int time;
    private Map<Integer, Runnable> tasks;

    public BattleTimeControl(Game game) {
        this.game = game;
        this.tasks = new HashMap<>();
        this.time = 0;

        runTaskTimer(0, 20);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String formatTime() {
        return String.format("%02d", time / 60) + ":" + String.format("%02d", time % 60);
    }

    public void run() {
        game.getGameMode().getScoreboard().display(game);
        time ++;
    }

    public void scheduleRepeatingTask(Runnable runnable, int seconds) {
        tasks.put(seconds, runnable);
    }

    public void stop() {
        cancel();
    }
}