package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class BattleLethal extends BattleEquipment implements Lethal {

    private double longDamage, midDamage, shortDamage;

    public BattleLethal(
            Battlegrounds plugin,
            String id,
            String name,
            String description,
            ItemStack itemStack,
            int amount,
            int cooldown,
            double longDamage,
            double longRange,
            double midDamage,
            double midRange,
            double shortDamage,
            double shortRange,
            double velocity,
            int ignitionTime,
            Sound[] ignitionSound
    ) {
        super(plugin, id, name, description, itemStack, EquipmentType.LETHAL, amount, cooldown, longRange, midRange, shortRange, velocity,
                ignitionTime == 0 ? IgnitionType.PASSIVE : IgnitionType.AGGRESSIVE, ignitionTime, ignitionSound);
        this.longDamage = longDamage;
        this.midDamage = midDamage;
        this.shortDamage = shortDamage;
    }

    public Lethal clone() {
        return (Lethal) super.clone();
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
    }

    private void inflictDamage(Location location) {
        for (BattleEntity entity : context.getNearbyEntities(location, gamePlayer.getTeam(), longRange)) {
            if (entity != null && !entity.getBukkitEntity().isDead()) {
                double damage = getDistanceDamage(gamePlayer.getLocation().distance(location) / 5);
                int pointsPerKill = 50;

                Event event;

                if (entity.getHealth() - damage <= 0) {
                    event = new GamePlayerKillEntityEvent(game, gamePlayer, entity, this, Hitbox.TORSO, pointsPerKill);
                } else {
                    event = new GamePlayerDamageEntityEvent(game, gamePlayer, entity, this, damage, Hitbox.TORSO);
                }

                // Handle the event on the plugin manager so other plugins can listen to this event as well
                plugin.getServer().getPluginManager().callEvent(event);
                // Handle the event on the event dispatcher so we can reuse the event without calling a listener to it
                plugin.getEventDispatcher().dispatchEvent(event);
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
                // Handle the event on the event dispatcher so we can reuse the event without calling a listener to it
                plugin.getEventDispatcher().dispatchEvent(event);
                // Handle the event on the plugin manager so other plugin can listen to this event as well
                plugin.getServer().getPluginManager().callEvent(event);
            }
        }
    }
}
