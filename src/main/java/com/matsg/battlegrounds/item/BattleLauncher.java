package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TranslationKey;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.entity.BattleEntity;
import com.matsg.battlegrounds.api.event.GamePlayerDamageEntityEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillEntityEvent;
import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.item.Launcher;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.entity.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class BattleLauncher extends BattleFirearm implements Launcher {

    private double launchSpeed;
    private LaunchType launchType;
    private Lethal lethal;

    public BattleLauncher(
            Battlegrounds plugin,
            String id,
            String name,
            String description,
            ItemStack itemStack,
            int magazine,
            int ammo,
            int maxAmmo,
            double launchSpeed,
            int cooldown,
            int reloadDuration,
            double accuracy,
            Lethal lethal,
            LaunchType launchType,
            ReloadType reloadType,
            Sound[] reloadSound,
            Sound[] shootSound
    ) {
        super(plugin, id, name, description, itemStack, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, reloadType, FirearmType.LAUNCHER, reloadSound, shootSound);
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

    protected String[] getLore() {
        return new String[] {
                ChatColor.WHITE + firearmType.getName(),
                ChatColor.GRAY + format(6, getAccuracy() * 100.0, 100.0) + " " + translator.translate(TranslationKey.STAT_ACCURACY),
                ChatColor.GRAY + format(6, lethal.getShortDamage(), 50.0) + " " + translator.translate(TranslationKey.STAT_DAMAGE),
                ChatColor.GRAY + format(6, Math.max((15 - cooldown.getValue() / 2) * 10.0, 40.0), 200.0) + " " + translator.translate(TranslationKey.STAT_FIRERATE),
                ChatColor.GRAY + format(6, lethal.getLongRange(), 35.0) + " " + translator.translate(TranslationKey.STAT_RANGE)
        };
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

            // Handle the event on the plugin manager so other plugins can listen to this event as well
            plugin.getServer().getPluginManager().callEvent(event);
            // Handle the event on the event dispatcher so we can reuse the event without calling a listener to it
            plugin.getEventDispatcher().dispatchEvent(event);
        }
    }

    private void inflictUserDamage(Location location) {
        double playerDistance = gamePlayer.getPlayer().getLocation().distanceSquared(location);

        if (playerDistance <= lethal.getLongRange()) {
            double damage = lethal.getDamage(Hitbox.TORSO, playerDistance) / 5;

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
