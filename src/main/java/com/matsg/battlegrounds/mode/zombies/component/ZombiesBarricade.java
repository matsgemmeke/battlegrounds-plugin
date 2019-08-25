package com.matsg.battlegrounds.mode.zombies.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.item.ItemSlot;
import com.matsg.battlegrounds.util.ActionBar;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ZombiesBarricade implements Barricade {

    private boolean locked;
    private int id;
    private List<Mob> mobs;
    private Location maximumPoint;
    private Location minimumPoint;
    private Material material;
    private MobSpawn mobSpawn;
    private World world;

    public ZombiesBarricade(int id, MobSpawn mobSpawn, Location maximumPoint, Location minimumPoint, World world, Material material) {
        this.id = id;
        this.mobSpawn = mobSpawn;
        this.maximumPoint = maximumPoint;
        this.minimumPoint = minimumPoint;
        this.world = world;
        this.material = material;
        this.locked = true;
        this.mobs = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public Location getMaximumPoint() {
        return maximumPoint;
    }

    public Location getMinimumPoint() {
        return minimumPoint;
    }

    public List<Mob> getMobs() {
        return mobs;
    }

    public World getWorld() {
        return world;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean contains(Location location) {
        return location.getBlockX() >= minimumPoint.getBlockX() && location.getBlockX() <= maximumPoint.getBlockX()
                && location.getBlockY() >= minimumPoint.getBlockY() && location.getBlockY() <= maximumPoint.getBlockY()
                && location.getBlockZ() >= minimumPoint.getBlockZ() && location.getBlockZ() <= maximumPoint.getBlockZ();
    }

    public int getArea() {
        return getWidth() * getHeight() * getLength();
    }

    public Block[] getBlocks() {
        List<Block> list = new ArrayList<>();
        for (int x = minimumPoint.getBlockX(); x <= maximumPoint.getBlockX(); x ++) {
            for (int y = minimumPoint.getBlockY(); y <= maximumPoint.getBlockY(); y ++) {
                for (int z = minimumPoint.getBlockZ(); z <= maximumPoint.getBlockZ(); z ++) {
                    list.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return list.toArray(new Block[list.size()]);
    }

    public Location getCenter() {
        return maximumPoint.toVector().add(minimumPoint.toVector()).multiply(0.5).toLocation(world);
    }

    public int getHeight() {
        return maximumPoint.getBlockY() - minimumPoint.getBlockY();
    }

    public int getLength() {
        return maximumPoint.getBlockZ() - minimumPoint.getBlockZ();
    }

    public int getWidth() {
        return maximumPoint.getBlockX() - minimumPoint.getBlockX();
    }

    public boolean isClosed() {
        for (Block block : getBlocks()) {
            if (block.getType() != material) {
                return false;
            }
        }
        return true;
    }

    public boolean isOpen() {
        for (Block block : getBlocks()) {
            if (block.getType() == material) {
                return false;
            }
        }
        return true;
    }

    public boolean onConstruct(GamePlayer gamePlayer, Block block) {
        // If the mob spawn is locked or not active, it does not accept constructions
        if (mobSpawn.isLocked()) {
            return false;
        }

        // If there are any mobs are tasked to break the barricade, the barricade will not repair
        if (mobs.size() > 0) {
            return false;
        }

        // If the barricade does not contain the constructed block, the barricade will not repair
        if (!contains(block.getLocation())) {
            return false;
        }

        // If the player is not holding the repair tool, the barricade will not repair
        if (gamePlayer.getPlayer().getInventory().getHeldItemSlot() != ItemSlot.MISCELLANEOUS.getSlot()) {
            return false;
        }

        block.setType(material);
        return true;
    }

    public boolean onPass(GamePlayer gamePlayer, Location from, Location to) {
        if (isClosed() || contains(from) || !contains(to)) {
            return false;
        }

        gamePlayer.getPlayer().teleport(gamePlayer.getPlayer().getLocation().add(from.toVector().subtract(to.toVector()).normalize()));

        ActionBar.PLAYER_PASS_BARRICADE.send(gamePlayer.getPlayer());
        return true;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;

        for (Block block : getBlocks()) {
            block.setType(material);
        }
    }
}
