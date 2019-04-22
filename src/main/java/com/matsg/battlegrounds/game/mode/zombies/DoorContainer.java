package com.matsg.battlegrounds.game.mode.zombies;

import com.matsg.battlegrounds.api.game.ComponentContainer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DoorContainer implements ComponentContainer<Door> {

    private Set<Door> doors;

    public DoorContainer() {
        this.doors = new HashSet<>();
    }

    public void add(Door door) {
        doors.add(door);
    }

    public Door get(int id) {
        for (Door door : doors) {
            if (door.getId() == id) {
                return door;
            }
        }
        return null;
    }

    public Iterable<Door> getAll() {
        return Collections.unmodifiableSet(doors);
    }

    public void remove(int id) {
        Door door = get(id);
        if (door == null) {
            return;
        }
        doors.remove(door);
    }
}
