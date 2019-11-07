package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Launcher;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RocketLaunch implements LaunchSystem {

    private double launchSpeed;
    private InternalsProvider internals;
    private Launcher launcher;
    private TaskRunner taskRunner;

    public RocketLaunch(double launchSpeed, InternalsProvider internals, TaskRunner taskRunner) {
        this.launchSpeed = launchSpeed;
        this.internals = internals;
        this.taskRunner = taskRunner;
    }

    public Launcher getWeapon() {
        return launcher;
    }

    public void setWeapon(Launcher launcher) {
        this.launcher = launcher;
    }

    public void launch(Location direction) {
        taskRunner.runTaskTimer(new BukkitRunnable() {
            double distance = 0.5, maxDistance = 50.0, range = 1.0; // Multiplier and range constants
            int i = 0;

            public void run() {
                do {
                    Vector vector = direction.getDirection();
                    vector.multiply(distance);
                    direction.add(vector);

                    internals.spawnColoredParticle(direction, "REDSTONE", Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

                    Team team = launcher.getGamePlayer().getTeam();

                    BattleEntity[] entities = launcher.getContext().getNearbyEntities(direction, team, range);

                    if (entities.length >= 1 || direction.getBlock().getType().isSolid()) {
                        launcher.explode(direction);
                        cancel();
                        return;
                    }

                    direction.subtract(vector);
                    distance += 1.0;
                } while (distance <= maxDistance && ++i <= launchSpeed);

                if (distance > maxDistance) {
                    cancel(); // If the projectile distance exceeds the long range, stop the runnable
                }
            }
        }, 0, 1);
    }
}
