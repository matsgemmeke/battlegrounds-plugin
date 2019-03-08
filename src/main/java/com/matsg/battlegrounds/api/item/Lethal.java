package com.matsg.battlegrounds.api.item;

import org.bukkit.Location;

public interface Lethal extends Equipment {

    Lethal clone();

    void explode(Location location);
}
