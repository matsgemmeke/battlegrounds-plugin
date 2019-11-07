package com.matsg.battlegrounds.api.storage;

import org.bukkit.Location;

public interface CacheYaml extends Yaml {

    /**
     * Parses a location from a certain file path.
     *
     * @param path the file path
     * @return a parsed location from the value at the file path
     */
    Location getLocation(String path);

    /**
     * Sets a location to a certain file path.
     *
     * @param path the file path
     * @param location the location
     * @param block whether the location should be set as the block location or the original location
     */
    void setLocation(String path, Location location, boolean block);
}
