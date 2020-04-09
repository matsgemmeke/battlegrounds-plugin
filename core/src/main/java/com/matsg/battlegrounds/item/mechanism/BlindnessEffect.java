package com.matsg.battlegrounds.item.mechanism;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.BattleEntityType;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Mob;
import com.matsg.battlegrounds.api.item.Tactical;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlindnessEffect implements TacticalEffect {

    private int duration;
    private Tactical tactical;
    private TaskRunner taskRunner;

    public BlindnessEffect(TaskRunner taskRunner, int duration) {
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
        double range = tactical.getLongRange();
        Location location = item.getLocation();
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, duration, 1);

        for (Sound sound : tactical.getIgnitionSound()) {
            sound.play(tactical.getGame(), item.getLocation());
        }

        location.getWorld().createExplosion(item.getLocation(), 0);
        item.remove();
        tactical.getDroppedItems().remove(item);

        for (BattleEntity entity : tactical.getContext().getNearbyEntities(location, range)) {
            if (entity.getEntityType() == BattleEntityType.PLAYER) {
                GamePlayer gamePlayer = (GamePlayer) entity;
                gamePlayer.getPlayer().addPotionEffect(potionEffect);
            } else {
                Mob mob = (Mob) entity;
                double originalSpeed = mob.getMovementSpeed();

                if (originalSpeed <= 0.1) {
                    return;
                }

                mob.setMovementSpeed(0.1);

                taskRunner.runTaskLater(() -> mob.setMovementSpeed(originalSpeed), duration);
            }
        }
    }
}
