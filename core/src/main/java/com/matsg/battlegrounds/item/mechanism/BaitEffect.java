package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Item;

public class BaitEffect implements TacticalEffect {

    private int duration;
    private Tactical tactical;
    private TaskRunner taskRunner;

    public BaitEffect(TaskRunner taskRunner, int duration) {
        this.taskRunner = taskRunner;
        this.duration = duration;
    }

    public Tactical getWeapon() {
        return tactical;
    }

    public void setWeapon(Tactical tactical) {
        this.tactical = tactical;
    }

    public void applyEffect(Item item) {
        taskRunner.runTaskLater(() -> {
            Location location = item.getLocation();
            Team team = tactical.getGamePlayer().getTeam();
            double range = tactical.getLongRange();

            for (BattleEntity entity : tactical.getContext().getNearbyEnemies(location, team, range)) {
                if (entity.getEntityType() != BattleEntityType.PLAYER) {
                    Mob mob = (Mob) entity;
                    mob.clearPathfinderGoals();
                    mob.setTarget(location);

                    taskRunner.runTaskLater(() -> {
                        location.getWorld().createExplosion(location, 0);

                        for (Sound sound : tactical.getIgnitionSound()) {
                            sound.play(tactical.getGame(), location);
                        }

                        mob.resetDefaultPathfinderGoals();

                        item.remove();
                        tactical.getDroppedItems().remove(item);
                    }, duration);
                }
            }
        }, tactical.getIgnitionTime());
    }
}
