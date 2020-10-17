package com.matsg.battlegrounds.nms.v1_11_R1;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.entity.PlayerState;
import com.matsg.battlegrounds.api.game.Game;
import net.minecraft.server.v1_11_R1.PathfinderGoal;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PathfinderGoalTargetPlayer extends PathfinderGoal {

    private static final int RUNNABLE_DELAY = 0;
    private Game game;
    private GamePlayer currentTarget;
    private int period;
    private Mob mob;
    private Plugin plugin;

    public PathfinderGoalTargetPlayer(Game game, Mob mob, Plugin plugin, int period) {
        this.game = game;
        this.mob = mob;
        this.plugin = plugin;
        this.period = period;
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

                GamePlayer gamePlayer = game.getPlayerManager().getNearestPlayer(mob.getBukkitEntity().getLocation(), g -> g.getState() == PlayerState.ACTIVE);

                if (gamePlayer != null && currentTarget != gamePlayer && mob.isHostileTowards(gamePlayer)) {
                    currentTarget = gamePlayer;

                    mob.setTarget(gamePlayer.getPlayer());
                }
            }
        }.runTaskTimer(plugin, RUNNABLE_DELAY, period);
    }
}
