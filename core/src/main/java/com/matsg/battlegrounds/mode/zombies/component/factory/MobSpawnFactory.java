package com.matsg.battlegrounds.mode.zombies.component.factory;

import com.matsg.battlegrounds.mode.zombies.component.MobSpawn;
import com.matsg.battlegrounds.mode.zombies.component.ZombiesMobSpawn;
import org.bukkit.Location;

public class MobSpawnFactory {

    /**
     * Creates a mob spawn component based on the given input.
     *
     * @param id the component id
     * @param location the location of the mob spawn
     * @return a mob spawn implementation
     */
    public MobSpawn make(int id, Location location) {
        return new ZombiesMobSpawn(id, location);
    }
}
