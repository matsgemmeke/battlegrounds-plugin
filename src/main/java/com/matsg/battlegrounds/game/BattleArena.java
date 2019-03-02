package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.player.GamePlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleArena implements Arena {

    private boolean active;
    private List<Block> boundingBlocks;
    private List<Spawn> spawns;
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

    public List<Spawn> getSpawns() {
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
        return hasBorders()
                && location != null
                && location.getX() >= min.getX() && location.getX() <= max.getX()
                && location.getY() >= min.getY() && location.getY() <= max.getY()
                && location.getZ() >= min.getZ() && location.getZ() <= max.getZ();
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

    public Spawn getRandomSpawn() {
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn == null || spawn.isOccupied());

        return spawn;
    }

    public Spawn getRandomSpawn(double distance) {
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn == null || spawn.isOccupied());

        return spawn;
    }

    public Spawn getRandomSpawn(int teamId) {
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn == null || spawn.isOccupied() || spawn.getTeamId() != teamId);

        return spawn;
    }

    public Spawn getRandomSpawn(int teamId, double distance) {
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn == null || spawn.isOccupied() || spawn.getTeamId() != teamId);

        return spawn;
    }

    public Spawn getSpawn(GamePlayer gamePlayer) {
        for (Spawn spawn : spawns) {
            if (spawn.getGamePlayer() == gamePlayer) {
                return spawn;
            }
        }
        return null;
    }

    public Spawn getSpawn(int index) {
        for (Spawn spawn : spawns) {
            if (spawn.getIndex() == index) {
                return spawn;
            }
        }
        return null;
    }

    public Spawn getTeamBase(int teamId) {
        for (Spawn spawn : spawns) {
            if (spawn.getTeamId() == teamId && spawn.isTeamBase()) {
                return spawn;
            }
        }
        return null;
    }

    public boolean hasBorders() {
        return max != null && min != null;
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