package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Selection;
import com.matsg.battlegrounds.util.Pair;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.Location;
import org.bukkit.World;

public class BattleSelection implements Selection {

    private Pair<Location, Location> selection;
    private World world;

    public BattleSelection(Pair<Location, Location> selection, World world) {
        this.selection = selection;
        this.world = world;
    }

    public Location getFirstSelectedPoint() {
        return selection.left();
    }

    public Location getSecondSelectedPoint() {
        return selection.right();
    }

    public boolean contains(Location location) {
        Location max = getMaximumPoint(), min = getMinimumPoint();
        return isComplete()
                && new IntRange(min.getX(), max.getX()).containsDouble(location.getX())
                && new IntRange(min.getY(), max.getY()).containsDouble(location.getY())
                && new IntRange(min.getZ(), max.getZ()).containsDouble(location.getZ());
    }

    public double getArea() {
        return getWidth() * getHeight() * getLength();
    }

    public Location getCenter() {
        if (!isComplete()) {
            throw new IncompleteExtentException("Cannot calculate center of incomplete selection");
        }
        return selection.left().toVector().add(selection.right().toVector()).multiply(0.5).toLocation(world);
    }

    public double getHeight() {
        if (!isComplete()) {
            throw new IncompleteExtentException("Cannot calculate height of incomplete selection");
        }
        return getMaximumPoint().getY() - getMinimumPoint().getY();
    }

    public double getLength() {
        if (!isComplete()) {
            throw new IncompleteExtentException("Cannot calculate length of incomplete selection");
        }
        return getMaximumPoint().getZ() - getMinimumPoint().getZ();
    }

    public Location getMaximumPoint() {
        if (!isComplete()) {
            throw new IncompleteExtentException("Cannot calculate maximum point of incomplete selection");
        }

        Location left = selection.left(), right = selection.right();

        return new Location(
                left.getWorld(),
                Math.max(left.getX(), right.getX()),
                Math.max(left.getY(), right.getY()),
                Math.max(left.getZ(), right.getZ())
        );
    }

    public Location getMinimumPoint() {
        if (!isComplete()) {
            throw new IncompleteExtentException("Cannot calculate minimum point of incomplete selection");
        }

        Location left = selection.left(), right = selection.right();

        return new Location(
                left.getWorld(),
                Math.min(left.getX(), right.getX()),
                Math.min(left.getY(), right.getY()),
                Math.min(left.getZ(), right.getZ())
        );
    }

    public double getWidth() {
        if (!isComplete()) {
            throw new IncompleteExtentException("Cannot calculate width of incomplete selection");
        }
        return getMaximumPoint().getX() - getMinimumPoint().getX();
    }

    public World getWorld() {
        return world;
    }

    public boolean isComplete() {
        return selection.left() != null && selection.right() != null;
    }
}
