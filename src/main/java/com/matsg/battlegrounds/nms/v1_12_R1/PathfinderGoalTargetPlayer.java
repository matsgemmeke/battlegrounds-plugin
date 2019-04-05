package com.matsg.battlegrounds.nms.v1_12_R1;

import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.util.BattleRunnable;
import net.minecraft.server.v1_12_R1.PathfinderGoal;

public class PathfinderGoalTargetPlayer extends PathfinderGoal {

    private Game game;
    private Mob mob;

    public PathfinderGoalTargetPlayer(Mob mob, Game game) {
        this.game = game;
        this.mob = mob;
    }

    public boolean a() {
        return true;
    }

    public void c() {
        new BattleRunnable() {
            public void run() {
                if (mob.getBukkitEntity() == null) {
                    mob.remove();
                    cancel();
                    return;
                }
                GamePlayer gamePlayer = game.getPlayerManager().getNearestPlayer(mob.getBukkitEntity().getLocation());
                if (gamePlayer != null && mob.isHostile()) {
                    mob.setTarget(gamePlayer.getLocation());
                }
            }
        }.runTaskTimer(0, 20);
    }
}
