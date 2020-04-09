package com.matsg.battlegrounds.nms.v1_12_R1;

import com.matsg.battlegrounds.api.game.Obstacle;
import com.matsg.battlegrounds.util.XMaterial;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.PathEntity;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PathfinderGoalEnterArena extends PathfinderGoal {

    private CustomZombie zombie;
    private Navigation navigation;
    private Obstacle obstacle;
    private Plugin plugin;

    public PathfinderGoalEnterArena(CustomZombie zombie, Obstacle obstacle, Plugin plugin) {
        this.zombie = zombie;
        this.obstacle = obstacle;
        this.plugin = plugin;
        this.navigation = (Navigation) zombie.getNavigation();
    }

    public boolean a() {
        return true;
    }

    public void c() {
        new BukkitRunnable() {
            boolean passed = true;

            public void run() {
                if (zombie.getBukkitEntity() == null || zombie.dead) {
                    cancel();
                    return;
                }

                if (obstacle != null && !obstacle.isOpen()) {
                    passed = false;
                }

                if (!passed) { // Break the barricade first before targetting a player
                    Location location = obstacle.getCenter();
                    location.setY(obstacle.getMinimumPoint().getY());

                    PathEntity pathEntity = navigation.a(location.getX(), location.getY(), location.getZ());
                    navigation.a(pathEntity, 1);

                    if (obstacle.isOpen() && zombie.getBukkitEntity().getLocation().distanceSquared(location) <= 1.0) {
                        passed = true;
                        return;
                    }

                    if (obstacle.isOpen() || zombie.getBukkitEntity().getLocation().distance(location) > 2.5) {
                        return;
                    }

                    Block block;
                    int i = -1;

                    do {
                        block = obstacle.getBlocks()[++i];
                    } while (block.getType() == Material.AIR);

                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                    block.setType(XMaterial.AIR.parseMaterial());

                    if (!obstacle.isOpen()) {
                        return;
                    }
                }

                cancel();
                zombie.resetDefaultPathfinderGoals();
                zombie.setHostile(true);
            }

        }.runTaskTimer(plugin, 0, 40);
    }
}
