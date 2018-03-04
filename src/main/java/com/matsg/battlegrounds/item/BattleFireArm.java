package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import com.matsg.battlegrounds.util.Particle;
import com.matsg.battlegrounds.util.Particle.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BattleFireArm extends BattleWeapon implements FireArm {

    protected boolean reloadCancelled, reloading, shooting;
    protected double accuracy;
    protected FireArmType fireArmType;
    protected int ammo, cooldown, magazine, magazineSize, maxAmmo, reloadDuration;
    protected List<Material> blocks;
    protected ReloadType reloadType;
    protected Sound[] reloadSound, shootSound;

    public BattleFireArm(String name, String description, ItemStack itemStack, short durability,
                         int magazine, int ammo, int maxAmmo, int cooldown, int reloadDuration, double accuracy,
                         ReloadType reloadType, FireArmType fireArmType, Sound[] reloadSound, Sound[] shootSound) {
        super(name, description, itemStack, durability);
        this.accuracy = accuracy;
        this.ammo = ammo;
        this.blocks = new ArrayList<>();
        this.cooldown = cooldown;
        this.fireArmType = fireArmType;
        this.magazine = magazine;
        this.magazineSize = magazine;
        this.maxAmmo = maxAmmo;
        this.reloadCancelled = false;
        this.reloadDuration = reloadDuration;
        this.reloading = false;
        this.reloadSound = reloadSound;
        this.reloadType = reloadType;
        this.shooting = false;
        this.shootSound = shootSound;

        for (String block : plugin.getBattlegroundsConfig().pierceableBlocks) {
            blocks.add(Material.valueOf(block));
        }
    }

    public FireArm clone() {
        return (FireArm) super.clone();
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getMagazine() {
        return magazine;
    }

    public int getMagazineSize() {
        return magazineSize;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getReloadDuration() {
        return reloadDuration;
    }

    public Sound[] getReloadSound() {
        return reloadSound;
    }

    public Sound[] getShootSound() {
        return shootSound;
    }

    public FireArmType getType() {
        return fireArmType;
    }

    public boolean isReloading() {
        return reloading;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setMagazine(int magazine) {
        this.magazine = magazine;
    }

    public void setReloadCancelled(boolean reloadCancelled) {
        this.reloadCancelled = reloadCancelled;
    }

    public void setReloadDuration(int reloadDuration) {
        this.reloadDuration = reloadDuration;
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public void cooldown(int time) {
        new BattleRunnable() {
            public void run() {
                shooting = false;
            }
        }.runTaskLater(time);
    }

    protected void displayParticle(Location location, float red, float green, float blue) {
        Particle particle = new Particle(ParticleEffect.REDSTONE, 0, location, 0, 0, 0, 1);
        particle.setOffSetX(red);
        particle.setOffSetY(green);
        particle.setOffSetZ(blue);
        particle.display();
    }

    protected abstract String[] getLore();

    protected Location getShootingDirection(Location targetDirection) {
        Player player = gamePlayer.getPlayer();
        Random random = new Random();

        double accuracyAmplifier = plugin.getBattlegroundsConfig().firearmAccuracy;
        if (gamePlayer.getPlayer().isSneaking()) { // Shoot more accurate when the player is crouching
            accuracyAmplifier /= 5.0;
        }
        if (gamePlayer.getPlayer().isSprinting()) { // Shoot less accurate when the player is sprinting
            accuracyAmplifier /= 0.5;
        }

        double accuracy = (1 - this.accuracy) * accuracyAmplifier;
        int offset = (int) (Math.round(accuracy / 2) * 2) + 1;

        double pitch = (player.getLocation().getPitch() + 90.0 + (random.nextInt(offset) - offset / 2)) * Math.PI / 180.0;
        double yaw = (player.getLocation().getYaw() + 90.0 + (random.nextInt(offset) - offset / 2)) * Math.PI / 180.0;
        double x = Math.sin(pitch) * Math.cos(yaw), y = Math.sin(pitch) * Math.sin(yaw), z = Math.cos(pitch);

        Location bulletDirection = targetDirection.clone();
        bulletDirection.setDirection(new Vector(x, z, y)); // Adds the recoil effect to the gun
        return bulletDirection;
    }

    public void onSwitch() {
        if (reloading) {
            gamePlayer.getPlayer().setFoodLevel(20);
            reloadCancelled = true;
        }
    }

    public void playReloadSound() {
        for (Sound sound : reloadSound) {
            if (sound == null) {
                continue;
            }
            if (!sound.isCancelled()) {
                long delay = sound.getDelay();
                sound.setDelay(delay / reloadDuration);
                sound.play(getGame(), getGamePlayer().getPlayer());
                sound.setCancelled(false);
                sound.setDelay(delay);
            }
            sound.setCancelled(false);
        }
    }

    public void refillAmmo() {
        ammo = maxAmmo;
    }

    public void reload() {
        reload(reloadDuration);
    }

    protected void reload(int reloadTime) {
        reloadCancelled = false;
        reloading = true;

        playReloadSound();

        gamePlayer.getPlayer().setFoodLevel(6); //Force the player to slow down

        long runnableSpeed = (long) (reloadTime / reloadDuration);

        new BattleRunnable() {
            public void run() {
                if (reloadCancelled) {
                    cancel();
                    reloadCancelled = false;
                    reloading = false;
                    setSoundCancelled(true, reloadSound);
                    return;
                }
                reloadType.reloadSingle(BattleFireArm.this, reloadDuration);
                if (ammo <= 0 || magazine >= magazineSize) {
                    cancel();
                    getGamePlayer().getPlayer().setFoodLevel(20);
                    reloading = false;
                    update();
                }
            }
        }.runTaskTimer(runnableSpeed, runnableSpeed);
    }

    private void setSoundCancelled(boolean cancelled, Sound... sounds) {
        for (Sound sound : sounds) {
            sound.setCancelled(cancelled);
        }
    }

    public boolean update() {
        Placeholder[] placeholders = new Placeholder[] {
                new Placeholder("bg_ammo", ammo),
                new Placeholder("bg_magazine", magazine),
                new Placeholder("bg_weapon", name)
        };
        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', Placeholder.replace(plugin.getBattlegroundsConfig().getWeaponDisplayName("firearm"), placeholders)))
                .setDurability(durability)
                .setLore(getLore())
                .setUnbreakable(true)
                .build();
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().setItem(itemSlot.getSlot(), itemStack);
        }
        return gamePlayer != null;
    }
}