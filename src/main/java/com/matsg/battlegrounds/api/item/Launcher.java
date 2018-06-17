package com.matsg.battlegrounds.api.item;

import org.bukkit.Location;

public interface Launcher extends FireArm {

    double getLaunchSpeed();

    Lethal getLethal();

    void explode(Location location);
}