package com.matsg.battlegrounds.item;

import com.matsg.battlegrounds.api.item.*;
import com.matsg.battlegrounds.api.entity.GamePlayer;
import com.matsg.battlegrounds.api.util.GenericAttribute;
import com.matsg.battlegrounds.api.util.Placeholder;
import com.matsg.battlegrounds.api.util.Sound;
import com.matsg.battlegrounds.item.modifier.IntegerAttributeModifier;
import com.matsg.battlegrounds.util.BattleAttribute;
import com.matsg.battlegrounds.util.valueobject.FloatValueObject;
import com.matsg.battlegrounds.util.valueobject.IntegerValueObject;
import com.matsg.battlegrounds.util.valueobject.ReloadTypeValueObject;
import com.matsg.battlegrounds.util.BattleRunnable;
import com.matsg.battlegrounds.util.BattleSound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BattleFirearm extends BattleWeapon implements Firearm {

    protected boolean reloadCancelled, reloading, shooting;
    protected FirearmType firearmType;
    protected GenericAttribute<Float> horizontalAccuracy, verticalAccuracy;
    protected GenericAttribute<Integer> ammo, cooldown, magazine, magazineSize, magazineSupply, maxAmmo, reloadDuration, reloadDurationOg;
    protected GenericAttribute<ReloadType> reloadType;
    protected List<Item> droppedItems;
    protected List<Material> blocks;
    protected Sound[] reloadSound, shotSound;

    public BattleFirearm(String id, String name, String description, ItemStack itemStack,
                         int magazine, int ammo, int maxAmmo, int cooldown, int reloadDuration, double accuracy,
                         ReloadType reloadType, FirearmType firearmType, Sound[] reloadSound, Sound[] shotSound) {
        super(id, name, description, itemStack);
        this.blocks = new ArrayList<>();
        this.droppedItems = new ArrayList<>();
        this.firearmType = firearmType;
        this.reloadCancelled = false;
        this.reloading = false;
        this.reloadSound = reloadSound;
        this.shooting = false;
        this.shotSound = shotSound;

        this.ammo = new BattleAttribute<>("ammo-reserve", new IntegerValueObject(magazine * ammo));
        this.cooldown = new BattleAttribute<>("shot-cooldown", new IntegerValueObject(cooldown));
        this.horizontalAccuracy = new BattleAttribute<>("accuracy-horizontal", new FloatValueObject((float) accuracy));
        this.magazine = new BattleAttribute<>("ammo-magazine", new IntegerValueObject(magazine));
        this.magazineSize = new BattleAttribute<>("ammo-magazine-size", new IntegerValueObject(magazine));
        this.magazineSupply = new BattleAttribute<>("ammo-magazine-supply", new IntegerValueObject(ammo));
        this.maxAmmo = new BattleAttribute<>("ammo-max", new IntegerValueObject(magazine * maxAmmo));
        this.reloadDuration = new BattleAttribute<>("reload-duration", new IntegerValueObject(reloadDuration));
        this.reloadDurationOg = new BattleAttribute<>("reload-duration-og", new IntegerValueObject(reloadDuration));
        this.reloadType = new BattleAttribute<>("reload-type", new ReloadTypeValueObject(reloadType));
        this.verticalAccuracy = new BattleAttribute<>("accuracy-vertical", new FloatValueObject((float) accuracy));

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

    public Firearm clone() {
        BattleFirearm firearm = (BattleFirearm) super.clone();
        firearm.ammo = firearm.getAttribute("ammo-reserve");
        firearm.cooldown = firearm.getAttribute("shot-cooldown");
        firearm.droppedItems = new ArrayList<>();
        firearm.horizontalAccuracy = firearm.getAttribute("accuracy-horizontal");
        firearm.magazine = firearm.getAttribute("ammo-magazine");
        firearm.magazineSize = firearm.getAttribute("ammo-magazine-size");
        firearm.magazineSupply = firearm.getAttribute("ammo-magazine-supply");
        firearm.maxAmmo = firearm.getAttribute("ammo-max");
        firearm.reloadDuration = firearm.getAttribute("reload-duration");
        firearm.reloadDurationOg = firearm.getAttribute("reload-duration-og");
        firearm.reloadType = firearm.getAttribute("reload-type");
        firearm.verticalAccuracy = firearm.getAttribute("accuracy-vertical");
        return firearm;
    }

    public double getAccuracy() {
        return (horizontalAccuracy.getValue() + verticalAccuracy.getValue()) / 2;
    }

    public int getAmmo() {
        return ammo.getValue();
    }

    public void setAmmo(int ammo) {
        this.ammo.applyModifier(new IntegerAttributeModifier(ammo));
    }

    public int getCooldown() {
        return cooldown.getValue();
    }

    public List<Item> getDroppedItems() {
        return droppedItems;
    }

    public int getMagazine() {
        return magazine.getValue();
    }

    public void setMagazine(int magazine) {
        this.magazine.applyModifier(new IntegerAttributeModifier(magazine));
    }

    public int getMagazineSize() {
        return magazineSize.getValue();
    }

    public int getMaxAmmo() {
        return maxAmmo.getValue();
    }

    public void setReloadCancelled(boolean reloadCancelled) {
        this.reloadCancelled = reloadCancelled;
    }

    public int getReloadDuration() {
        return reloadDuration.getValue();
    }

    public void setReloadDuration(int reloadDuration) {
        this.reloadDuration.applyModifier(new IntegerAttributeModifier(reloadDuration));
    }

    public FirearmType getType() {
        return firearmType;
    }

    public boolean isReloading() {
        return reloading;
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public boolean isShooting() {
        return shooting;
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
        plugin.getVersion().spawnColoredParticle(location, "REDSTONE", red, green, blue);
    }

    protected abstract String[] getLore();

    private double getReloadSpeed() {
        return (double) reloadDurationOg.getValue() / (double) reloadDuration.getValue();
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

        double horizontalRecoil = (1 - horizontalAccuracy.getValue()) * accuracyAmplifier;
        double verticalRecoil = (1 - verticalAccuracy.getValue()) * accuracyAmplifier;
        int horizontalOffset = (int) (Math.round(horizontalRecoil / 2) * 2) + 1;
        int verticalOffset = (int) (Math.round(verticalRecoil / 2) * 2) + 1;

        double pitch = (player.getLocation().getPitch() + 90.0 + (random.nextInt(verticalOffset) - verticalOffset / 2)) * Math.PI / 180.0;
        double yaw = (player.getLocation().getYaw() + 90.0 + (random.nextInt(horizontalOffset) - horizontalOffset / 2)) * Math.PI / 180.0;
        double x = Math.sin(pitch) * Math.cos(yaw), y = Math.sin(pitch) * Math.sin(yaw), z = Math.cos(pitch);

        Location bulletDirection = targetDirection.clone();
        bulletDirection.setDirection(new Vector(x, z, y)); // Adds the recoil effect to the gun
        return bulletDirection;
    }

    public boolean isRelated(ItemStack itemStack) {
        for (Item item : droppedItems) {
            if (item.getItemStack().equals(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public boolean onDrop(GamePlayer gamePlayer, Item item) {
        if (this.gamePlayer != gamePlayer || reloading || shooting) {
            return true;
        }
        droppedItems.add(item);
        this.gamePlayer.getLoadout().removeWeapon(this);
        this.gamePlayer = null;
        return false;
    }

    public boolean onPickUp(GamePlayer gamePlayer, Item item) {
        ItemSlot itemSlot = ItemSlot.fromSlot(gamePlayer.getPlayer().getInventory().getHeldItemSlot());
        Weapon weapon = gamePlayer.getLoadout().getWeaponIgnoreMetadata(item.getItemStack());

        if (weapon != null && weapon instanceof Firearm) {
            BattleSound.play(BattleSound.ITEM_EQUIP, game, gamePlayer.getLocation());
            Firearm firearm = (Firearm) weapon;

            int maxAmmo = this.maxAmmo.getValue() * magazineSize.getValue();
            int pickupAmmo = firearm.getAmmo() + ammo.getValue();

            firearm.setAmmo(Math.min(maxAmmo, pickupAmmo));
            item.remove();
            return firearm.update();
        }

        if (itemSlot == null || itemSlot != ItemSlot.FIREARM_PRIMARY && itemSlot != ItemSlot.FIREARM_SECONDARY || gamePlayer.getLoadout().getWeapon(itemSlot) != null) {
            return true;
        }

        if (itemSlot == ItemSlot.FIREARM_PRIMARY) {
            gamePlayer.getLoadout().setPrimary(this);
        } else if (itemSlot == ItemSlot.FIREARM_SECONDARY) {
            gamePlayer.getLoadout().setSecondary(this);
        }

        BattleSound.play(BattleSound.ITEM_EQUIP, game, gamePlayer.getLocation());
        droppedItems.clear();
        item.remove();
        this.gamePlayer = gamePlayer;
        this.itemSlot = itemSlot;
        return update();
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
        reload(reloadDuration.getValue());
    }

    protected void reload(int reloadTime) {
        reloadCancelled = false;
        reloading = true;

        playReloadSound();

        gamePlayer.getPlayer().setFoodLevel(6); //Force the player to slow down

        new BattleRunnable() {
            public void run() {
                if (reloadCancelled || gamePlayer == null || !gamePlayer.getState().isAlive()) {
                    cancel();
                    reloadCancelled = false;
                    reloading = false;
                    setSoundCancelled(true, reloadSound);
                    return;
                }
                reloadType.getValue().reloadSingle(BattleFirearm.this, reloadDuration.getValue());
                if (ammo.getValue() <= 0 || magazine.getValue() >= magazineSize.getValue()) {
                    cancel();
                    gamePlayer.getPlayer().setFoodLevel(20);
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
        for (Item item : droppedItems) {
            item.remove();
        }
    }

    public void resetState() {
        ammo.applyModifier(new IntegerAttributeModifier(magazineSupply.getValue() * magazineSize.getValue()));
        magazine.applyModifier(new IntegerAttributeModifier(magazineSize.getValue()));
    }

    private void setSoundCancelled(boolean cancelled, Sound... sounds) {
        for (Sound sound : sounds) {
            sound.setCancelled(cancelled);
        }
    }

    public boolean update() {
        Placeholder[] placeholders = new Placeholder[] {
                new Placeholder("bg_ammo", ammo.getValue()),
                new Placeholder("bg_magazine", magazine.getValue()),
                new Placeholder("bg_weapon", name)
        };
        String displayName = messageHelper.createSimple(plugin.getBattlegroundsConfig().getWeaponDisplayName("firearm"), placeholders);

        itemStack = new ItemStackBuilder(itemStack)
                .addItemFlags(ItemFlag.values())
                .setAmount(1)
                .setDisplayName(displayName)
                .setLore(getLore())
                .setUnbreakable(true)
                .build();
        if (gamePlayer != null) {
            gamePlayer.getPlayer().getInventory().setItem(itemSlot.getSlot(), itemStack);
        }
        return gamePlayer != null;
    }
}
