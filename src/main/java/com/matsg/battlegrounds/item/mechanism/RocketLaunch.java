package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Launcher;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RocketLaunch implements LaunchSystem {

    private double launchSpeed;
    private Launcher launcher;
    private TaskRunner taskRunner;
    private Version version;

    public RocketLaunch(double launchSpeed, TaskRunner taskRunner, Version version) {
        this.launchSpeed = launchSpeed;
        this.taskRunner = taskRunner;
        this.version = version;
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

                    version.spawnColoredParticle(direction, "REDSTONE", Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

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
