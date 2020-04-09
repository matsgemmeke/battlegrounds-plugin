package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.item.Equipment;
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
            public void run() {
                // Check if the equipment was not swapped by the player
                if (equipment.getGame() == null || equipment.getGamePlayer() == null) {
                    equipment.getDroppedItems().remove(item);
                    item.remove();
                    return;
                }

                equipment.ignite(item);
            }
        }, equipment.getIgnitionTime());
    }
}
