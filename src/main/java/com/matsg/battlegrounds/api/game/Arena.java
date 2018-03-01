package com.matsg.battlegrounds.api.game;

import java.util.Collection;

public interface Arena extends Region {

    String getName();

    Collection<Spawn> getSpawns();

    boolean isActive();

    void setActive(boolean active);
}