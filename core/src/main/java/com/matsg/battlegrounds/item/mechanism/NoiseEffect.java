package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.Loadout;
import com.matsg.battlegrounds.api.item.Tactical;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class NoiseEffect implements TacticalEffect {

    private Tactical tactical;
    private TaskRunner taskRunner;

    public NoiseEffect(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;
    }

    public Tactical getWeapon() {
        return tactical;
    }

    public void setWeapon(Tactical tactical) {
        this.tactical = tactical;
    }

    public void applyEffect(Item item) {
        Loadout loadout = tactical.getGamePlayer().getLoadout();
        Gun gun = getDefaultGun(loadout);

        if (gun == null) {
            return;
        }

        long period = 2;
        int maxLoops = 40;

        taskRunner.runTaskTimer(new BukkitRunnable() {
            int loops = 0;

            public void run() {
                loops += period;
                gun.playShotSound(item);

                if (loops > maxLoops) {
                    tactical.getDroppedItems().remove(item);
                    item.remove();
                    cancel();
                }
            }
        }, 0, period);
    }

    private Gun getDefaultGun(Loadout loadout) {
        if (loadout.getPrimary() != null && loadout.getPrimary() instanceof Gun) {
            return (Gun) loadout.getPrimary();
        } else if (loadout.getSecondary() != null && loadout.getSecondary() instanceof Gun) {
            return (Gun) loadout.getSecondary();
        }
        return null;
    }
}
