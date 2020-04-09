package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class TriggerIgnition implements IgnitionSystem {

    private Equipment equipment;
    private TaskRunner taskRunner;

    public TriggerIgnition(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public Equipment getWeapon() {
        return equipment;
    }

    public void setWeapon(Equipment equipment) {
        this.equipment = equipment;
    }

    public void igniteItem(Item item) {
        taskRunner.runTaskTimer(new BukkitRunnable() {
            Game game = equipment.getGame();
            GamePlayer gamePlayer = equipment.getGamePlayer();

            public void run() {
                if (game == null || gamePlayer == null || !equipment.getDroppedItems().contains(item)) {
                    return;
                }
                if (equipment.getContext().getNearbyEnemies(item.getLocation(), gamePlayer.getTeam(), equipment.getLongRange()).length >= 1) {
                    for (Sound sound : equipment.getIgnitionSound()) {
                        sound.play(game, item.getLocation());
                    }
                    equipment.ignite(item);
                    equipment.getDroppedItems().remove(item);
                    item.remove();
                    cancel();
                }
            }
        }, 0, 20);
    }
}
