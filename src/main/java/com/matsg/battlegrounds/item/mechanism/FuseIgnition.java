package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.game.Game;
import com.matsg.battlegrounds.api.item.Equipment;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class FuseIgnition implements IgnitionSystem {

    private Equipment equipment;
    private TaskRunner taskRunner;

    public FuseIgnition(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public Equipment getWeapon() {
        return equipment;
    }

    public void setWeapon(Equipment equipment) {
        this.equipment = equipment;
    }

    public void igniteItem(Item item) {
        taskRunner.runTaskLater(new BukkitRunnable() {
            Game game = equipment.getGame();
            GamePlayer gamePlayer = equipment.getGamePlayer();

            public void run() {
                if (game == null || gamePlayer == null) {
                    return;
                }
                for (Sound sound : equipment.getIgnitionSound()) {
                    sound.play(game, item.getLocation());
                }
                equipment.ignite(item);
                equipment.getDroppedItems().remove(item);
                item.remove();
            }
        }, equipment.getIgnitionTime());
    }
}
