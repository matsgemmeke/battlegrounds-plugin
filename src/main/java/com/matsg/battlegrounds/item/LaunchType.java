package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.Launcher;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.util.BattleRunnable;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public enum LaunchType {

    GRENADE(0) {
        public BukkitTask launch(Launcher launcher, Location direction) {
            GamePlayer gamePlayer = launcher.getGamePlayer();
            Lethal lethal = launcher.getLethal();

            Item item = gamePlayer.getPlayer().getWorld().dropItem(direction, lethal.getItemStack());
            item.setPickupDelay(1000);
            item.setVelocity(gamePlayer.getPlayer().getEyeLocation().getDirection().multiply(launcher.getLaunchSpeed()));

            return new BattleRunnable() {
                double range = 0.1; // Range constant

                public void run() {
                    Location location = item.getLocation();
                    GamePlayer[] players = launcher.getGame().getPlayerManager().getNearbyEnemyPlayers(launcher.getGame().getGameMode().getTeam(launcher.getGamePlayer()), direction, range);

                    if (players.length >= 1 || location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
                        launcher.explode(location);
                        item.remove();
                        cancel();
                    }
                }
            }.runTaskTimer(0, 1);
        }
    },
    ROCKET(1) {
        public BukkitTask launch(Launcher launcher, Location direction) {
            return new BattleRunnable() {
                double distance = 0.5, maxDistance = 50.0, range = 1.0; // Multiplier and range constant
                int i = 0;

                public void run() {
                    do {
                        Vector vector = direction.getDirection();
                        vector.multiply(distance);
                        direction.add(vector);

                        plugin.getVersion().spawnParticle(direction, "REDSTONE", 0, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, 0);

                        GamePlayer[] players = launcher.getGame().getPlayerManager().getNearbyEnemyPlayers(launcher.getGame().getGameMode().getTeam(launcher.getGamePlayer()), direction, range);

                        if (players.length >= 1 || direction.getBlock().getType().isSolid()) {
                            launcher.explode(direction);
                            cancel();
                            return;
                        }

                        direction.subtract(vector);
                        distance += 1.0;
                    } while (distance <= maxDistance && ++ i <= launcher.getLaunchSpeed());

                    if (distance > maxDistance) {
                        cancel(); //If the projectile distance exceeds the long range, stop the runnable
                    }
                }
            }.runTaskTimer(0, 1);
        }
    };

    private int id;

    LaunchType(int id) {
        this.id = id;
    }

    public static LaunchType valueOf(int id) {
        for (LaunchType launchType : values()) {
            if (launchType.id == id) {
                return launchType;
            }
        }
        throw new IllegalArgumentException();
    }

    public abstract BukkitTask launch(Launcher launcher, Location direction);
}
