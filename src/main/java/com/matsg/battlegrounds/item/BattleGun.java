package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.event.GamePlayerKillPlayerEvent;
import com.matsg.battlegrounds.api.game.Team;
import com.matsg.battlegrounds.api.item.DamageSource;
import com.matsg.battlegrounds.api.item.Gun;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.player.GamePlayer;
import com.matsg.battlegrounds.api.player.Hitbox;
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
import java.util.Random;

public class BattleGun extends BattleFireArm implements Gun {

    private boolean scoped;
    private Bullet bullet;
    private FireMode fireMode;
    private int burstRounds, fireRate, hits, scopeZoom;

    public BattleGun(String name, String description, ItemStack itemStack, short durability,
                     int magazine, int ammo, int maxAmmo, int fireRate, int burstRounds, int cooldown, int reloadDuration, double accuracy,
                     Bullet bullet, FireMode fireMode, FireArmType fireArmType, ReloadType reloadType, Sound[] reloadSound, Sound[] shootSound) {
        super(name, description, itemStack, durability, magazine, ammo, maxAmmo, cooldown, reloadDuration, accuracy, reloadType, fireArmType, reloadSound, shootSound);
        this.bullet = bullet;
        this.burstRounds = burstRounds;
        this.fireMode = fireMode;
        this.fireRate = fireRate;
        this.scopeZoom = 10;
    }

    public int getBurstRounds() {
        return burstRounds;
    }

    public int getFireRate() {
        return fireRate;
    }

    public DamageSource getProjectile() {
        return bullet;
    }

    protected String[] getLore() {
        return new String[] {
                ChatColor.WHITE + fireArmType.getName(),
                ChatColor.GRAY + format(6, accuracy * 100.0, 100.0) + " " + EnumMessage.STAT_ACCURACY.getMessage(),
                ChatColor.GRAY + format(6, bullet.getShortDamage(), 55.0) + " " + EnumMessage.STAT_DAMAGE.getMessage(),
                ChatColor.GRAY + format(6, Math.max((fireRate + 10 - cooldown / 2) * 10.0, 40.0), 200.0) + " " + EnumMessage.STAT_FIRERATE.getMessage(),
                ChatColor.GRAY + format(6, bullet.getMidRange(), 70.0) + " " + EnumMessage.STAT_RANGE.getMessage() };
    }

    private List<Location> getSpreadDirections(Location direction, int amount) {
        if (amount <= 0) {
            return Collections.EMPTY_LIST;
        }
        List<Location> list = new ArrayList<>();
        Random random = new Random();
        float choke = (float) 3.0;

        for (int i = 1; i <= amount; i ++) {
            Location location = direction.clone();
            location.setPitch(location.getPitch() + random.nextFloat() * choke - choke / 2);
            location.setYaw(location.getYaw() + random.nextFloat() * choke - choke / 2);
            list.add(location);
        }

        return list;
    }

    private void inflictDamage(Location location, double range) {
        GamePlayer[] players = game.getPlayerManager().getNearbyPlayers(location, range);
        Team team = game.getGameMode().getTeam(gamePlayer);
        if (players.length > 0) {
            GamePlayer gamePlayer = players[0];
            if (gamePlayer == null || gamePlayer == this.gamePlayer || gamePlayer.getPlayer().isDead() || team != null && game.getGameMode().getTeam(gamePlayer) == team) {
                return;
            }
            Hitbox hitbox = Hitbox.getHitbox(gamePlayer.getLocation().getY(), location.getY());
            double damage = bullet.getDamage(hitbox, gamePlayer.getLocation().distance(this.gamePlayer.getLocation())) / plugin.getBattlegroundsConfig().gunDamageModifer;
            game.getPlayerManager().damagePlayer(gamePlayer, damage);
            hits ++;
            if (gamePlayer.getPlayer().isDead()) {
                plugin.getServer().getPluginManager().callEvent(new GamePlayerKillPlayerEvent(game, gamePlayer, this.gamePlayer, this, hitbox));
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

    public void resetState() {
        super.resetState();
        setScoped(false);
    }

    public void setScoped(boolean scoped) {
        if (!fireArmType.hasScope() || scoped == this.scoped) {
            return;
        }

        this.scoped = scoped;

        Player player = gamePlayer.getPlayer();

        if (scoped) {
            for (Sound sound : BattleSound.GUN_SCOPE) {
                sound.play(game, player.getLocation());
            }

            cooldown(1);
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

            if (!blocks.contains(block.getType()) && !HalfBlocks.isAir(direction)) {
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