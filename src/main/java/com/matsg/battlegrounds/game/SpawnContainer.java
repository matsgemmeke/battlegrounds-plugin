package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.ComponentContainer;
import com.matsg.battlegrounds.api.game.Spawn;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SpawnContainer implements ComponentContainer<Spawn> {

    private Set<Spawn> spawns;

    public SpawnContainer() {
        this.spawns = new HashSet<>();
    }

    public void add(Spawn spawn) {
        spawns.add(spawn);
    }

    public Spawn get(int id) {
        for (Spawn spawn : spawns) {
            if (spawn.getId() == id) {
                return spawn;
            }
        }
        return null;
    }

    public Iterable<Spawn> getAll() {
        return Collections.unmodifiableSet(spawns);
    }

    public void remove(int id) {
        Spawn spawn = get(id);
        if (spawn == null) {
            return;
        }
        spawns.remove(spawn);
    }
}
