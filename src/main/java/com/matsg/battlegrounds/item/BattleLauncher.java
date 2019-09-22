package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.Version;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.EventDispatcher;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.mechanism.ReloadSystem;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BattleLauncher extends BattleFirearm implements Launcher {

    private double launchSpeed;
    private LaunchType launchType;
    private Lethal lethal;

    public BattleLauncher(
            ItemMetadata metadata,
            ItemStack itemStack,
            EventDispatcher eventDispatcher,
            Version version,
            LaunchType launchType,
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
            double accuracyAmplifier
    ) {
        super(metadata, itemStack, eventDispatcher, version, FirearmType.LAUNCHER, piercableMaterials, reloadSystem,
                reloadSound, shootSound, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, accuracyAmplifier);
        this.launchSpeed = launchSpeed;
        this.launchType = launchType;
        this.lethal = lethal;
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
        for (BattleEntity entity : context.getNearbyEntities(location, gamePlayer.getTeam(), range)) {
            if (entity == null || entity == gamePlayer || entity.getBukkitEntity().isDead()) {
                continue;
            }

            double damage = lethal.getDamage(Hitbox.TORSO, gamePlayer.getLocation().distance(location));
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

    public void onLeftClick() {
        if (reloading || shooting || ammo.getValue() <= 0 || magazine.getValue() >= magazineSize.getValue()) {
            return;
        }
        reload(getReloadDuration());
    }

    public void onRightClick() {
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
    }

    public void onSwap() { }

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
        launchType.launch(this, getShootingDirection(gamePlayer.getPlayer().getEyeLocation()));
    }
}
