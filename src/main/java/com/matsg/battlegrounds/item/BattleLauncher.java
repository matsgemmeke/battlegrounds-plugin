package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent;
import com.matsg.battlegrounds.api.event.GamePlayerDeathEvent.DeathCause;
import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.item.Launcher;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.Particle.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class BattleLauncher extends BattleFireArm implements Launcher {

    private double launchSpeed;
    private LaunchType launchType;
    private Lethal lethal;

    public BattleLauncher(String name, String description, ItemStack itemStack, short durability,
                     int magazine, int ammo, int maxAmmo, double launchSpeed, int cooldown, int reloadDuration, double accuracy,
                     Lethal lethal, LaunchType launchType, ReloadType reloadType, Sound[] reloadSound, Sound[] shootSound) {
        super(name, description, itemStack, durability, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, reloadType, FireArmType.LAUNCHER, reloadSound, shootSound);
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
        displayCircleEffect(location, 2, ParticleEffect.EXPLOSION_LARGE, 2, 6);
        inflictDamage(location, lethal.getLongRange());
        inflictUserDamage(location);

        BattleSound.EXPLOSION.play(game, location);
    }

    protected String[] getLore() {
        return new String[] {
                ChatColor.WHITE + fireArmType.getName(),
                ChatColor.GRAY + format(6, accuracy * 10.0, 10.0) + " " + EnumMessage.STAT_ACCURACY.getMessage(),
                ChatColor.GRAY + format(6, lethal.getShortDamage(), 50.0) + " " + EnumMessage.STAT_DAMAGE.getMessage(),
                ChatColor.GRAY + format(6, Math.max((15 - cooldown / 2) * 10.0, 40.0), 200.0) + " " + EnumMessage.STAT_FIRERATE.getMessage(),
                ChatColor.GRAY + format(6, lethal.getLongRange(), 35.0) + " " + EnumMessage.STAT_RANGE.getMessage() };
    }

    public DamageSource getProjectile() {
        return lethal;
    }

    private void inflictDamage(Location location, double range) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getNearbyPlayers(location, range)) { //Explosion splash damage range
            if (gamePlayer == null || gamePlayer == this.gamePlayer || gamePlayer.getPlayer() == null || gamePlayer.getPlayer().isDead()) {
                continue;
            }
            double damage = lethal.getDamage(Hitbox.TORSO, gamePlayer.getLocation().distance(location));
            game.getPlayerManager().damagePlayer(gamePlayer, damage);
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getEventManager().callEvent(new GamePlayerKillPlayerEvent(game, gamePlayer, this.gamePlayer, this, Hitbox.TORSO));
            }
        }
    }

    private void inflictUserDamage(Location location) {
        double playerDistance = gamePlayer.getPlayer().getLocation().distanceSquared(location);
        if (playerDistance <= lethal.getLongRange()) {
            game.getPlayerManager().damagePlayer(gamePlayer, lethal.getDamage(Hitbox.TORSO, playerDistance) / 5);
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getEventManager().callEvent(new GamePlayerDeathEvent(game, gamePlayer, DeathCause.SUICIDE));
            }
        }
    }

    public void onLeftClick() {
        if (reloading || shooting || ammo <= 0 || magazine >= magazineSize) {
            return;
        }
        reload(reloadDuration);
    }

    public void onRightClick() {
        if (reloading || shooting) {
            return;
        }
        if (magazine <= 0) {
            if (ammo > 0) {
                reload(reloadDuration); //Call a reload event if the magazine is empty
            }
            return;
        }
        shoot();
    }

    public void shoot() {
        magazine --;
        shooting = true;

        for (Sound sound : shootSound) {
            sound.play(game, gamePlayer.getPlayer().getLocation());
        }

        cooldown(cooldown);
        shootProjectile();
        update();
    }

    public void shootProjectile() {
        launchType.launch(this, getShootingDirection(gamePlayer.getPlayer().getEyeLocation()));
    }
}