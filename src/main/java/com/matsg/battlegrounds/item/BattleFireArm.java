package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.FireArm;
import com.matsg.battlegrounds.api.item.ItemAttribute;
import com.matsg.battlegrounds.api.item.ReloadType;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.attributes.DoubleAttributeValue;
import com.matsg.battlegrounds.item.attributes.IntegerAttributeValue;
import com.matsg.battlegrounds.item.attributes.ReloadTypeAttributeValue;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.ItemStackBuilder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BattleFireArm extends BattleWeapon implements FireArm {

    protected boolean reloadCancelled, reloading, shooting;
    protected FireArmType fireArmType;
    protected ItemAttribute<Double> horizontalAccuracy, verticalAccuracy;
    protected ItemAttribute<Integer> ammo, cooldown, magazine, magazineSize, magazineSupply, maxAmmo, reloadDuration, reloadDurationOg;
    protected ItemAttribute<ReloadType> reloadType;
    protected List<Material> blocks;
    protected Sound[] reloadSound, shotSound;

    public BattleFireArm(String id, String name, String description, ItemStack itemStack, short durability,
                         int magazine, int ammo, int maxAmmo, int cooldown, int reloadDuration, double accuracy,
                         ReloadType reloadType, FireArmType fireArmType, Sound[] reloadSound, Sound[] shotSound) {
        super(id, name, description, itemStack, durability);
        this.blocks = new ArrayList<>();
        this.fireArmType = fireArmType;
        this.reloadCancelled = false;
        this.reloading = false;
        this.reloadSound = reloadSound;
        this.shooting = false;
        this.shotSound = shotSound;

        this.ammo = new BattleItemAttribute<>("ammo-reserve", new IntegerAttributeValue(magazine * ammo));
        this.cooldown = new BattleItemAttribute<>("shot-cooldown", new IntegerAttributeValue(cooldown));
        this.horizontalAccuracy = new BattleItemAttribute<>("accuracy-horizontal", new DoubleAttributeValue(accuracy));
        this.magazine = new BattleItemAttribute<>("ammo-magazine", new IntegerAttributeValue(magazine));
        this.magazineSize = new BattleItemAttribute<>("ammo-magazine-size", new IntegerAttributeValue(magazine));
        this.magazineSupply = new BattleItemAttribute<>("ammo-magazine-supply", new IntegerAttributeValue(ammo));
        this.maxAmmo = new BattleItemAttribute<>("ammo-max", new IntegerAttributeValue(magazine * maxAmmo));
        this.reloadDuration = new BattleItemAttribute<>("reload-duration", new IntegerAttributeValue(reloadDuration));
        this.reloadDurationOg = new BattleItemAttribute<>("reload-duration-og", new IntegerAttributeValue(reloadDuration));
        this.reloadType = new BattleItemAttribute<>("reload-type", new ReloadTypeAttributeValue(reloadType));
        this.verticalAccuracy = new BattleItemAttribute<>("accuracy-vertical", new DoubleAttributeValue(accuracy));

        attributes.add(this.ammo);
        attributes.add(this.cooldown);
        attributes.add(this.horizontalAccuracy);
        attributes.add(this.magazine);
        attributes.add(this.magazineSize);
        attributes.add(this.magazineSupply);
        attributes.add(this.maxAmmo);
        attributes.add(this.reloadDuration);
        attributes.add(this.reloadDurationOg);
        attributes.add(this.reloadType);
        attributes.add(this.verticalAccuracy);

        for (String block : plugin.getBattlegroundsConfig().pierceableBlocks) {
            blocks.add(Material.valueOf(block));
        }
    }

    public FireArm clone() {
        BattleFireArm fireArm = (BattleFireArm) super.clone();
        fireArm.ammo = fireArm.getAttribute("ammo-reserve");
        fireArm.cooldown = fireArm.getAttribute("shot-cooldown");
        fireArm.horizontalAccuracy = fireArm.getAttribute("accuracy-horizontal");
        fireArm.magazine = fireArm.getAttribute("ammo-magazine");
        fireArm.magazineSize = fireArm.getAttribute("ammo-magazine-size");
        fireArm.magazineSupply = fireArm.getAttribute("ammo-magazine-supply");
        fireArm.maxAmmo = fireArm.getAttribute("ammo-max");
        fireArm.reloadDuration = fireArm.getAttribute("reload-duration");
        fireArm.reloadDurationOg = fireArm.getAttribute("reload-duration-og");
        fireArm.reloadType = fireArm.getAttribute("reload-type");
        fireArm.verticalAccuracy = fireArm.getAttribute("accuracy-vertical");
        return fireArm;
    }

    public double getAccuracy() {
        return (horizontalAccuracy.getAttributeValue().getValue() + verticalAccuracy.getAttributeValue().getValue()) / 2;
    }

    public int getAmmo() {
        return ammo.getAttributeValue().getValue();
    }

    public int getCooldown() {
        return cooldown.getAttributeValue().getValue();
    }

    public int getMagazine() {
        return magazine.getAttributeValue().getValue();
    }

    public int getMagazineSize() {
        return magazineSize.getAttributeValue().getValue();
    }

    public int getMaxAmmo() {
        return maxAmmo.getAttributeValue().getValue();
    }

    public int getReloadDuration() {
        return reloadDuration.getAttributeValue().getValue();
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
        this.ammo.getAttributeValue().setValue(ammo);
    }

    public void setMagazine(int magazine) {
        this.magazine.getAttributeValue().setValue(magazine);
    }

    public void setReloadCancelled(boolean reloadCancelled) {
        this.reloadCancelled = reloadCancelled;
    }

    public void setReloadDuration(int reloadDuration) {
        this.reloadDuration.getAttributeValue().setValue(reloadDuration);
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    private void cancelReload() {
        if (!reloading) {
            return;
        }
        gamePlayer.getPlayer().setFoodLevel(20);
        reloadCancelled = true;
    }

    public void cooldown(int time) {
        new BattleRunnable() {
            public void run() {
                shooting = false;
            }
        }.runTaskLater(time);
    }

    protected void displayParticle(Location location, float red, float green, float blue) {
        plugin.getVersion().spawnParticle(location, "REDSTONE", 0, red, green, blue, 0);
    }

    protected abstract String[] getLore();

    private double getReloadSpeed() {
        return (double) reloadDurationOg.getAttributeValue().getValue() / (double) reloadDuration.getAttributeValue().getValue();
    }

    protected Location getShootingDirection(Location targetDirection) {
        Player player = gamePlayer.getPlayer();
        Random random = new Random();

        double accuracyAmplifier = plugin.getBattlegroundsConfig().firearmAccuracy;
        if (gamePlayer.getPlayer().isSneaking()) { // Shoot more accurate when the player is crouching
            accuracyAmplifier /= 3.0;
        }
        if (gamePlayer.getPlayer().isSprinting()) { // Shoot less accurate when the player is sprinting
            accuracyAmplifier /= 0.5;
        }

        double horizontalRecoil = (1 - horizontalAccuracy.getAttributeValue().getValue()) * accuracyAmplifier;
        double verticalRecoil = (1 - verticalAccuracy.getAttributeValue().getValue()) * accuracyAmplifier;
        int horizontalOffset = (int) (Math.round(horizontalRecoil / 2) * 2) + 1;
        int verticalOffset = (int) (Math.round(verticalRecoil / 2) * 2) + 1;

        double pitch = (player.getLocation().getPitch() + 90.0 + (random.nextInt(verticalOffset) - verticalOffset / 2)) * Math.PI / 180.0;
        double yaw = (player.getLocation().getYaw() + 90.0 + (random.nextInt(horizontalOffset) - horizontalOffset / 2)) * Math.PI / 180.0;
        double x = Math.sin(pitch) * Math.cos(yaw), y = Math.sin(pitch) * Math.sin(yaw), z = Math.cos(pitch);

        Location bulletDirection = targetDirection.clone();
        bulletDirection.setDirection(new Vector(x, z, y)); // Adds the recoil effect to the gun
        return bulletDirection;
    }

    public void onSwitch() {
        cancelReload();
    }

    public void playReloadSound() {
        playReloadSound(gamePlayer.getPlayer());
    }

    public void playReloadSound(Entity entity) {
        for (Sound sound : reloadSound) {
            if (sound == null) {
                continue;
            }
            if (!sound.isCancelled()) {
                long delay = sound.getDelay();

                sound.setDelay((long) (delay / getReloadSpeed()));
                sound.play(game, entity);
                sound.setCancelled(false);
                sound.setDelay(delay);
            }
            sound.setCancelled(false);
        }
    }

    public void playShotSound() {
        playShotSound(gamePlayer.getPlayer());
    }

    public void playShotSound(Entity entity) {
        for (Sound sound : shotSound) {
            if (!sound.isCancelled()) {
                sound.play(game, entity);
            }
            sound.setCancelled(false);
        }
    }

    public void reload() {
        reload(reloadDuration.getAttributeValue().getValue());
    }

    protected void reload(int reloadTime) {
        reloadCancelled = false;
        reloading = true;

        playReloadSound();

        gamePlayer.getPlayer().setFoodLevel(6); //Force the player to slow down

        new BattleRunnable() {
            public void run() {
                if (reloadCancelled || gamePlayer == null || !gamePlayer.getStatus().isAlive()) {
                    cancel();
                    reloadCancelled = false;
                    reloading = false;
                    setSoundCancelled(true, reloadSound);
                    return;
                }
                reloadType.getAttributeValue().getValue().reloadSingle(BattleFireArm.this, reloadDuration.getAttributeValue().getValue());
                if (ammo.getAttributeValue().getValue() <= 0 || magazine.getAttributeValue().getValue() >= magazineSize.getAttributeValue().getValue()) {
                    cancel();
                    getGamePlayer().getPlayer().setFoodLevel(20);
                    reloading = false;
                    update();
                }
            }
        }.runTaskTimer(reloadTime, reloadTime);
    }

    public void remove() {
        super.remove();
        cancelReload();
        resetState();
    }

    public void resetState() {
        ammo.getAttributeValue().setValue(magazineSupply.getAttributeValue().getValue() * magazineSize.getAttributeValue().getValue());
        magazine.getAttributeValue().setValue(magazineSize.getAttributeValue().getValue());
    }

    private void setSoundCancelled(boolean cancelled, Sound... sounds) {
        for (Sound sound : sounds) {
            sound.setCancelled(cancelled);
        }
    }

    public boolean update() {
        Placeholder[] placeholders = new Placeholder[] {
                new Placeholder("bg_ammo", ammo.getAttributeValue().getValue()),
                new Placeholder("bg_magazine", magazine.getAttributeValue().getValue()),
                new Placeholder("bg_weapon", name)
        };
        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(1)
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