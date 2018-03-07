package com.matsg.battlegrounds.api.game;

import java.util.Collection;

public interface Arena extends Region {

    String getName();

    Spawn getRandomSpawn();

    Collection<Spawn> getSpawns();

    boolean isActive();

    void setActive(boolean active);
}