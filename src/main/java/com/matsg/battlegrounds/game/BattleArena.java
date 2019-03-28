package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleArena implements Arena {

    private boolean active;
    private List<Spawn> spawns;
    private Location maximumPoint, minimumPoint;
    private String name;
    private World world;

    public BattleArena(String name, Location maximumPoint, Location minimumPoint, World world) {
        this.active = false;
        this.maximumPoint = maximumPoint;
        this.minimumPoint = minimumPoint;
        this.name = name;
        this.spawns = new ArrayList<>();
        this.world = world;
    }

    public Location getMaximumPoint() {
        return maximumPoint;
    }

    public Location getMinimumPoint() {
        return minimumPoint;
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
                && location.getX() >= minimumPoint.getX() && location.getX() <= maximumPoint.getX()
                && location.getY() >= minimumPoint.getY() && location.getY() <= maximumPoint.getY()
                && location.getZ() >= minimumPoint.getZ() && location.getZ() <= maximumPoint.getZ();
    }

    public int getArea() {
        return getWidth() * getHeight() * getLength();
    }

    public Location getCenter() {
        if (!hasBorders()) {
            return null;
        }
        return maximumPoint.toVector().add(minimumPoint.toVector()).multiply(0.5).toLocation(world);
    }

    public int getHeight() {
        if (!hasBorders()) {
            return -1;
        }
        return maximumPoint.getBlockY() - minimumPoint.getBlockY();
    }

    public int getLength() {
        if (!hasBorders()) {
            return -1;
        }
        return maximumPoint.getBlockZ() - minimumPoint.getBlockZ();
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
            if (spawn.getId() == index) {
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

    public int getWidth() {
        if (!hasBorders()) {
            return -1;
        }
        return maximumPoint.getBlockX() - minimumPoint.getBlockX();
    }

    public boolean hasBorders() {
        return maximumPoint != null && minimumPoint != null;
    }

    private List<Block> updateBoundingBlocks() {
        List<Block> list = new ArrayList<>();
        for (int x = (int) minimumPoint.getX(); x <= maximumPoint.getX(); x += 1.0) {
            for (int y = (int) minimumPoint.getY(); y <= maximumPoint.getY(); y += 1.0) {
                for (int z = (int) minimumPoint.getZ(); z <= maximumPoint.getZ(); z += 1.0) {
                    list.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return list;
    }
}
