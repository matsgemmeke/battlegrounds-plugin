package com.matsg.battlegrounds.mode.shared;

import com.matsg.battlegrounds.api.game.Countdown;
import org.bukkit.scheduler.BukkitRunnable;

public class NulledCountdown extends BukkitRunnable implements Countdown {

    public void cancelCountdown() {
        cancel();
    }

    public void run() {
        cancel();
    }
}
