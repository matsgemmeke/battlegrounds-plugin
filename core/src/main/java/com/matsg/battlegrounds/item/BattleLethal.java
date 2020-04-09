package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.mechanism.IgnitionSystem;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class BattleLethal extends BattleEquipment implements Lethal {

    private double longDamage;
    private double midDamage;
    private double shortDamage;
    private EventDispatcher eventDispatcher;

    public BattleLethal(
            ItemMetadata metadata,
            ItemStack itemStack,
            InternalsProvider internals,
            TaskRunner taskRunner,
            EventDispatcher eventDispatcher,
            IgnitionSystem ignitionSystem,
            Sound[] ignitionSound,
            int amount,
            int maxAmount,
            int cooldown,
            int ignitionTime,
            double longDamage,
            double longRange,
            double midDamage,
            double midRange,
            double shortDamage,
            double shortRange,
            double velocity
    ) {
        super(metadata, itemStack, internals, taskRunner, EquipmentType.LETHAL, ignitionSystem, ignitionSound, amount,
                maxAmount, cooldown, ignitionTime, longRange, midRange, shortRange, velocity);
        this.eventDispatcher = eventDispatcher;
        this.longDamage = longDamage;
        this.midDamage = midDamage;
        this.shortDamage = shortDamage;
    }

    public BattleLethal clone() {
        return (BattleLethal) super.clone();
    }

    public double getLongDamage() {
        return longDamage;
    }

    public void setLongDamage(double longDamage) {
        this.longDamage = longDamage;
    }

    public double getMidDamage() {
        return midDamage;
    }

    public void setMidDamage(double midDamage) {
        this.midDamage = midDamage;
    }

    public double getShortDamage() {
        return shortDamage;
    }

    public void setShortDamage(double shortDamage) {
        this.shortDamage = shortDamage;
    }

    public void explode(Location location) {
        displayCircleEffect(location, 3, "EXPLOSION_LARGE", 1, 6);
        inflictDamage(location);
        inflictUserDamage(location);

        for (Sound sound : ignitionSound) {
            sound.play(game, location);
        }
    }

    public double getDamage(Hitbox hitbox, double distance) {
        return getDistanceDamage(distance);
    }

    private double getDistanceDamage(double distance) {
        if (distance <= shortRange) {
            return shortDamage;
        } else if (distance > shortRange && distance <= midRange) {
            return midDamage;
        } else if (distance > midRange && distance <= longRange) {
            return longDamage;
        }
        return 0.0;
    }

    public void ignite(Item item) {
        explode(item.getLocation());
        droppedItems.remove(item);
        item.remove();
    }

    private void inflictDamage(Location location) {
        for (BattleEntity entity : context.getNearbyEnemies(location, gamePlayer.getTeam(), longRange)) {
            if (entity != null && !entity.getBukkitEntity().isDead()) {
                double damage = getDistanceDamage(entity.getLocation().distance(location));
                int pointsPerKill = 50;

                Event event;

                if (entity.getHealth() - damage <= 0) {
                    event = new GamePlayerKillEntityEvent(game, gamePlayer, entity, this, Hitbox.TORSO, pointsPerKill);
                } else {
                    event = new GamePlayerDamageEntityEvent(game, gamePlayer, entity, this, damage, Hitbox.TORSO);
                }

                eventDispatcher.dispatchExternalEvent(event);
            }
        }
    }

    private void inflictUserDamage(Location location) {
        double playerDistance = gamePlayer.getPlayer().getLocation().distanceSquared(location);
        if (playerDistance <= longRange) {
            double damage = getDistanceDamage(playerDistance);

            gamePlayer.damage(damage);

            if (gamePlayer.getPlayer().isDead()) {
                Event event = new GamePlayerDeathEvent(game, gamePlayer, DeathCause.SUICIDE);

                eventDispatcher.dispatchExternalEvent(event);
            }
        }
    }
}
