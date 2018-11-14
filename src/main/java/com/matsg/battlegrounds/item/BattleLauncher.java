package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.TranslationKey;
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
import com.matsg.battlegrounds.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class BattleLauncher extends BattleFireArm implements Launcher {

    private double launchSpeed;
    private LaunchType launchType;
    private Lethal lethal;

    public BattleLauncher(String id, String name, String description, ItemStack itemStack, short durability,
                     int magazine, int ammo, int maxAmmo, double launchSpeed, int cooldown, int reloadDuration, double accuracy,
                     Lethal lethal, LaunchType launchType, ReloadType reloadType, Sound[] reloadSound, Sound[] shootSound) {
        super(id, name, description, itemStack, durability, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, reloadType, FireArmType.LAUNCHER, reloadSound, shootSound);
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
                ChatColor.WHITE + fireArmType.getName(),
                ChatColor.GRAY + format(6, getAccuracy() * 100.0, 100.0) + " " + Message.create(TranslationKey.STAT_ACCURACY),
                ChatColor.GRAY + format(6, lethal.getShortDamage(), 50.0) + " " + Message.create(TranslationKey.STAT_DAMAGE),
                ChatColor.GRAY + format(6, Math.max((15 - getCooldown() / 2) * 10.0, 40.0), 200.0) + " " + Message.create(TranslationKey.STAT_FIRERATE),
                ChatColor.GRAY + format(6, lethal.getLongRange(), 35.0) + " " + Message.create(TranslationKey.STAT_RANGE) };
    }

    public DamageSource getProjectile() {
        return lethal;
    }

    private void inflictDamage(Location location, double range) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getNearbyPlayers(location, range)) { //Explosion splash damage range
            if (gamePlayer == null || gamePlayer == this.gamePlayer || gamePlayer.getPlayer().isDead() || gamePlayer.getTeam() == this.gamePlayer.getTeam()) {
                continue;
            }
            double damage = lethal.getDamage(Hitbox.TORSO, gamePlayer.getLocation().distance(location));
            game.getPlayerManager().damagePlayer(gamePlayer, damage);
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getServer().getPluginManager().callEvent(new GamePlayerKillPlayerEvent(game, gamePlayer, this.gamePlayer, this, Hitbox.TORSO));
                game.getGameMode().onKill(gamePlayer, this.gamePlayer, this, Hitbox.TORSO);
            }
        }
    }

    private void inflictUserDamage(Location location) {
        double playerDistance = gamePlayer.getPlayer().getLocation().distanceSquared(location);
        if (playerDistance <= lethal.getLongRange()) {
            game.getPlayerManager().damagePlayer(gamePlayer, lethal.getDamage(Hitbox.TORSO, playerDistance) / 5);
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getServer().getPluginManager().callEvent(new GamePlayerDeathEvent(game, gamePlayer, DeathCause.SUICIDE));
                game.getGameMode().onDeath(gamePlayer, DeathCause.SUICIDE);
            }
        }
    }

    public boolean onDrop() {
        return true;
    }

    public void onLeftClick() {
        if (reloading || shooting || ammo.getAttributeValue().getValue() <= 0 || magazine.getAttributeValue().getValue() >= magazineSize.getAttributeValue().getValue()) {
            return;
        }
        reload(getReloadDuration());
    }

    public void onRightClick() {
        if (reloading || shooting) {
            return;
        }
        if (magazine.getAttributeValue().getValue() <= 0) {
            if (ammo.getAttributeValue().getValue() > 0) {
                reload(reloadDuration.getAttributeValue().getValue()); //Call a reload event if the magazine is empty
            }
            return;
        }
        shoot();
    }

    public void onSwap() { }

    public void shoot() {
        shooting = true;
        setMagazine(getMagazine() - 1);

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