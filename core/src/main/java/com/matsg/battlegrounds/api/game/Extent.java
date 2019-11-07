package com.matsg.battlegrounds.api.game;

import org.bukkit.Location;
import org.bukkit.World;

public interface Extent {

    /**
     * Returns true based on whether the extent contains the location.
     *
     * @param location the position
     * @return true if contained
     */
    boolean contains(Location location);

    /**
     * Gets the area coverage of the extent.
     *
     * @return the extent's area coverage
     */
    double getArea();

    /**
     * Gets the center location of the extent.
     *
     * @return the center of the extent
     */
    Location getCenter();

    /**
     * Gets the Y-size.
     *
     * @return the height of the extent
     */
    double getHeight();

    /**
     * Gets the Z-size.
     *
     * @return the length of the extent
     */
    double getLength();

    /**
     * Gets the maximum point in the extent. If the extent is unbounded, then a large (positive) value may be returned.
     *
     * @return the maximum point
     */
    Location getMaximumPoint();

    /**
     * Gets the minimum point in the extent. If the extent is unbounded, then a large (negative) value may be returned.
     *
     * @return the minimum point
     */
    Location getMinimumPoint();

    /**
     * Gets the X-size.
     *
     * @return the width of the extent
     */
    double getWidth();

    /**
     * Gets the world of the extent.
     *
     * @return the world of the extent or null if it is unbounded
     */
    World getWorld();
}
