package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.storage.AbstractYaml;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.Location;
import org.bukkit.Server;

import java.io.IOException;
import java.io.InputStream;

public class BattleCacheYaml extends AbstractYaml implements CacheYaml {

    protected Server server;

    public BattleCacheYaml(String fileName, String filePath, InputStream resource, Server server) throws IOException {
        super(fileName, filePath, resource, false);
        this.server = server;
    }

    public Location getLocation(String path) {
        String[] locationString = getString(path).split(",");

        return new Location(
                server.getWorld(locationString[0]),
                Double.parseDouble(locationString[1]),
                Double.parseDouble(locationString[2]),
                Double.parseDouble(locationString[3]),
                Float.parseFloat(locationString[4]),
                Float.parseFloat(locationString[5])
        );
    }

    public void setLocation(String path, Location location, boolean block) {
        if (block) {
            set(path, location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," +
                    location.getBlockZ() + "," + location.getYaw() + "," + 0.0);
        } else {
            set(path, location.getWorld().getName() + "," + (location.getBlockX() + 0.5) + "," + location.getBlockY() + "," +
                    (location.getBlockZ() + 0.5) + "," + location.getYaw() + "," + 0.0);
        }
    }
}
