package com.matsg.battlegrounds.api.game;

import org.bukkit.Location;

import java.util.Collection;

public interface Arena extends Region {

    String getName();

    Collection<Location> getSpawns();

    boolean isActive();

    void setActive(boolean active);
}