package com.matsg.battlegrounds.game;

import com.matsg.battlegrounds.IncompleteExtentException;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Spawn;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.entity.BattleGamePlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BattleArenaTest {

    private World world;

    @Before
    public void setUp() {
        this.world = mock(World.class);
    }

    @Test
    public void createBasicArena() {
        String name = "Arena";
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(name, world, max, min);

        assertEquals(name, arena.getName());
        assertEquals(max, arena.getMaximumPoint());
        assertEquals(min, arena.getMinimumPoint());
        assertEquals(world, arena.getWorld());
        assertFalse(arena.isActive());
    }

    @Test
    public void canBeSetToActive() {
        BattleArena arena = new BattleArena(null, world, null, null);
        arena.setActive(true);

        assertTrue(arena.isActive());
    }

    @Test
    public void canCheckWhetherArenaHasBorders() {
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);

        assertTrue(arena.hasBorders());
    }

    @Test
    public void canCheckWhetherArenaHasNoBordersIfMaximumPointIsMissing() {
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, null, min);

        assertFalse(arena.hasBorders());
    }

    @Test
    public void canCheckWhetherArenaHasNoBordersIfMinimumPointIsMissing() {
        Location max = new Location(world, 20.0, 30.0, 40.0);

        BattleArena arena = new BattleArena(null, world, max, null);

        assertFalse(arena.hasBorders());
    }

    @Test
    public void containsLocationWhenNotHavingBorders() {
        Location location = new Location(world, 15.0, 15.0, 15.0);

        BattleArena arena = new BattleArena(null, world, null, null);
        boolean contains = arena.contains(location);

        assertFalse(contains);
    }

    @Test
    public void containsLocationWithDivergentX() {
        Location location = new Location(world, 5.0, 15.0, 15.0);
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        boolean contains = arena.contains(location);

        assertFalse(contains);
    }

    @Test
    public void containsLocationWithDivergentY() {
        Location location = new Location(world, 15.0, 5.0, 15.0);
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        boolean contains = arena.contains(location);

        assertFalse(contains);
    }

    @Test
    public void containsLocationWithDivergentZ() {
        Location location = new Location(world, 15.0, 15.0, 5.0);
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        boolean contains = arena.contains(location);

        assertFalse(contains);
    }

    @Test
    public void containsLocation() {
        Location location = new Location(world, 15.0, 15.0, 15.0);
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        boolean contains = arena.contains(location);

        assertTrue(contains);
    }

    @Test(expected = IncompleteExtentException.class)
    public void calculateAreaRangeWithoutBorders() {
        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getArea();
    }

    @Test
    public void calculateAreaRange() {
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        double area = arena.getArea();

        assertEquals(6000.0, area, 0.0); // Area is 10.0 * 20.0 * 30.0 = 6000.0
    }

    @Test(expected = IncompleteExtentException.class)
    public void calculateCenterLocationWithoutBorders() {
        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getCenter();
    }

    @Test
    public void calculateCenterLocation() {
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        Location center = arena.getCenter();

        assertEquals(15.0, center.getX(), 0.0);
        assertEquals(20.0, center.getY(), 0.0);
        assertEquals(25.0, center.getZ(), 0.0);
        assertEquals(world, center.getWorld());
    }

    @Test
    public void canFindComponentById() {
        int id = 1;
        Spawn spawn = new ArenaSpawn(id, null, 0);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(spawn, arena.getComponent(id));
    }

    @Test
    public void canFindComponentCount() {
        Spawn spawn = new ArenaSpawn(1, null, 0);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(1, arena.getComponentCount());
    }

    @Test
    public void canFindAllArenaComponents() {
        Spawn spawn = new ArenaSpawn(1, null, 0);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(1, arena.getComponents().size());
        assertTrue(arena.getComponents().contains(spawn));
    }

    @Test
    public void canFindComponentByLocation() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Spawn spawn = new ArenaSpawn(1, location, 0);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(spawn, arena.getComponent(location));
    }

    @Test
    public void canNotFindComponentWithDifferentLocation() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Spawn spawn = new ArenaSpawn(1, location.clone().add(5.0, 5.0, 5.0), 0);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertNull(arena.getComponent(location));
    }

    @Test
    public void canFindAllGenericArenaComponents() {
        Spawn spawn = new ArenaSpawn(1, null, 0);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(1, arena.getComponents(Spawn.class).size());
        assertTrue(arena.getComponents(Spawn.class).contains(spawn));
    }

    @Test(expected = IncompleteExtentException.class)
    public void calculateArenaWidthWithoutBorders() {
        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getWidth();
    }

    @Test
    public void calculateArenaWidth() {
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        double width = arena.getWidth();

        assertEquals(10.0, width, 0.0);
    }

    @Test(expected = IncompleteExtentException.class)
    public void calculateArenaHeightWithoutBorders() {
        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getHeight();
    }

    @Test
    public void calculateArenaHeight() {
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        double height = arena.getHeight();

        assertEquals(20.0, height, 0.0);
    }

    @Test(expected = IncompleteExtentException.class)
    public void calculateArenaLengthWithoutBorders() {
        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getLength();
    }

    @Test
    public void calculateArenaLength() {
        Location max = new Location(world, 20.0, 30.0, 40.0);
        Location min = new Location(world, 10.0, 10.0, 10.0);

        BattleArena arena = new BattleArena(null, world, max, min);
        double length = arena.getLength();

        assertEquals(30.0, length, 0.0);
    }

    @Test
    public void canFindRandomSpawn() {
        BattleArena arena = new BattleArena(null, world, null, null);

        for (int i = 1; i <= 10; i++) {
            arena.getSpawnContainer().add(new ArenaSpawn(i, null, 0));
        }

        Spawn spawn = arena.getRandomSpawn();

        assertNotNull(spawn);
    }

    @Test
    public void canFindRandomSpawnWithTeamId() {
        int teamId = 1;

        BattleArena arena = new BattleArena(null, world, null, null);

        for (int i = 1; i <= 10; i++) {
            arena.getSpawnContainer().add(new ArenaSpawn(i, null, i));
        }

        Spawn spawn = arena.getRandomSpawn(teamId);

        assertEquals(teamId, spawn.getTeamId());
    }

    @Test
    public void canFindRandomSpawnWithLocationDistance() {
        double distance = 5.0;
        Location location = new Location(world, 5.0, 5.0, 5.0);

        BattleArena arena = new BattleArena(null, world, null, null);

        for (int i = 1; i <= 10; i++) {
            Location spawnLocation = new Location(world, i, i, i);

            arena.getSpawnContainer().add(new ArenaSpawn(i, spawnLocation, 1));
        }

        Spawn spawn = arena.getRandomSpawn(location, distance);

        assertTrue(Math.abs(spawn.getLocation().getX() - distance) > distance / 2);
        assertTrue(Math.abs(spawn.getLocation().getY() - distance) > distance / 2);
        assertTrue(Math.abs(spawn.getLocation().getZ() - distance) > distance / 2);
    }

    @Test
    public void canFindRandomSpawnWithLocationDistanceAndTeamId() {
        double distance = 5.0;
        Location location = new Location(world, 5.0, 5.0, 5.0);

        BattleArena arena = new BattleArena(null, world, null, null);

        for (int i = 1; i <= 10; i++) {
            Location spawnLocation = new Location(world, i, i, i);

            arena.getSpawnContainer().add(new ArenaSpawn(i, spawnLocation, i));
        }

        Spawn spawn = arena.getRandomSpawn(1, location, distance);

        assertTrue(Math.abs(spawn.getLocation().getX() - distance) > 3.0);
        assertTrue(Math.abs(spawn.getLocation().getY() - distance) > 3.0);
        assertTrue(Math.abs(spawn.getLocation().getZ() - distance) > 3.0);
    }

    @Test
    public void canFindSpawnWithPlayerOccupant() {
        GamePlayer gamePlayer = new BattleGamePlayer(null, null);
        Spawn spawn = new ArenaSpawn(1, null, 1);

        spawn.setOccupant(gamePlayer);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(spawn, arena.getSpawn(gamePlayer));
    }

    @Test
    public void canFindSpawnWithTeamOccupant() {
        GamePlayer gamePlayer = new BattleGamePlayer(null, null);
        Spawn spawn = new ArenaSpawn(1, null, 1);
        Team team = new BattleTeam(1, null, null, null);

        gamePlayer.setTeam(team);
        spawn.setOccupant(gamePlayer);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(spawn, arena.getSpawn(gamePlayer));
    }

    @Test
    public void canNotFindSpawnWithPlayerOrTheirTeam() {
        GamePlayer gamePlayer = new BattleGamePlayer(null, null);
        Spawn spawn = new ArenaSpawn(1, null, 1);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertNull(arena.getSpawn(gamePlayer));
    }

    @Test
    public void canFindSpawnById() {
        int id = 1;
        Spawn spawn = new ArenaSpawn(id, null, 1);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(spawn, arena.getSpawn(id));
    }

    @Test
    public void canNotFindSpawnByIdIfItDoesNotExist() {
        int id = 1;
        Spawn spawn = new ArenaSpawn(id + 1, null, 1);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertNull(arena.getSpawn(id));
    }

    @Test
    public void countAmountOfSpawns() {
        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(new ArenaSpawn(1, null, 1));

        assertEquals(1, arena.getSpawnCount());
    }

    @Test
    public void countAmountOfSpawnsWithTeamId() {
        int teamId = 1;

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(new ArenaSpawn(1, null, teamId));

        assertEquals(1, arena.getSpawnCount(teamId));
    }

    @Test
    public void canFindTeamBaseSpawn() {
        int teamId = 1;
        Spawn spawn = new ArenaSpawn(1, null, teamId);

        spawn.setTeamBase(true);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertEquals(spawn, arena.getTeamBase(teamId));
    }

    @Test
    public void canNotFindTeamBaseSpawnIfItDoesNotExist() {
        int teamId = 1;

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(new ArenaSpawn(1, null, teamId));

        assertNull(arena.getTeamBase(teamId));
    }

    @Test
    public void canRemoveArenaComponent() {
        Spawn spawn = new ArenaSpawn(1, null, 1);

        BattleArena arena = new BattleArena(null, world, null, null);
        arena.getSpawnContainer().add(spawn);

        assertTrue(arena.removeComponent(spawn));
    }

    @Test
    public void canNotRemoveArenaComponentIfItIsNotAdded() {
        Spawn spawn = new ArenaSpawn(1, null, 1);

        BattleArena arena = new BattleArena(null, world, null, null);

        assertFalse(arena.removeComponent(spawn));
    }
}
