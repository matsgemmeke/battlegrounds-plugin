package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.item.Launcher;
import com.matsg.battlegrounds.api.item.Lethal;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.util.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.Particle.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class BattleLauncher extends BattleFireArm implements Launcher {

    private LaunchType launchType;
    private Lethal lethal;

    public BattleLauncher(String name, String description, ItemStack itemStack, short durability,
                     int magazine, int ammo, int maxAmmo, int cooldown, int reloadDuration, double accuracy,
                     Lethal lethal, LaunchType launchType, ReloadType reloadType, Sound[] reloadSound, Sound[] shootSound) {
        super(name, description, itemStack, durability, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, reloadType, FireArmType.LAUNCHER, reloadSound, shootSound);
        this.launchType = launchType;
        this.lethal = lethal;
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
                "§f" + fireArmType.getName(),
                "§7" + format(6, accuracy * 10.0, 10.0) + " " + EnumMessage.STAT_ACCURACY.getMessage(),
                "§7" + format(6, lethal.getShortDamage(), 50.0) + " " + EnumMessage.STAT_DAMAGE.getMessage(),
                "§7" + format(6, 10.0, 60.0) + " " + EnumMessage.STAT_FIRERATE.getMessage(),
                "§7" + format(6, lethal.getLongRange(), 35.0) + " " + EnumMessage.STAT_RANGE.getMessage() };
    }

    public DamageSource getProjectile() {
        return lethal;
    }

    protected void inflictDamage(Location location, double range) {
        for (GamePlayer gamePlayer : game.getPlayerManager().getNearbyPlayers(location, range)) { //Explosion splash damage range
            if (gamePlayer == null || gamePlayer.getPlayer() == null || gamePlayer.getPlayer().isDead()) {
                continue;
            }
            double damage = lethal.getDamage(null, gamePlayer.getLocation().distance(location) / 2 + 1);
            game.getPlayerManager().damagePlayer(gamePlayer, damage);
        }
    }

    protected void inflictUserDamage(Location location) {
        double playerDistance = gamePlayer.getPlayer().getLocation().distanceSquared(location);
        if (playerDistance <= lethal.getLongRange()) {
            gamePlayer.getPlayer().damage(lethal.getDamage(null, playerDistance) / 5);
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