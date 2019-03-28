package com.matsg.battlegrounds.api.game;

import org.bukkit.Location;
import org.bukkit.World;

public interface Extent {

    /**
     * Returns true based on whether the extent contains the location.
     *
     * @param location The position.
     * @return True if contained.
     */
    boolean contains(Location location);

    /**
     * Gets the number of blocks in the extent.
     *
     * @return The number of blocks.
     */
    int getArea();

    /**
     * Gets the center location of the extent.
     *
     * @return The center of the extent.
     */
    Location getCenter();

    /**
     * Gets the Y-size.
     *
     * @return The height of the extent.
     */
    int getHeight();

    /**
     * Gets the Z-size.
     *
     * @return The length of the extent.
     */
    int getLength();

    /**
     * Gets the maximum point in the extent. If the extent is unbounded, then a large (positive) value may be returned.
     *
     * @return The maximum point.
     */
    Location getMaximumPoint();

    /**
     * Gets the minimum point in the extent. If the extent is unbounded, then a large (negative) value may be returned.
     *
     * @return The minimum point.
     */
    Location getMinimumPoint();

    /**
     * Gets the X-size.
     *
     * @return The width of the extent.
     */
    int getWidth();

    /**
     * Gets the world of the extent.
     *
     * @return The world of the extent or null if it is unbounded.
     */
    World getWorld();
}
