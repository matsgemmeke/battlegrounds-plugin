package com.matsg.battlegrounds.api.item;

import org.bukkit.Location;

public interface Launcher extends Firearm {

    Lethal getLethal();

    void explode(Location location);
}
