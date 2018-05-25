package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.item.Attachment;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.util.Hitbox;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleSound;
import com.matsg.battlegrounds.util.EnumMessage;
import com.matsg.battlegrounds.util.HalfBlocks;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleGun extends BattleFireArm implements Gun {

    private boolean scoped;
    private Bullet bullet;
    private FireMode fireMode;
    private int burstRounds, fireRate, hits, scopeZoom;
    private List<Attachment> attachments;

    public BattleGun(String name, String description, ItemStack itemStack, short durability,
                     int magazine, int ammo, int maxAmmo, int fireRate, int burstRounds, int cooldown, int reloadDuration, double accuracy,
                     Bullet bullet, FireMode fireMode, FireArmType fireArmType, ReloadType reloadType, Sound[] reloadSound, Sound[] shootSound) {
        super(name, description, itemStack, durability, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, reloadType, fireArmType, reloadSound, shootSound);
        this.attachments = new ArrayList<>();
        this.bullet = bullet;
        this.burstRounds = burstRounds;
        this.fireMode = fireMode;
        this.fireRate = fireRate;
        this.scopeZoom = 10;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public int getBurstRounds() {
        return 0;
    }

    public int getFireRate() {
        return 0;
    }

    public DamageSource getProjectile() {
        return bullet;
    }

    protected String[] getLore() {
        return new String[] {
                ChatColor.WHITE + fireArmType.getName(),
                ChatColor.GRAY + format(6, accuracy * 100.0, 100.0) + " " + EnumMessage.STAT_ACCURACY.getMessage(),
                ChatColor.GRAY + format(6, bullet.getShortDamage(), 50.0) + " " + EnumMessage.STAT_DAMAGE.getMessage(),
                ChatColor.GRAY + format(6, (burstRounds == 0 ? fireRate : burstRounds) * 10, 60.0) + " " + EnumMessage.STAT_FIRERATE.getMessage(),
                ChatColor.GRAY + format(6, bullet.getMidRange(), 70.0) + " " + EnumMessage.STAT_RANGE.getMessage() };
    }

    private List<Location> getSpreadDirections(Location direction, int amount) {
        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }
        List<Location> list = new ArrayList<>();
        float spread = (float) 0.5;

        Location bottom = direction.clone(), left = direction.clone(), right = direction.clone(), top = direction.clone();
        bottom.setPitch(bottom.getPitch() - spread);
        left.setYaw(left.getYaw() - spread);
        right.setYaw(right.getYaw() + spread);
        top.setPitch(top.getPitch() + spread);

        list.add(bottom);
        list.add(left);
        list.add(right);
        list.add(top);

        //TODO it may be a good idea to actually get this working soon
//        double spreadDensity = 25.0; // Spread density constant. Higher values mean more density
//        for (double i = 0; i < amount; i ++) {
//            double degrees = i / amount * 360;
//            double s = degrees % 180 == 0 ? 0 : degrees - 180;
//
//            float pitch = (float) (direction.getPitch() + (degrees - 180) * spreadDensity);
//            float yaw = (float) (direction.getYaw() + m / spreadDensity);
//
//            Location spread = direction.clone();
//            spread.setPitch(pitch);
//            spread.setYaw(yaw);
//
//            list.add(spread);
//        }
        return list;
    }

    private void inflictDamage(Location location, double range) {
        GamePlayer[] players = game.getPlayerManager().getNearbyPlayers(game, location, range);
        if (players.length > 0) {
            GamePlayer gamePlayer = players[0];
            if (gamePlayer == null || gamePlayer == this.gamePlayer || gamePlayer.getPlayer().isDead()) {
                return;
            }
            Hitbox hitbox = Hitbox.getHitbox(gamePlayer.getLocation().getY(), location.getY());
            gamePlayer.getPlayer().damage(0.01); // Create a fake damage animation
            game.getPlayerManager().damagePlayer(gamePlayer, bullet.getDamage(hitbox, gamePlayer.getLocation().distance(this.gamePlayer.getLocation())) / 5);
            hits ++;
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getEventManager().callEvent(new GamePlayerKillPlayerEvent(game, gamePlayer, this.gamePlayer, this));
            }
        }
    }

    public void onLeftClick() {
        if (fireArmType.hasScope() && scoped) {
            setScoped(false);
            return;
        }
        if (reloading || shooting || ammo <= 0 || magazine >= magazineSize) {
            return;
        }
        reload(reloadDuration);
    }

    public void onRightClick() {
        if (reloading || shooting) {
            return;
        }
        if (fireArmType.hasScope() && !scoped) {
            setScoped(true);
            return;
        }
        if (magazine <= 0) {
            if (ammo > 0) {
                reload(reloadDuration); // Reload if the magazine is empty
            }
            return;
        }
        shoot();
    }

    public void setScoped(boolean scoped) {
        this.scoped = scoped;

        Player player = getGamePlayer().getPlayer();

        if (scoped) {
            for (Sound sound : BattleSound.GUN_SCOPE) {
                sound.play(game, player.getLocation());
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000, -scopeZoom)); //Zoom effect
            player.getInventory().setHelmet(new ItemStack(Material.PUMPKIN, 1));
        } else {
            BattleSound.GUN_SCOPE[0].play(game, player.getLocation(), (float) 0.75);
            BattleSound.GUN_SCOPE[1].play(game, player.getLocation(), (float) 1.5);

            player.getInventory().setHelmet(null);
            player.removePotionEffect(PotionEffectType.SPEED); //Restore zoom effect
        }
    }

    public void shoot() {
        shooting = true;
        fireMode.shoot(this, fireRate, burstRounds);
    }

    public void shootProjectile() {
        shootProjectile(getShootingDirection(gamePlayer.getPlayer().getEyeLocation().subtract(0, 0.25, 0)), fireArmType.getProjectileAmount());
    }

    private void shootProjectile(Location direction) {
        double distance = 0.5, range = 0.1; // Multiplier and range constant

        do {
            Vector vector = direction.getDirection();
            direction.add(vector.multiply(distance));

            displayParticle(direction, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
            inflictDamage(direction, range);

            Block block = direction.getBlock();

            if (!blocks.contains(block.getType()) || !HalfBlocks.isAir(direction)) {
                block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                return;
            }

            direction.subtract(vector); // Restore the location
            distance += 0.5;
        } while (distance < bullet.getLongRange() && hits < fireArmType.getMaxHits()); // If the projectile distance exceeds the long range, stop the loop

        hits = 0;
    }

    private void shootProjectile(Location direction, int amount) {
        if (amount <= 0) {
            return;
        }
        for (Location spreadDirection : getSpreadDirections(direction, amount - 1)) {
            shootProjectile(spreadDirection);
        }
        shootProjectile(direction);
    }
}