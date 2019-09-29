package com.matsg.battlegrounds;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public interface TaskRunner {

    BukkitTask runTaskLater(BukkitRunnable runnable, long delay);

    BukkitTask runTaskLater(Runnable runnable, long delay);

    BukkitTask runTaskTimer(BukkitRunnable runnable, long delay, long period);

    BukkitTask runTaskTimer(Runnable runnable, long delay, long period);
}
