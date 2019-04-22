package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class BattleArena implements Arena {

    private boolean active;
    private ComponentContainer<Spawn> spawnContainer;
    private Location maximumPoint, minimumPoint;
    private String name;
    private World world;

    public BattleArena(String name, World world, Location maximumPoint, Location minimumPoint) {
        this.name = name;
        this.world = world;
        this.maximumPoint = maximumPoint;
        this.minimumPoint = minimumPoint;
        this.active = false;
        this.spawnContainer = new SpawnContainer();
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

    public ComponentContainer<Spawn> getSpawnContainer() {
        return spawnContainer;
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

    public ArenaComponent getComponent(int id) {
        return spawnContainer.get(id);
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
        List<Spawn> spawns = getSpawns();
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn == null || spawn.isOccupied());

        return spawn;
    }

    public Spawn getRandomSpawn(Location location, double distance) {
        List<Spawn> spawns = getSpawns();
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn == null || spawn.isOccupied() || location.distance(spawn.getLocation()) < distance);

        return spawn;
    }

    public Spawn getRandomSpawn(int teamId) {
        List<Spawn> spawns = getSpawns();
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn == null || spawn.isOccupied() || spawn.getTeamId() != teamId);

        return spawn;
    }

    public Spawn getRandomSpawn(int teamId, Location location, double distance) {
        List<Spawn> spawns = getSpawns();
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn == null || spawn.isOccupied() || spawn.getTeamId() != teamId || location.distance(spawn.getLocation()) < distance);

        return spawn;
    }

    public Spawn getSpawn(GamePlayer gamePlayer) {
        for (Spawn spawn : getSpawns()) {
            if (spawn.getGamePlayer() == gamePlayer) {
                return spawn;
            }
        }
        return null;
    }

    public Spawn getSpawn(int index) {
        for (Spawn spawn : getSpawns()) {
            if (spawn.getId() == index) {
                return spawn;
            }
        }
        return null;
    }

    public Spawn getTeamBase(int teamId) {
        for (Spawn spawn : getSpawns()) {
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

    public boolean removeComponent(ArenaComponent component) {
        if (spawnContainer.get(component.getId()) != null) {
            spawnContainer.remove(component.getId());
            return true;
        }
        return false;
    }

    private List<Spawn> getSpawns() {
        List<Spawn> spawns = new ArrayList<>();
        for (Spawn spawn : spawnContainer.getAll()) {
            spawns.add(spawn);
        }
        return spawns;
    }
}
