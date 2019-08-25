package com.matsg.battlegrounds.nms.v1_12_R1;

import com.matsg.battlegrounds.mode.zombies.component.Barricade;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.mode.zombies.component.MobSpawn;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.XMaterial;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.PathEntity;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class PathfinderGoalEnterArena extends PathfinderGoal {

    private CustomZombie zombie;
    private Game game;
    private MobSpawn mobSpawn;
    private Navigation navigation;

    public PathfinderGoalEnterArena(Game game, CustomZombie zombie, MobSpawn mobSpawn) {
        this.game = game;
        this.mobSpawn = mobSpawn;
        this.navigation = (Navigation) zombie.getNavigation();
        this.zombie = zombie;
    }

    public boolean a() {
        return true;
    }

    public void c() {
        new BattleRunnable() {
            boolean passed = true;

            public void run() {
                if (zombie.getBukkitEntity() == null || zombie.dead) {
                    cancel();
                    return;
                }

                Barricade barricade = mobSpawn.getBarricade();

                if (barricade != null && !barricade.isOpen()) {
                    passed = false;
                }

                if (!passed) { // Break the barricade first before targetting a player
                    Location location = barricade.getCenter();
                    location.setY(barricade.getMinimumPoint().getY());

                    PathEntity pathEntity = navigation.a(location.getX(), location.getY(), location.getZ());
                    navigation.a(pathEntity, 1);

                    if (barricade.isOpen() && zombie.getBukkitEntity().getLocation().distanceSquared(location) <= 1.0) {
                        passed = true;
                        return;
                    }

                    if (barricade.isOpen() || zombie.getBukkitEntity().getLocation().distance(location) > 2.5) {
                        return;
                    }

                    Block block;
                    int i = -1;

                    do {
                        block = barricade.getBlocks()[++ i];
                    } while (block.getType() == Material.AIR);

                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                    block.setType(XMaterial.AIR.parseMaterial());

                    if (!barricade.isOpen()) {
                        return;
                    }
                }

                cancel();
                zombie.resetDefaultPathfinderGoals();
                zombie.setHostile(true);
            }

        }.runTaskTimer(0, 40);
    }
}