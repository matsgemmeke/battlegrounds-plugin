package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.TimeControl;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class BattleTimeControl extends BukkitRunnable implements TimeControl {

    private static final int TIME_CONTROL_DELAY = 0;
    private static final int TIME_CONTROL_PERIOD = 20;

    private Game game;
    private int time;
    private Map<Integer, Runnable> tasks;
    private TaskRunner taskRunner;

    public BattleTimeControl(Game game, TaskRunner taskRunner) {
        this.game = game;
        this.taskRunner = taskRunner;
        this.tasks = new HashMap<>();
        this.time = 0;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String formatTime() {
        return time / 60 + ":" + String.format("%02d", time % 60);
    }

    public void run() {
        game.getGameMode().tick();
        time++;
    }

    public void scheduleRepeatingTask(Runnable runnable, int seconds) {
        tasks.put(seconds, runnable);
    }

    public void start() {
        taskRunner.runTaskTimer(this, TIME_CONTROL_DELAY, TIME_CONTROL_PERIOD);
    }

    public void stop() {
        cancel();
    }
}
