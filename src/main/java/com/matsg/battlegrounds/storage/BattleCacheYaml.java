package com.matsg.battlegrounds.storage;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.storage.AbstractYaml;
import com.matsg.battlegrounds.api.storage.CacheYaml;
import org.bukkit.Location;

import java.io.IOException;

public class BattleCacheYaml extends AbstractYaml implements CacheYaml {

    public BattleCacheYaml(Battlegrounds plugin, String resource) throws IOException {
        super(plugin, plugin.getDataFolder().getPath(), resource, false);
    }

    public BattleCacheYaml(Battlegrounds plugin, String path, String resource) throws IOException {
        super(plugin, path, resource, false);
    }

    public Location getLocation(String path) {
        if (getString(path) == null) {
            return null;
        }

        String[] locationString = getString(path).split(",");

        if (locationString.length <= 0) {
            return null;
        }

        try {
            return new Location(plugin.getServer().getWorld(locationString[0]), Double.parseDouble(locationString[1]),
                    Double.parseDouble(locationString[2]), Double.parseDouble(locationString[3]),
                    Float.parseFloat(locationString[4]), Float.parseFloat(locationString[5]));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
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
