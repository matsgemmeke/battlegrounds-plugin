package com.matsg.battlegrounds.api;

import com.matsg.battlegrounds.api.game.Extent;
import org.bukkit.Location;

public interface Selection extends Extent {

    Location getFirstSelectedPoint();

    Location getSecondSelectedPoint();

    boolean isComplete();
}
