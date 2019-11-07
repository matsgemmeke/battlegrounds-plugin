package com.matsg.battlegrounds.api;

import org.bukkit.Location;

public interface Selection extends Extent {

    /**
     * Gets the first selected location of the selection.
     *
     * @return the first selected point
     */
    Location getFirstSelectedPoint();

    /**
     * Gets the second selected location of the selection.
     *
     * @return the second selected point
     */
    Location getSecondSelectedPoint();

    /**
     * Gets whether the selection has both first and second points selected.
     *
     * @return whether both points of the selection are set
     */
    boolean isComplete();
}
