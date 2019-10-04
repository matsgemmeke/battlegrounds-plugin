package com.matsg.battlegrounds;

import com.matsg.battlegrounds.util.Pair;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BattleSelectionTest {

    private World world;

    @Before
    public void setUp() {
        this.world = mock(World.class);
    }

    @Test
    public void createBasicSelection() {
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        Location firstSelectedPoint = selection.getFirstSelectedPoint();
        Location secondSelectedPoint = selection.getSecondSelectedPoint();
        World world = selection.getWorld();

        assertEquals(locationOne, firstSelectedPoint);
        assertEquals(locationTwo, secondSelectedPoint);
        assertEquals(this.world, world);
    }

    @Test
    public void checkIncompleteSelectionCompletion() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        boolean complete = selection.isComplete();

        assertFalse(complete);
    }

    @Test
    public void checkSelectionCompletion() {
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        boolean complete = selection.isComplete();

        assertTrue(complete);
    }

    @Test(expected = IncompleteExtentException.class)
    public void checkIncompleteSelectionContainsLocation() {
        Location insideLocation = new Location(world, 15.0, 15.0, 15.0);
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        selection.contains(insideLocation);
    }

    @Test
    public void checkSelectionDoesNotContainLocationWithDivergentX() {
        Location insideLocation = new Location(world, 5.0, 15.0, 15.0);
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        boolean contains = selection.contains(insideLocation);

        assertFalse(contains);
    }

    @Test
    public void checkSelectionDoesNotContainLocationWithDivergentY() {
        Location insideLocation = new Location(world, 15.0, 5.0, 15.0);
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        boolean contains = selection.contains(insideLocation);

        assertFalse(contains);
    }

    @Test
    public void checkSelectionDoesNotContainLocationWithDivergentZ() {
        Location insideLocation = new Location(world, 15.0, 15.0, 5.0);
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        boolean contains = selection.contains(insideLocation);

        assertFalse(contains);
    }

    @Test
    public void checkSelectionContainsLocation() {
        Location insideLocation = new Location(world, 15.0, 15.0, 15.0);
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        boolean contains = selection.contains(insideLocation);

        assertTrue(contains);
    }

    @Test(expected = IncompleteExtentException.class)
    public void checkIfIncompleteSelectionHasHeight() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        selection.getHeight();
    }

    @Test
    public void checkSelectionHasHeight() {
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        double height = selection.getHeight();

        assertEquals(20.0, height, 0.0); // Height is 30.0 - 10.0 = 10.0
    }

    @Test(expected = IncompleteExtentException.class)
    public void checkIncompleteSelectionHasWidth() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        selection.getWidth();
    }

    @Test
    public void checkSelectionHasWidth() {
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        double width = selection.getWidth();

        assertEquals(10.0, width, 0.0); // Width is 20.0 - 10.0 = 10.0
    }

    @Test(expected = IncompleteExtentException.class)
    public void checkIncompleteSelectionHasLength() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        selection.getLength();
    }

    @Test
    public void checkSelectionHasLength() {
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        double length = selection.getLength();

        assertEquals(30.0, length, 0.0); // Length is 40.0 - 10.0 = 30.0
    }

    @Test(expected = IncompleteExtentException.class)
    public void checkIncompleteSelectionHasAreaCoverage() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        selection.getArea();
    }

    @Test
    public void checkSelectionAreaCoverage() {
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        double area = selection.getArea();

        assertEquals(6000.0, area, 0.0); // Area is (20.0 - 10.0) * (30.0 - 10.0) * (40.0 - 10,0) = 6000.0
    }

    @Test(expected = IncompleteExtentException.class)
    public void calculateIncompleteSelectionCenterLocation() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        selection.getCenter();
    }

    @Test
    public void calculateSelectionCenterLocation() {
        Location locationOne = new Location(world, 10.0, 10.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 30.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        Location center = selection.getCenter();

        assertEquals(15.0, center.getX(), 0.0);
        assertEquals(20.0, center.getY(), 0.0);
        assertEquals(25.0, center.getZ(), 0.0);
        assertEquals(world, center.getWorld());
    }

    @Test(expected = IncompleteExtentException.class)
    public void calculateIncompleteSelectionMaximumLocation() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        selection.getMaximumPoint();
    }

    @Test
    public void calculateSelectionMaximumLocation() {
        Location locationOne = new Location(world, 10.0, 30.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 10.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        Location max = selection.getMaximumPoint();

        assertEquals(20.0, max.getX(), 0.0);
        assertEquals(30.0, max.getY(), 0.0);
        assertEquals(40.0, max.getZ(), 0.0);
    }

    @Test(expected = IncompleteExtentException.class)
    public void calculateIncompleteSelectionMinimumLocation() {
        Location location = new Location(world, 10.0, 10.0, 10.0);
        Pair<Location, Location> pair = new Pair<>(location, null);

        BattleSelection selection = new BattleSelection(pair, world);
        selection.getMinimumPoint();
    }

    @Test
    public void calculateSelectionMinimumLocation() {
        Location locationOne = new Location(world, 10.0, 30.0, 10.0);
        Location locationTwo = new Location(world, 20.0, 10.0, 40.0);
        Pair<Location, Location> pair = new Pair<>(locationOne, locationTwo);

        BattleSelection selection = new BattleSelection(pair, world);
        Location min = selection.getMinimumPoint();

        assertEquals(10.0, min.getX(), 0.0);
        assertEquals(10.0, min.getY(), 0.0);
        assertEquals(10.0, min.getZ(), 0.0);
    }
}
