package com.matsg.battlegrounds.game.component;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Arena;
import com.matsg.battlegrounds.api.game.Door;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.game.Section;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.util.ActionBar;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public class ArenaDoor implements Door {

    private boolean locked;
    private Game game;
    private int id;
    private Location maximumPoint, minimumPoint;
    private Material material;
    private Section section;
    private World world;

    public ArenaDoor(
            int id,
            Game game,
            Section section,
            World world,
            Location maximumPoint,
            Location minimumPoint,
            Material material
    ) {
        this.id = id;
        this.game = game;
        this.section = section;
        this.world = world;
        this.maximumPoint = maximumPoint;
        this.minimumPoint = minimumPoint;
        this.material = material;
        this.locked = true;
    }

    public int getId() {
        return id;
    }

    public Location getMaximumPoint() {
        return maximumPoint;
    }

    public Location getMinimumPoint() {
        return minimumPoint;
    }

    public Section getSection() {
        return section;
    }

    public World getWorld() {
        return world;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean contains(Location location) {
        return location != null
                && location.getWorld() == world
                && location.getX() >= minimumPoint.getX() && location.getX() <= maximumPoint.getX()
                && location.getY() >= minimumPoint.getY() && location.getY() <= maximumPoint.getY()
                && location.getZ() >= minimumPoint.getZ() && location.getZ() <= maximumPoint.getZ();
    }

    public int getArea() {
        return getWidth() * getHeight() * getLength();
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

    public boolean onInteract(GamePlayer gamePlayer, Block block) {
        // If the door was unlocked, it can not be unlocked again.
        if (!locked) {
            return false;
        }

        // If the player does not have enough price they can not unlock the door.
        if (gamePlayer.getPoints() < section.getPrice()) {
            ActionBar.UNSUFFICIENT_POINTS.send(gamePlayer.getPlayer());
            return true;
        }

        game.getPlayerManager().givePoints(gamePlayer, -section.getPrice());
        section.setLocked(false);

        BattleSound.DOOR_OPEN.play(game, getCenter());

        return true;
    }

    public boolean onLook(GamePlayer gamePlayer, Block block) {
        // If the door was unlocked, it does not accept look interactions.
        if (!locked) {
            return false;
        }

        ActionBar.DOOR.send(gamePlayer.getPlayer(), new Placeholder("bg_price", section.getPrice()));

        return true;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;

        Material material = locked ? this.material : Material.AIR;

        for (int x = minimumPoint.getBlockX(); x <= maximumPoint.getBlockX(); x ++) {
            for (int y = minimumPoint.getBlockY(); y <= maximumPoint.getBlockY(); y ++) {
                for (int z = minimumPoint.getBlockZ(); z <= maximumPoint.getBlockZ(); z ++) {
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(material);
                }
            }
        }
    }
}
