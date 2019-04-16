package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.game.ComponentContainer;
import com.matsg.battlegrounds.api.game.MobSpawn;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MobSpawnContainer implements ComponentContainer<MobSpawn> {

    private Set<MobSpawn> mobSpawns;

    public MobSpawnContainer() {
        this.mobSpawns = new HashSet<>();
    }

    public void add(MobSpawn mobSpawn) {
        mobSpawns.add(mobSpawn);
    }

    public MobSpawn get(int id) {
        for (MobSpawn mobSpawn : mobSpawns) {
            if (mobSpawn.getId() == id) {
                return mobSpawn;
            }
        }
        return null;
    }

    public Iterable<MobSpawn> getAll() {
        return Collections.unmodifiableSet(mobSpawns);
    }

    public void remove(int id) {
        MobSpawn mobSpawn = get(id);
        if (mobSpawn == null) {
            return;
        }
        mobSpawns.remove(mobSpawn);
    }
}
