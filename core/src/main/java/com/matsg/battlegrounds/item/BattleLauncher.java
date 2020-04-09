package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.mechanism.LaunchSystem;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BattleLauncher extends BattleFirearm implements Launcher {

    private double damageAmplifier;
    private double launchSpeed;
    private LaunchSystem launchSystem;
    private Lethal lethal;

    public BattleLauncher(
            ItemMetadata metadata,
            ItemStack itemStack,
            EventDispatcher eventDispatcher,
            InternalsProvider internals,
            TaskRunner taskRunner,
            LaunchSystem launchSystem,
            Lethal lethal,
            List<Material> piercableMaterials,
            ReloadSystem reloadSystem,
            Sound[] reloadSound,
            Sound[] shootSound,
            int magazine,
            int ammo,
            int maxAmmo,
            double launchSpeed,
            int cooldown,
            int reloadDuration,
            double accuracy,
            double accuracyAmplifier,
            double damageAmplifier
    ) {
        super(metadata, itemStack, internals, taskRunner, eventDispatcher, FirearmType.LAUNCHER, piercableMaterials, reloadSystem,
                reloadSound, shootSound, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, accuracyAmplifier);
        this.damageAmplifier = damageAmplifier;
        this.launchSpeed = launchSpeed;
        this.launchSystem = launchSystem;
        this.lethal = lethal;
    }

    public Launcher clone() {
        BattleLauncher launcher = (BattleLauncher) super.clone();
        launcher.launchSystem.setWeapon(launcher);
        return launcher;
    }

    public double getLaunchSpeed() {
        return launchSpeed;
    }

    public Lethal getLethal() {
        return lethal;
    }

    public void explode(Location location) {
        displayCircleEffect(location, 2, "EXPLOSION_LARGE", 2, 6);
        inflictDamage(location, lethal.getLongRange());
        inflictUserDamage(location);

        BattleSound.EXPLOSION.play(game, location);
    }

    public DamageSource getProjectile() {
        return lethal;
    }

    private void inflictDamage(Location location, double range) {
        for (BattleEntity entity : context.getNearbyEnemies(location, gamePlayer.getTeam(), range)) {
            if (entity == null || entity == gamePlayer || entity.getBukkitEntity().isDead()) {
                continue;
            }

            double damage = lethal.getDamage(Hitbox.TORSO, gamePlayer.getLocation().distance(location)) / damageAmplifier;
            int pointsPerKill = 50;

            Event event;
            Hitbox hitbox = Hitbox.TORSO;

            if (gamePlayer.getHealth() - damage <= 0) {
                event = new GamePlayerKillEntityEvent(game, gamePlayer, entity, this, hitbox, pointsPerKill);
            } else {
                event = new GamePlayerDamageEntityEvent(game, gamePlayer, entity, this, damage, hitbox);
            }

            eventDispatcher.dispatchExternalEvent(event);
        }
    }

    private void inflictUserDamage(Location location) {
        double playerDistance = gamePlayer.getPlayer().getLocation().distanceSquared(location);

        if (playerDistance <= lethal.getLongRange()) {
            double damage = lethal.getDamage(Hitbox.TORSO, playerDistance) / 5;

            gamePlayer.damage(damage);

            if (gamePlayer.getPlayer().isDead()) {
                Event event = new GamePlayerDeathEvent(game, gamePlayer, DeathCause.SUICIDE);

                eventDispatcher.dispatchExternalEvent(event);
            }
        }
    }

    public boolean isInUse() {
        return shooting || reloading;
    }

    public void onLeftClick(PlayerInteractEvent event) {
        if (reloading || shooting || ammo.getValue() <= 0 || magazine.getValue() >= magazineSize.getValue()) {
            return;
        }
        reload(getReloadDuration());
        event.setCancelled(true);
    }

    public void onRightClick(PlayerInteractEvent event) {
        if (reloading || shooting) {
            return;
        }
        if (magazine.getValue() <= 0) {
            if (ammo.getValue() > 0) {
                reload(reloadDuration.getValue()); //Call a reload event if the magazine is empty
            }
            return;
        }
        shoot();
        event.setCancelled(true);
    }

    public void shoot() {
        shooting = true;
        setMagazine(magazine.getValue() - 1);

        for (Sound sound : shotSound) {
            sound.play(game, gamePlayer.getPlayer().getLocation());
        }

        cooldown(getCooldown());
        shootProjectile();
        update();
    }

    public void shootProjectile() {
        launchSystem.launch(getShootingDirection(gamePlayer.getPlayer().getEyeLocation()));
    }
}
