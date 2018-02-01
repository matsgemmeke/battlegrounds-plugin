package com.matsg.battlegrounds.api.config;

import org.bukkit.Location;

public interface CacheYaml extends Yaml {

    Location getLocation(String path);

    void setLocation(String path, Location location, boolean block);
}