package com.matsg.battlegrounds.mode.zombies;

import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.mode.zombies.component.MobSpawn;

import java.util.List;

public interface Wave<T extends Mob> {

    /**
     * Gets the amount of mobs that are to be spawned in the wave.
     *
     * @return the mob count
     */
    int getMobCount();

    /**
     * Gets the list of mobs that are to be spawned in the wave. Does not include mobs which are already spawned in.
     *
     * @return the list of mobs that will spawn
     */
    List<T> getMobs();

    /**
     * Gets the list of mob spawns where mob will be spawned during the wave.
     *
     * @return the list of mob spawns
     */
    List<MobSpawn> getMobSpawns();

    /**
     * Gets the round number of the wave.
     *
     * @return the round number
     */
    int getRound();

    /**
     * Gets whether the wave is currently running.
     *
     * @return whether the wave is running
     */
    boolean isRunning();

    /**
     * Sets whether the wave is currently running.
     *
     * @param running whether the wave should run
     */
    void setRunning(boolean running);

    /**
     * Gets the next mob to be spawned in the game.
     *
     * @return the next mob
     */
    T nextMob();
}
