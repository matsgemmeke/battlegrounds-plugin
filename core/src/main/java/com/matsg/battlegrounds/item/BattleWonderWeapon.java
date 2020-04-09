package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.item.Firearm;
import com.matsg.battlegrounds.api.item.ItemMetadata;
import com.matsg.battlegrounds.api.item.WonderWeapon;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.mechanism.FireMode;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;
import com.matsg.battlegrounds.util.HalfBlocks;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class BattleWonderWeapon extends BattleFirearm implements WonderWeapon {

    private Bullet bullet;
    private double damageAmplifier;
    private FireMode fireMode;

    public BattleWonderWeapon(
            ItemMetadata metadata,
            ItemStack itemStack,
            InternalsProvider internals,
            TaskRunner taskRunner,
            EventDispatcher eventDispatcher,
            Bullet bullet,
            FirearmType firearmType,
            FireMode fireMode,
            List<Material> pierceableMaterials,
            ReloadSystem reloadSystem,
            Sound[] reloadSound,
            Sound[] shotSound,
            int magazine,
            int ammo,
            int maxAmmo,
            int cooldown,
            int reloadDuration,
            double accuracy,
            double accuracyAmplifier,
            double damageAmplifier
    ) {
        super(metadata, itemStack, internals, taskRunner, eventDispatcher, firearmType, pierceableMaterials, reloadSystem, reloadSound,
                shotSound, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, accuracyAmplifier);
        this.bullet = bullet;
        this.fireMode = fireMode;
        this.damageAmplifier = damageAmplifier;
    }

    public DamageSource getProjectile() {
        return bullet;
    }

    public WonderWeapon clone() {
        BattleWonderWeapon wonderWeapon = (BattleWonderWeapon) super.clone();
        wonderWeapon.fireMode = fireMode.clone();
        wonderWeapon.fireMode.setWeapon(wonderWeapon);
        return wonderWeapon;
    }

    public boolean isInUse() {
        return shooting || reloading;
    }

    public void onLeftClick(PlayerInteractEvent event) {
        if (reloading || shooting || ammo.getValue() <= 0 || magazine.getValue() >= magazineSize.getValue()) {
            return;
        }
        reload();
        event.setCancelled(true);
    }

    public void onRightClick(PlayerInteractEvent event) {
        if (reloading || shooting) {
            return;
        }
        if (magazine.getValue() <= 0) {
            if (ammo.getValue() > 0) {
                reload(reloadDuration.getValue()); // Reload if the magazine is empty
            }
            return;
        }
        shoot();
        event.setCancelled(true);
    }

    public void shoot() {
        shooting = true;
        fireMode.shoot();
    }

    public void shootProjectile() {
        shootProjectile(getShootingDirection(gamePlayer.getPlayer().getEyeLocation().subtract(0, 0.25, 0)));
    }

    private void inflictDamage(BattleEntity[] entities, Location location) {
        for (BattleEntity entity : entities) {
            entity.damage(0.01);

            Location entityLocation = entity.getLocation();
            Hitbox hitbox = Hitbox.getHitbox(entityLocation.getY(), location.getY());

            double damage = bullet.getDamage(hitbox, entityLocation.distance(gamePlayer.getLocation())) / damageAmplifier;

            if (context.hasBloodEffectDisplay(entity.getEntityType())) {
                location.getWorld().playEffect(location, Effect.STEP_SOUND, Material.SLIME_BLOCK);
            }

            Event event;

            if (entity.getHealth() - damage <= 0) {
                event = new GamePlayerKillEntityEvent(game, gamePlayer, entity, this, hitbox, hitbox.getPoints());
            } else {
                event = new GamePlayerDamageEntityEvent(game, gamePlayer, entity, this, damage, hitbox);
            }

            eventDispatcher.dispatchExternalEvent(event);
        }
    }

    private void shootProjectile(Location direction) {
        long delay = 0;
        long period = 1;

        taskRunner.runTaskTimer(new BukkitRunnable() {
            double distance = 0.5;
            double maxDistance = 50.0;
            double range = 0.15;
            int i = 0;
            int speed = 8;

            public void run() {
                do {
                    Vector vector = direction.getDirection();
                    vector.multiply(distance);
                    direction.add(vector);

                    displayParticle(direction, Float.MIN_VALUE, Float.MAX_VALUE, Float.MIN_VALUE);

                    BattleEntity[] entities = context.getNearbyEnemies(direction, gamePlayer.getTeam(), range);

                    if (!pierableMaterials.contains(direction.getBlock().getType()) && !HalfBlocks.isAir(direction)) {
                        direction.getWorld().playEffect(direction, Effect.STEP_SOUND, Material.SLIME_BLOCK);
                        cancel();
                        return;
                    }
                    if (entities.length >= 1) {
                        direction.getWorld().playEffect(direction, Effect.STEP_SOUND, Material.SLIME_BLOCK);
                        inflictDamage(entities, direction);
                        cancel();
                        return;
                    }

                    direction.subtract(vector);
                    distance += 1.0;
                } while (distance <= maxDistance && ++ i <= speed);

                if (distance > maxDistance) {
                    cancel(); // If the projectile distance exceeds the long range, stop the runnable
                }
            }
        }, delay, period);
    }
}
