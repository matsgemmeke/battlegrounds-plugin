package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.IncompleteExtentException;
import com.matsg.battlegrounds.api.game.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import org.apache.commons.lang.math.IntRange;
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
        this.spawnContainer = new BattleComponentContainer<>();
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
                && new IntRange(minimumPoint.getX(), maximumPoint.getX()).containsDouble(location.getX())
                && new IntRange(minimumPoint.getY(), maximumPoint.getY()).containsDouble(location.getY())
                && new IntRange(minimumPoint.getZ(), maximumPoint.getZ()).containsDouble(location.getZ());
    }

    public double getArea() {
        return getWidth() * getHeight() * getLength();
    }

    public Location getCenter() {
        if (!hasBorders()) {
            throw new IncompleteExtentException("Cannot calculate center of arena without borders");
        }
        return maximumPoint.toVector().add(minimumPoint.toVector()).multiply(0.5).toLocation(world);
    }

    public ArenaComponent getComponent(int id) {
        return spawnContainer.get(id);
    }

    public ArenaComponent getComponent(Location location) {
        for (Spawn spawn : spawnContainer.getAll()) {
            if (spawn.getLocation().equals(location)) {
                return spawn;
            }
        }
        return null;
    }

    public int getComponentCount() {
        return spawnContainer.getAll().size();
    }

    public Collection<ArenaComponent> getComponents() {
        List<ArenaComponent> list = new ArrayList<>();
        list.addAll(spawnContainer.getAll());
        return Collections.unmodifiableList(list);
    }

    public <T extends ArenaComponent> Collection<T> getComponents(Class<T> componentClass) {
        List<T> list = new ArrayList<>();
        for (ArenaComponent component : getComponents()) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                list.add((T) component);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public double getHeight() {
        if (!hasBorders()) {
            throw new IncompleteExtentException("Cannot calculate height of arena without borders");
        }
        return maximumPoint.getY() - minimumPoint.getY();
    }

    public double getLength() {
        if (!hasBorders()) {
            throw new IncompleteExtentException("Cannot calculate length of arena without borders");
        }
        return maximumPoint.getZ() - minimumPoint.getZ();
    }

    public Spawn getRandomSpawn() {
        List<Spawn> spawns = new ArrayList<>(spawnContainer.getAll());
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn.isOccupied());

        return spawn;
    }

    public Spawn getRandomSpawn(int teamId) {
        List<Spawn> spawns = new ArrayList<>(spawnContainer.getAll());
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn.isOccupied() || spawn.getTeamId() != teamId);

        return spawn;
    }

    public Spawn getRandomSpawn(Location location, double distance) {
        List<Spawn> spawns = new ArrayList<>(spawnContainer.getAll());
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn.isOccupied() || location.distance(spawn.getLocation()) < distance);

        return spawn;
    }

    public Spawn getRandomSpawn(int teamId, Location location, double distance) {
        List<Spawn> spawns = new ArrayList<>(spawnContainer.getAll());
        Random random = new Random();
        Spawn spawn;

        do {
            spawn = spawns.get(random.nextInt(spawns.size()));
        } while (spawn.isOccupied() || spawn.getTeamId() != teamId || location.distance(spawn.getLocation()) < distance);

        return spawn;
    }

    public Spawn getSpawn(GamePlayer gamePlayer) {
        for (Spawn spawn : spawnContainer.getAll()) {
            if (spawn.getOccupant() == gamePlayer || gamePlayer.getTeam() != null && spawn.getOccupant() == gamePlayer.getTeam()) {
                return spawn;
            }
        }
        return null;
    }

    public Spawn getSpawn(int index) {
        for (Spawn spawn : spawnContainer.getAll()) {
            if (spawn.getId() == index) {
                return spawn;
            }
        }
        return null;
    }

    public int getSpawnCount() {
        return spawnContainer.getAll().size();
    }

    public int getSpawnCount(int teamId) {
        int count = 0;

        for (Spawn spawn : spawnContainer.getAll()) {
            if (spawn.getTeamId() == teamId) {
                count++;
            }
        }

        return count;
    }

    public Spawn getTeamBase(int teamId) {
        for (Spawn spawn : spawnContainer.getAll()) {
            if (spawn.getTeamId() == teamId && spawn.isTeamBase()) {
                return spawn;
            }
        }
        return null;
    }

    public double getWidth() {
        if (!hasBorders()) {
            throw new IncompleteExtentException("Cannot calculate width of arena without borders");
        }
        return maximumPoint.getX() - minimumPoint.getX();
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
}
