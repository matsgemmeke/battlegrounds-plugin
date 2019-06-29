package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.game.Barricade;
import com.matsg.battlegrounds.api.game.MobSpawn;
import org.bukkit.Location;

public class ZombiesMobSpawn implements MobSpawn {

    private Barricade barricade;
    private boolean locked;
    private int id;
    private Location location;

    public ZombiesMobSpawn(int id, Location location) {
        this.id = id;
        this.location = location;
        this.locked = true;
    }

    public Barricade getBarricade() {
        return barricade;
    }

    public void setBarricade(Barricade barricade) {
        this.barricade = barricade;
    }

    public int getId() {
        return id;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    private Location getBarricadeSpawnLocation() {
        Integer x1 = barricade.getMaximumPoint().getBlockX(), x2 = barricade.getMinimumPoint().getBlockX();
        Location loc1, loc2;

        if (x1.compareTo(x2) == 1) { // Compare the x coordinates to determine whether the barricade spans on the x or z axis.
            loc1 = barricade.getCenter().add(0, 0, 1);
            loc2 = barricade.getCenter().add(0, 0, -1);
        } else {
            loc1 = barricade.getCenter().add(1, 0, 0);
            loc2 = barricade.getCenter().add(-1, 0, 0);
        }

        Double dis1 = location.distance(loc1), dis2 = location.distance(loc2);
        return dis1.compareTo(dis2) == 1 ? loc1 : loc2;
    }

    public Location getSpawnLocation(BattleEntityType entityType) {
        switch (entityType) {
            case HELLHOUND:
                return getBarricadeSpawnLocation();
            case ZOMBIE:
                return location;
            default:
                throw new IllegalArgumentException("Invalid entity type \"" + entityType + "\"");
        }
    }
}
