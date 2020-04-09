package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Launcher;
import com.matsg.battlegrounds.api.item.Lethal;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GrenadeLaunch implements LaunchSystem {

    private double launchSpeed;
    private Launcher launcher;
    private TaskRunner taskRunner;

    public GrenadeLaunch(double launchSpeed, TaskRunner taskRunner) {
        this.launchSpeed = launchSpeed;
        this.taskRunner = taskRunner;
    }

    public Launcher getWeapon() {
        return launcher;
    }

    public void setWeapon(Launcher launcher) {
        this.launcher = launcher;
    }

    public void launch(Location direction) {
        GamePlayer gamePlayer = launcher.getGamePlayer();
        Lethal lethal = launcher.getLethal();
        Player player = gamePlayer.getPlayer();

        Item item = player.getWorld().dropItem(direction, lethal.getItemStack());
        item.setPickupDelay(1000);
        item.setVelocity(player.getEyeLocation().getDirection().multiply(launchSpeed));

        taskRunner.runTaskTimer(new BukkitRunnable() {
            double range = 0.1; // Range constant

            public void run() {
                Location location = item.getLocation();
                Team team = gamePlayer.getTeam();

                BattleEntity[] entities = launcher.getContext().getNearbyEnemies(direction, team, range);

                if (entities.length >= 1 || location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid()) {
                    launcher.explode(location);
                    item.remove();
                    cancel();
                }
            }
        }, 0, 1);
    }
}
