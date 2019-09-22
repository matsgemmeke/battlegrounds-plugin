package com.matsg.battlegrounds;

import org.bukkit.scheduler.BukkitTask;

public interface TaskRunner {

    BukkitTask runTaskLater(Runnable runnable, long delay);

    BukkitTask runTaskTimer(Runnable runnable, long delay, long period);
}
