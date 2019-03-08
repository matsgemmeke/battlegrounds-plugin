package com.matsg.battlegrounds.api.game;

import org.bukkit.Location;
import org.bukkit.World;

public interface Region {

    boolean contains(Location location);

    Location getMax();

    Location getMin();

    int getSize();

    World getWorld();
}
