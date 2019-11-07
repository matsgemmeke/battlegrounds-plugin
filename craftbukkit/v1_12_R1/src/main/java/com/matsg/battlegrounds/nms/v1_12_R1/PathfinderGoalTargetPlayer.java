package com.matsg.battlegrounds.nms.v1_12_R1;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.Game;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PathfinderGoalTargetPlayer extends PathfinderGoal {

    private Game game;
    private Mob mob;
    private Plugin plugin;

    public PathfinderGoalTargetPlayer(Game game, Mob mob, Plugin plugin) {
        this.game = game;
        this.mob = mob;
        this.plugin = plugin;
    }

    public boolean a() {
        return true;
    }

    public void c() {
        new BukkitRunnable() {
            public void run() {
                if (mob.getBukkitEntity() == null) {
                    mob.remove();
                    cancel();
                    return;
                }

                GamePlayer gamePlayer = game.getPlayerManager().getNearestPlayer(mob.getBukkitEntity().getLocation());

                if (gamePlayer != null && mob.isHostileTowards(gamePlayer)) {
                    mob.setTarget(gamePlayer.getLocation());
                }
            }
        }.runTaskTimer(plugin, 0, 100);
    }
}
