package com.matsg.battlegrounds;

import com.matsg.battlegrounds.api.Selection;
import com.matsg.battlegrounds.util.Pair;
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
                && location != null
                && location.getX() >= min.getX() && location.getX() <= max.getX()
                && location.getY() >= min.getY() && location.getY() <= max.getY()
                && location.getZ() >= min.getZ() && location.getZ() <= max.getZ();
    }

    public int getArea() {
        return getWidth() * getHeight() * getLength();
    }

    public Location getCenter() {
        if (!isComplete()) {
            return null;
        }
        return selection.left().toVector().add(selection.right().toVector()).multiply(0.5).toLocation(world);
    }

    public int getHeight() {
        if (!isComplete()) {
            return -1;
        }
        return getMaximumPoint().getBlockY() - getMinimumPoint().getBlockY();
    }

    public int getLength() {
        if (!isComplete()) {
            return -1;
        }
        return getMaximumPoint().getBlockZ() - getMinimumPoint().getBlockZ();
    }

    public Location getMaximumPoint() {
        if (!isComplete()) {
            return null;
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
            return null;
        }

        Location left = selection.left(), right = selection.right();

        return new Location(
                left.getWorld(),
                Math.min(left.getX(), right.getX()),
                Math.min(left.getY(), right.getY()),
                Math.min(left.getZ(), right.getZ())
        );
    }

    public int getWidth() {
        if (!isComplete()) {
            return -1;
        }
        return getMaximumPoint().getBlockX() - getMinimumPoint().getBlockX();
    }

    public World getWorld() {
        return world;
    }

    public boolean isComplete() {
        return selection.left() != null && selection.right() != null;
    }
}
