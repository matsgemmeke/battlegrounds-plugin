package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Arena;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.*;

public class BattleArena implements Arena {

    private boolean active;
    private List<Block> boundingBlocks;
    private List<Location> spawns;
    private Location max, min;
    private String name;
    private World world;

    public BattleArena(String name, Location max, Location min, World world) {
        this.active = false;
        this.max = max;
        this.min = min;
        this.name = name;
        this.spawns = new ArrayList<>();
        this.world = world;
    }

    public Location getMax() {
        return max;
    }

    public Location getMin() {
        return min;
    }

    public String getName() {
        return name;
    }

    public List<Location> getSpawns() {
        return spawns;
    }

    public World getWorld() {
        return world;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean contains(Location location) {
        return location != null && getBoundingBlocks().contains(location.getBlock());
    }

    private List<Block> getBoundingBlocks() {
        if (boundingBlocks == null || boundingBlocks.size() <= 0 || boundingBlocks.isEmpty()) {
            boundingBlocks = updateBoundingBlocks();
        }
        return boundingBlocks;
    }

    public int getSize() {
        return getBoundingBlocks().size();
    }

    private List<Block> updateBoundingBlocks() {
        List<Block> list = new ArrayList<>();
        for (int x = (int) min.getX(); x <= max.getX(); x += 1.0) {
            for (int y = (int) min.getY(); y <= max.getY(); y += 1.0) {
                for (int z = (int) min.getZ(); z <= max.getZ(); z += 1.0) {
                    list.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return list;
    }
}